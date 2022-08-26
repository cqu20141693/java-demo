package com.wujt.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wujt.mqtt.client.MqttClient;
import com.wujt.mqtt.client.MqttClientCallback;
import com.wujt.mqtt.client.MqttClientImpl;
import com.wujt.mqtt.client.MqttConnectResult;
import io.netty.buffer.Unpooled;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttVersion;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/18
 */
public class MqttClientTest {
    public static void main(String[] args) throws Exception {
        EventLoopGroup loop = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);

        dev(loop);
//        test(loop);
    }

    private static void test(EventLoopGroup loop) throws InterruptedException {


        // 子设备
        String childId = "cassandra1";
        // mqtt address
        String host = "118.24.224.99";
        int port = 1901;
        // mqtt auth
        String clientId = "ccgateway";
        String admin = "admin";
        String password = "cc@123456";
        String productId = "cctemplate";
        String gatewayId = "ccgateway";
//# The SQLAlchemy connection string.
//SQLALCHEMY_DATABASE_URI = "sqlite:///" + os.path.join(DATA_DIR, "superset.db")
//# SQLALCHEMY_DATABASE_URI = 'mysql://myapp@localhost/myapp'
//# SQLALCHEMY_DATABASE_URI = "sqlite:///" + os.path.join(DATA_DIR, "superset.db")
//SQLALCHEMY_DATABASE_URI = 'mysql+pymysql://superset:superset@192.168.96.163:3306/superset'
        glMqtt(loop, productId, gatewayId, host, port, clientId, admin, password, childId);
    }

    private static void dev(EventLoopGroup loop) throws InterruptedException {

        JSONObject json = getGatewayData();

//                                String topic = "/cctemplate/ccgateway/v1/devices/me/telemetry";

        // 子设备
        String childId = "cassandra1";
        JSONObject child = getChildData(json, childId);

        // mqtt address
        String host = "173.27.52.141";
        int port = 1883;
        // mqtt auth
        String clientId = "cassandra-gt";
        String admin = "admin";
        String password = "cc@123456";
        String productId = "cassandraP";
        String gatewayId = "cassandra-gt";
        glMqtt(loop, productId, gatewayId, host, port, clientId, admin, password, childId);
    }

    private static JSONObject getChildData(JSONObject json, String childId) {
        JSONObject child = new JSONObject();
        JSONArray children = new JSONArray();
        JSONObject value = new JSONObject();
        value.put("values", json);
        children.add(value);

        child.put(childId, children);
        return child;
    }

    private static JSONObject getGatewayData() {
        JSONObject json = new JSONObject();
        int nextInt = RandomUtils.nextInt(1, 100000);
        long nextLong = RandomUtils.nextLong(1, 100000000000L);
        float nextFloat = RandomUtils.nextFloat(1.1f, 100000.1f);
        double nextDouble = RandomUtils.nextDouble(1.1, 10000000.10001);
        JSONObject obj = new JSONObject();
        obj.put("a", nextInt);
        obj.put("b", RandomUtils.nextInt(100000, 100100));
        json.put("e-text", RandomStringUtils.randomAlphabetic(3));
        json.put("f-bool", nextInt % 2 == 0);
        json.put("e-long", nextLong);
        json.put("b-float", nextFloat);
        json.put("d-double", nextDouble);
        json.put("a-int", nextInt);
        json.put("obj", obj);
        JSONArray arr = new JSONArray();
        arr.add("good");
        arr.add("lucky");
        json.put("arr1", arr);
        return json;
    }

    private static void glMqtt(EventLoopGroup loop, String productId, String gatewayId, String host, int port, String clientId, String admin, String password, String childId) throws InterruptedException {
        MqttClient mqttClient = new MqttClientImpl(((t, payload) -> {
            System.out.println(t + "=>" + payload.toString(StandardCharsets.UTF_8));
        }));

        mqttClient.setEventLoop(loop);
        mqttClient.getClientConfig().setChannelClass(NioSocketChannel.class);

        mqttClient.getClientConfig().setClientId(clientId);

        mqttClient.getClientConfig().setUsername(admin);

        mqttClient.getClientConfig().setPassword(password);
        mqttClient.getClientConfig().setProtocolVersion(MqttVersion.MQTT_3_1);
        mqttClient.getClientConfig().setReconnect(false);
        mqttClient.setCallback(new MqttClientCallback() {
            @Override
            public void connectionLost(Throwable cause) {

                cause.printStackTrace();
            }

            @Override
            public void onSuccessfulReconnect() {

            }
        });

        mqttClient.connect(host, port)
                .addListener(future -> {
                    try {
                        MqttConnectResult result = (MqttConnectResult) future.get(15, TimeUnit.SECONDS);
                        if (result.getReturnCode() != MqttConnectReturnCode.CONNECTION_ACCEPTED) {
                            System.out.println("error:" + result.getReturnCode() + "--");
                            mqttClient.disconnect();
                        } else {
                            System.out.println("success:");
                            Task.scheduledPublish(() -> {
                                JSONObject json = getGatewayData();
                                JSONObject child = getChildData(json, childId);
                                String topic = String.join("/", "", productId, gatewayId, "v1/devices/me/telemetry");
                                String childTopic = String.join("/", "", productId, gatewayId, "v1/gateway/telemetry");
                                // gateway
                                mqttClient.publish(topic,
                                        Unpooled.copiedBuffer(json.toString(), StandardCharsets.UTF_8));
                                // child
                                mqttClient.publish(childTopic,
                                        Unpooled.copiedBuffer(child.toString(), StandardCharsets.UTF_8));
                            }, 1000, 5000, TimeUnit.MILLISECONDS);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).await(5, TimeUnit.SECONDS);
    }
}
