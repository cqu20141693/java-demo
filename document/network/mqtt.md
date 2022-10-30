### MQTT 协议分享
- **平台网关介绍**
- **协议讲解**
- **总结**
#### 平台网关介绍
1. 网络组件
```
UDP
TCP 服务端
TCP 客户端
MQTT 客户端
MQTT 服务端
COAP 客户端
COAP 服务端
HTTP 客户端
HTTP 服务端
Websocket 客户端
Websocket 服务端
流媒体服务
```
2. 证书管理
```
证书主要是用于安全交互的应用层协议，在数据交互前服务端和客户端相互身份认证，并约定安全密钥进行数据交换。
JKS,PEM,PFX： 是三种不同的证书保存格式，可以通过java工具颁发和OpenSSL颁发。
```
3. 协议包
```
以MQTT 网关协议包为例
协议包主要是规定了平台对客户端身份认证的实现和对推送MQTT 载荷进行数据解析为平台的事件。
```
4. [参考文档](https://blog.freessl.cn/ssl-cert-format-introduce/)


#### 协议讲解
以下介绍以MQTT3_1为主题，MQTT5_0提供了更多的能力。
##### 简介
```
MQTT 全称(Message Queue Telemetry Transport)：一种基于发布/订阅（publish/subscribe）模式的轻量级通讯协议，
通过订阅相应的主题来获取消息，是物联网（Internet of Thing）中的一个标准传输协议。该协议将消息的发布者（publisher）
与订阅者（subscriber）进行分离，同时提供MQTT Broker实现消息的路由；因此可以在不可靠的网络环境中，为远程连接的设备
提供可靠的消息服务，使用方式与传统的MQ有点类似。
```
![images](./images/mqtt.png)
##### 特点
1. 发布订阅
```
使用发布/订阅消息模式，提供一对多的消息发布，解除应用程序耦合。在通讯过程中，MQTT协议中有三种身份：发布者（Publish），代理（Broker）（服务器）、订阅者（Subscribe）。其中，消息的发布者和订阅者都是客户端，消息代理是服务器，消息发布者可以同时是订阅者
```
2. 协议自身负载低（固定长度的头部是2字节，即最小报文），降低网络流量，提高负载能力

3. 使用TCP/IP提供网络连接，稳定可靠。
```
当然也有UDP实现，各自使用该场景不同。
```
4. 有三种消息发布服务质量
``` 
Qos0至多一次”，消息发布完全依赖底层TCP/IP网络。会发生消息丢失或重复。

Qos1“至少一次”，确保消息到达，但消息重复可能会发生。

Qos2“只有一次”，确保消息到达一次。在一些要求比较严格的计费系统中，可以使用此级别。在计费系统中，消息重复或丢失会导致不正确的结果。
```
5. 遗言通知
```
客户端每次在推送消息是都可以携带遗言，表示客户端下线时，将消息通知到订阅者。
```
##### 协议
1. 协议支持的控制报文类型和标识

|名字|值|报文流动方向|描述|
|---|---|---|---|
|Reserved|	0|	禁止|	保留|
|CONNECT|	1|	客户端到服务端|	客户端请求连接服务端|
|CONNACK|	2|	服务端到客户端|	连接报文确认|
|PUBLISH|	3|	两个方向都允许|	发布消息|
|PUBACK|	4|	两个方向都允许|	QoS 1消息发布收到确认|
|PUBREC|	5|	两个方向都允许|	发布收到（保证交付第一步）|
|PUBREL|	6|	两个方向都允许|	发布释放（保证交付第二步）|
|PUBCOMP|	7|	两个方向都允许|	QoS 2消息发布完成（保证交互第三步）|
|SUBSCRIBE|	8|	客户端到服务端|	客户端订阅请求|
|SUBACK|	9|	服务端到客户端|	订阅请求报文确认|
|UNSUBSCRIBE|	10|	客户端到服务端	客户端取消订阅请求|
|UNSUBACK|	11|	服务端到客户端|	取消订阅报文确认|
|PINGREQ|	12|	客户端到服务端|	心跳请求|
|PINGRESP|	13|	服务端到客户端	心跳响应|
|DISCONNECT|	14|	两个方向都允许|	断开连接通知|
|AUTH|	15|	两个方向都允许|	认证信息交换|

|Control Package    |Fixed header flags     |bit3           |bit2           |bit1           |bit0|
|---    |---     |---           |---          |---           |---|
|CONNECT            |Reserved               |0              |0              |0              |0|
|CONNACK            |Reserved               |0              |0              |0              |0|
|PUBLISH            |Used in MQTT 3.1.1     |DUP1           |QoS2           |QoS2           |RETAIN3|
|PUBACK             |Reserved               |0              |0              |0              |0|
|PUBREC             |Reserved               |0              |0              |0              |0|
|PUBREL             |Reserved               |0              |0              |1              |0|
|PUBCOMP            |Reserved               |0              |0              |0              |0|
|SUBSCRIBE          |Reserved               |0              |0              |1              |0|
|SUBACK             |Reserved               |0              |0              |0              |0|
|UNSUBSCRIBE        |Reserved               |0              |0              |1              |0|
|UNSUBACK           |Reserved               |0              |0              |0              |0|
|PINGREQ            |Reserved               |0              |0              |0              |0|
|PINGRESP           |Reserved               |0              |0              |0              |0|
|DISCONNECT         |Reserved               |0              |0              |0              |0|
2. 协议报文格式
- 固定包头
```markdown
每一个MQTT控制包都包含一个固定包头。包含两个字节。
|Bit         |7       |6       |5       |4       |3       |2       |1           |0|
|byte 1      |控制报文类型           |特定于每个MQTT控制包类型的标志
|byte 2      |剩余长度
```
- 可变包头
```
存在于某些MQTT控制包
|Control Packet                 | variable packet header
|CONNECT                        |YES
|CONNACK                        |YES
|PUBLISH                        |YES 
|PUBACK                         |YES
|PUBREC                         |YES
|PUBREL                         |YES
|PUBCOMP                        |YES
|SUBSCRIBE                      |YES
|SUBACK                         |YES
|UNSUBSCRIBE                    |YES
|UNSUBACK                       |YES
|PINGREQ                        |NO
|PINGRESP                       |NO
|DISCONNECT                     |NO
``` 
- 载荷
```
存在于某些MQTT控制包
|Control Packet             |Payload
|CONNECT                    |Required
|CONNACK                    |None
|PUBLISH                    |Optional
|PUBACK                     |None
|PUBREC                     |None
|PUBREL                     |None
|PUBCOMP                    |None
|SUBSCRIBE                  |Required
|SUBACK                     |Required
|UNSUBSCRIBE                |Required
|UNSUBACK                   |None
|PINGREQ                    |None
|PINGRESP                   |None
|DISCONNECT                 |None
```

4. 经典报文
```
CONNECT - 第一个从客户端发送给服务端的包必须是CONNECT包
|Bit        |7      |6      |5      |4      |3      |2      |1      |0
|byte 1     |MQTT Control Packet type (1)   |Reserved
|           |1      |0      |0      |0      |0      |0      |1      |0
|byte 2     |Remaining Length
固定包头： 标识了控制报文类型和剩余长度

可变包头： 标明了协议名称和版本信息，同时标识了连接标识，负载中是否携带用户名，密码，遗言信息，cleanSession 和keepAlive 信息。
载荷： 包含客户端唯一标识、遗言Topic、遗言Message以及用户名和密码。

CONNACK - 确认收到连接请求，服务端发往客户端的第一个包一定是CONNACK包

固定包头： 标识了控制报文类型和剩余长度
可变包头： 标识连接成功或者失败，失败错误码信息
载荷： 无
```
```
PUBLISH - 发布消息
|Bit        |7  |6  |5  |4                  |3          |2  |1      |0
|byte 1     |MQTT Control Packet type (3)   |DUP flag   |QoS level  |RETAIN
|           |0  |0  |1  |1                  |X          |X  |X      |X
|byte 2     |Remaining Length
固定包头： 除了标识了控制报文类型和剩余长度，还标识了是否为重复发送，服务质量，是否保留消息
可变包头： 包括了topic信息和报文唯一标识
载荷： 向对应主题发送消息数据

PUBACK - 发布确认
固定包头： 除了标识了控制报文类型和剩余长度
可变包头： 确认的那个PUBLISH包的包唯一标识
载荷： 无

```
![0](./images/qos0.jpg)
![1](./images/qos1.jpg)
![2](./images/qos2.jpg)
```
SUBSCRIBE - 订阅话题

固定包头：标识了控制报文类型和剩余长度
可变包头：两个字节包唯一标识
载荷： 要订阅的主题以及QoS

SUBACK - 订阅确认

固定包头： 标识了控制报文类型和剩余长度
可变包头：确认的SUBSCRIBE包的包唯一标识
载荷： 确认订阅topic状态码列表，标识订阅是否成功
```

5. 平台mqtt实现
```
平台主要通过Connect 报文进行客户端的身份认证，在通过Publish报文推送数据到平台，
平台通过在协议包中解析publish报文中的topic和payload进行业务数据解析，将数据发送到平台事件进行数据处理。
```

#### 总结
MQTT 协议是一个非常简单经典的协议，有兴趣了解和学习网络协议栈的同学可以从其入手。
它也因为简单易用被各个领域使用:物联网数采、移动互联网、智能硬件、车联网、电力能源等领域.

