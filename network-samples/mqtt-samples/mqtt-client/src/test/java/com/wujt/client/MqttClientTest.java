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
import io.netty.util.concurrent.Future;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/18
 */
public class MqttClientTest {
    public static void main(String[] args) throws Exception {
        EventLoopGroup loop = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);

        //dev(loop);
//        test(loop);

//        local(loop);
        beanchTest(loop);
    }

    private static void beanchTest(EventLoopGroup loop) throws InterruptedException {
        // 子设备
//        String childId = "202209091120";
        List<String> childIds = new ArrayList<>();
        int index = 0;
        for (int i = index; i < index + 1000; i++) {
            childIds.add("ccchild" + i);
        }
        String childProduct="testC";
        // mqtt address
        String host = "10.113.75.71";
        int port = 1883;
        // mqtt auth
        Set<String> clientIds = new HashSet<>(Arrays.asList("test-deviceid-1200"));

        String admin = "admin";
        String password = "Foxconn123!@#";

//# The SQLAlchemy connection string.
//SQLALCHEMY_DATABASE_URI = "sqlite:///" + os.path.join(DATA_DIR, "superset.db")
//# SQLALCHEMY_DATABASE_URI = 'mysql://myapp@localhost/myapp'
//# SQLALCHEMY_DATABASE_URI = "sqlite:///" + os.path.join(DATA_DIR, "superset.db")
//SQLALCHEMY_DATABASE_URI = 'mysql+pymysql://superset:superset@192.168.96.163:3306/superset'
        for (String clientId : clientIds) {
            glMqttCreateChildDevice(loop, host, port, clientId, admin, password, childIds,childProduct );
        }

    }

    private static void glMqttCreateChildDevice(EventLoopGroup loop, String host, int port, String clientId,
                                                String admin, String password, List<String> childIds,
                                                String childProduct) throws InterruptedException {
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

                            System.out.println("success connect:" + clientId);
                            for (String childId : childIds) {
                                String topic = clientId + "/v1/gateway/connect";
                                JSONObject json = new JSONObject();
                                json.put("productId",childProduct);
                                json.put("deviceId",childId);
                                json.put("deviceName",childId);
                                // gateway
                                mqttClient.publish(topic,
                                        Unpooled.copiedBuffer(json.toString(), StandardCharsets.UTF_8)).addListener(future1 -> {
                                    MqttConnectResult ret = (MqttConnectResult) future.get(15, TimeUnit.SECONDS);
                                    if (!ret.isSuccess()) {
                                        System.out.println("publish failed: " + topic);
                                        mqttClient.disconnect();
                                    }
                                });
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).await(5, TimeUnit.SECONDS);
    }


    private static void local(EventLoopGroup loop) throws InterruptedException {


        // 子设备
//        String childId = "202209091120";
        String childId = "";
        // mqtt address
        String host = "localhost";
        int port = 1884;
        // mqtt auth
        Set<String> clientIds = new HashSet<>(Arrays.asList("1542449208217583616", "1551395488596348928","test-bench"));
        int index = 200;
        for (int i = index; i < index + 200; i++) {
            clientIds.add("ccgateway" + i);
        }
        String admin = "admin";
        String password = "cc@123456";
//# The SQLAlchemy connection string.
//SQLALCHEMY_DATABASE_URI = "sqlite:///" + os.path.join(DATA_DIR, "superset.db")
//# SQLALCHEMY_DATABASE_URI = 'mysql://myapp@localhost/myapp'
//# SQLALCHEMY_DATABASE_URI = "sqlite:///" + os.path.join(DATA_DIR, "superset.db")
//SQLALCHEMY_DATABASE_URI = 'mysql+pymysql://superset:superset@192.168.96.163:3306/superset'
        for (String clientId : clientIds) {
            glMqttPublish(loop, host, port, clientId, admin, password, childId, 500, false);
        }

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
//# The SQLAlchemy connection string.
//SQLALCHEMY_DATABASE_URI = "sqlite:///" + os.path.join(DATA_DIR, "superset.db")
//# SQLALCHEMY_DATABASE_URI = 'mysql://myapp@localhost/myapp'
//# SQLALCHEMY_DATABASE_URI = "sqlite:///" + os.path.join(DATA_DIR, "superset.db")
//SQLALCHEMY_DATABASE_URI = 'mysql+pymysql://superset:superset@192.168.96.163:3306/superset'
        glMqttPublish(loop, host, port, clientId, admin, password, childId);
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
        glMqttPublish(loop, host, port, clientId, admin, password, childId);
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

    private static void glMqttPublish(EventLoopGroup loop, String host, int port, String clientId, String admin, String password, String childId) throws InterruptedException {
        int period = 5;
        glMqttPublish(loop, host, port, clientId, admin, password, childId, period, false);
    }

    private static void glMqttPublish(EventLoopGroup loop, String host, int port, String clientId, String admin,
                                      String password, String childId, int period, boolean random) throws InterruptedException {
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

                            System.out.println("success connect:" + clientId);

                            if (random) {
                                Task.schedule(() -> sendMsg(clientId, childId, mqttClient, future, random, period),
                                        15, TimeUnit.MILLISECONDS);
                            } else {
                                Task.scheduleAtFixedRate(() -> sendMsg(clientId, childId, mqttClient, future, random, period),
                                        1, period, TimeUnit.MILLISECONDS);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).await(5, TimeUnit.SECONDS);
    }

    private static void sendMsg(String clientId, String childId, MqttClient mqttClient,
                                Future<? super MqttConnectResult> future, boolean random, int period) {
        JSONObject json = getGatewayData();

        String topic = clientId + "/v1/devices/me/telemetry";
        String childTopic = clientId + "/v1/gateway/telemetry";
        // gateway
        mqttClient.publish(topic,
                Unpooled.copiedBuffer(json.toString(), StandardCharsets.UTF_8)).addListener(future1 -> {
            MqttConnectResult ret = (MqttConnectResult) future.get(15, TimeUnit.SECONDS);
            if (!ret.isSuccess()) {
                System.out.println("publish failed: " + childId);
                mqttClient.disconnect();
            }
        });

        if (!StringUtils.isBlank(childId)) {
            JSONObject child = getChildData(json, childId);
            // child
            mqttClient.publish(childTopic,
                    Unpooled.copiedBuffer(child.toString(), StandardCharsets.UTF_8));
        }
        if (random) {
            long delay = RandomUtils.nextLong(5, period);
            System.out.println(clientId + " next msg time=" + delay);
            Task.schedule(() -> sendMsg(clientId, childId, mqttClient, future, random, period),
                    delay, TimeUnit.SECONDS);
        }
    }
}
