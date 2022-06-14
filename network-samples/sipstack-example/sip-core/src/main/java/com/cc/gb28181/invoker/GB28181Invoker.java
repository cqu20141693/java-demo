package com.cc.gb28181.invoker;

import com.cc.gb28181.gateway.GB28181Device;
import com.cc.gb28181.message.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sip.ClientTransaction;
import javax.sip.RequestEvent;
import javax.sip.message.Request;
import java.util.Date;

/**
 * GB28181指令执行器,用于向设备发送相应的指令
 * <p>
 * wcc 2022/5/24
 */
public interface GB28181Invoker {

    /**
     * 发送订阅报警指令
     *
     * @param device             设备信息
     * @param startAlarmPriority 开始报警级别
     * @param endAlarmPriority   结束报警级别
     * @param alarmMethod        报警方式
     * @param startAlarmTime     开始报警时间
     * @param endAlarmTime       结束报警时间
     * @return void
     */
    Void subscribe(@Nonnull GB28181Device device,
                   int startAlarmPriority,
                   int endAlarmPriority,
                   @Nonnull String alarmMethod,
                   @Nullable Date startAlarmTime,
                   @Nullable Date endAlarmTime);

    /**
     * 发送设备控制指令,{@link DeviceControl#getDeviceId()}为通道ID
     *
     * @param device  设备信息
     * @param command 控制指令
     * @return void
     */
    Void deviceControl(GB28181Device device, DeviceControl command);

    /**
     * 获取设备详细信息
     *
     * @param device 设备信息
     * @return 详细信息
     */
    GB28181Device requestDeviceInfo(GB28181Device device);

    /**
     * 获取通道列表
     *
     * @param device 设备信息
     * @return 通道列表
     */
    CatalogInfo readCatalog(GB28181Device device);

    /**
     * 查询录像文件
     *
     * @param request 查询请求
     * @return 录像文件信息
     */
    RecordFile queryRecord(GB28181Device device, RecordInfoRequest request);

    /**
     * 查询预置位信息
     *
     * @param device 设备ID
     * @return 预置位信息
     */
    PresetQuery queryPreset(GB28181Device device);

    /**
     * 订阅通道目录，发送订阅请求后,通过{@link GB28181Invoker#handleMessage}将会收到
     *
     * @param device device
     * @param from   订阅有效期从
     * @param to     订阅有效期止
     * @return void
     */
    Void subscribeCatalog(GB28181Device device, Date from, Date to);


    /**
     * 下载设备配置信息，可指定配置类型，如果未指定类型则获取所有类型的配置
     *
     * @param device     设备信息
     * @param configType 要下载的配置类型
     * @return 配置信息
     */
    ConfigDownload downloadConfig(GB28181Device device, ConfigDownload.ConfigType... configType);

    /**
     * 发送SIP原始请求
     *
     * @param device   设备
     * @param request  原始请求
     * @param awaitAck 是否等待响应
     * @return 事务信息
     */
    ClientTransaction request(GB28181Device device, Request request, boolean awaitAck);

    /**
     * 发起一个请求，并等待响应，不同的请求方式以及内容，响应的内容不同。
     *
     * @param transaction ClientTransaction
     * @param request     Request
     * @param awaitAck    是否等待响应
     * @return 响应结果
     */
    Object request(ClientTransaction transaction, Request request, boolean awaitAck);

    /**
     * 监听来自远程的可识别的设备消息,如: 设备信息等
     *
     * @return 数据流 tp1为原始数据，tp2为解析后等数据
     * @see GB28181Device
     * @see CatalogInfo
     * @see KeepaliveMessage
     */
    HandleDeviceMessage handleMessage();

    RequestEvent handleRequest(String method);

    Void deviceConfigModifyName(String deviceId, String name, GB28181Device device);
}
