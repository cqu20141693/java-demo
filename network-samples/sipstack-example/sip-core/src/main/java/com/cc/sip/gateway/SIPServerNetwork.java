package com.cc.sip.gateway;

import com.alibaba.fastjson.JSONObject;
import com.cc.gb28181.gateway.GB28181Device;
import com.cc.gb28181.invoker.AuthenticationHelper;
import com.cc.gb28181.invoker.DefaultGB28181Invoker;
import com.cc.gb28181.media.MediaGateway;
import com.cc.gb28181.media.server.DeviceStreamInfo;
import com.cc.gb28181.stream.StreamMode;
import com.cc.netwok.Network;
import com.cc.netwok.NetworkType;
import com.cc.netwok.session.SessionManager;
import com.cc.sip.SipLayer;
import com.cc.sip.SipLayerFactory;
import com.cc.sip.SipProperties;
import com.cc.spi.DeviceFacade;
import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.Expires;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.sip.RequestEvent;
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.Calendar;
import java.util.Locale;

import static com.cc.sip.SipUtils.getServerTransaction;

/**
 * SIP network server
 * wcc 2022/6/23
 */
@Data
@Slf4j
public class SIPServerNetwork implements Network, MediaGateway {
    private final SipLayerFactory sipLayerFactory;

    private final SipProperties properties;

    private SipLayer sipLayer;

    private DefaultGB28181Invoker invoker;

    private DeviceFacade deviceService;

    private SessionManager sessionManager;

    @Override
    public String getId() {
        return "sip-server";
    }


    @Override
    public void start() {
        if (sipLayer == null || sipLayer.isDisposed()) {
            initSip(sipLayerFactory.createSipLayer(properties));
            log.info("listen device status");
        }

    }

    private void initSip(SipLayer sipLayer) {
        this.sipLayer = sipLayer;
        initListener();
    }

    private void initListener() {
        this.invoker = new DefaultGB28181Invoker(properties, sipLayer);

        //监听注册消息
        invoker.handleRequest(Request.REGISTER, this::handleDeviceRegister);

        //处理可解析的消息,如KeepAlive等

//        this.invoker
//                .handleMessage()
//                .flatMap(tp2 -> this.handleSipDeviceMessage(tp2.getT1(), tp2.getT2()))
//
//
//        //监听来自设备的消息
//        disposable.add(
//                this.invoker
//                        .handleMessage()
//                        .flatMap(tp3 -> this
//                                .handleMessage(tp3.getT1(), tp3.getT2())
//                                .onErrorResume(err -> {
//                                    log.error("handle message error", err);
//                                    return Mono.empty();
//                                }))
//                        .subscribe()
//        );
    }

    //处理设备注册
    @SneakyThrows
    private void handleDeviceRegister(RequestEvent event) {

        Request request = event.getRequest();
        Response response;
        try {
            if (!AuthenticationHelper.doAuthenticatePlainTextPassword(request, properties.getPassword())) {
                response = sipLayer.getMessageFactory().createResponse(Response.UNAUTHORIZED, request);
                AuthenticationHelper.generateChallenge(sipLayer.getHeaderFactory(), response, properties.getDomain());
                getServerTransaction(sipLayer, event).sendResponse(response);
                return;
            } else {
                response = sipLayer.getMessageFactory().createResponse(Response.OK, request);
                // 添加date头
                response.addHeader(sipLayer
                        .getHeaderFactory()
                        .createDateHeader(Calendar.getInstance(Locale.ENGLISH)));
                ExpiresHeader expiresHeader = (ExpiresHeader) request.getHeader(Expires.NAME);
                if (expiresHeader != null) {
                    // 添加Expires头
                    response.addHeader(expiresHeader);
                }
                // 添加Contact头
                if (request.getHeader(ContactHeader.NAME) != null) {
                    response.addHeader(request.getHeader(ContactHeader.NAME));
                }
                FromHeader fromHeader = (FromHeader) request.getHeader(FromHeader.NAME);
                ViaHeader viaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
                String received = viaHeader.getReceived();
                int rPort = viaHeader.getRPort();
                // 本地模拟设备 received 为空 rPort 为 -1
                // 解析本地地址替代
                if (StringUtils.isEmpty(received) || rPort == -1) {
                    received = viaHeader.getHost();
                    rPort = viaHeader.getPort();
                }
                AddressImpl address = (AddressImpl) fromHeader.getAddress();
                SipUri uri = (SipUri) address.getURI();
                String deviceId = uri.getUser();
                String transport = viaHeader.getTransport();

                GB28181Device device = new GB28181Device();
                device.setId(deviceId);
                device.setHost(received);
                device.setPort(rPort);
                device.setStreamMode(StreamMode.UDP);
                device.setTransport(transport);
                getServerTransaction(sipLayer, event).sendResponse(response);
                //注销
                if (expiresHeader != null && expiresHeader.getExpires() == 0) {
                    this.handleUnRegister(device);
                } else {

                    // 判断id是否在平台已存在，已存在使用平台的设备名字
                    GB28181Device deviceInfo = deviceService.getDevice(deviceId);
                    if (deviceInfo != null) {
                        handleDeviceRegister(device);
                    } else {
                        log.info("video device={} not exist", deviceId);
                    }
                    //                    return localDeviceInstanceService.findById(deviceId).defaultIfEmpty(new DeviceInstanceEntity())
//                            .flatMap(deviceInstanceEntity -> {
//                                if (deviceInstanceEntity.getId() == null) {
//                                    return handleDevice(productId, device);
//                                } else {
//                                    device.setName(deviceInstanceEntity.getName());
//                                    return productVersionService.getMaxVideoVersion(deviceInstanceEntity.getProductId()).flatMap(p -> {
//                                        return this.handleDeviceRegister(device);
//                                    }).then();
//                                }
//                            });
                }
            }
        } catch (Throwable err) {
            log.error("handle sip request error\n{}", request, err);
            response = sipLayer.getMessageFactory().createResponse(Response.UNAUTHORIZED, request);
            getServerTransaction(sipLayer, event).sendResponse(response);
        }

    }

    private void handleDeviceRegister(GB28181Device device) {

    }

    private boolean checkDevice(String deviceId) {
        return true;
    }

    //处理设备注销
    private void handleUnRegister(GB28181Device device) {
        log.debug("handle sip device[{}] unregister，session clean and send device offline event", JSONObject.toJSONString(device));
        sessionManager.unregister(device.getDeviceId());

    }


    @Override
    public void syncChannel(String id) {

    }

    @Override
    public Boolean closeStream(DeviceStreamInfo streamInfo) {
        return null;
    }

    @Override
    public void dispose() {

    }

    @Override
    public NetworkType getType() {
        return null;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public boolean isAutoReload() {
        return false;
    }
}
