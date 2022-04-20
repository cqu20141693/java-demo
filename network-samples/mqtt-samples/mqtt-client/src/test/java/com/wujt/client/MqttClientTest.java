package com.wujt.client;

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

        MqttClient mqttClient = new MqttClientImpl(((topic, payload) -> {
            System.out.println(topic + "=>" + payload.toString(StandardCharsets.UTF_8));
        }));

        mqttClient.setEventLoop(loop);
        mqttClient.getClientConfig().setChannelClass(NioSocketChannel.class);
        mqttClient.getClientConfig().setClientId("gateway2222222222222222222222222");
        mqttClient.getClientConfig().setUsername("RTzQdVDPQEmEr5g2");
        mqttClient.getClientConfig().setPassword("G:wfF1wpKDBkNRKZVv");
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
        mqttClient.connect("172.30.203.21", 1883)
                .addListener(future -> {
                    try {
                        MqttConnectResult result = (MqttConnectResult) future.get(15, TimeUnit.SECONDS);
                        if (result.getReturnCode() != MqttConnectReturnCode.CONNECTION_ACCEPTED) {
                            System.out.println("error:" + result.getReturnCode() + "--");
                            mqttClient.disconnect();
                        } else {
                            System.out.println("success:");
//                            mqttClient.publish("signal_report",
//                                    Unpooled.copiedBuffer("{\"type\":\"read-property\"}", StandardCharsets.UTF_8));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).await(5, TimeUnit.SECONDS);

    }
}
