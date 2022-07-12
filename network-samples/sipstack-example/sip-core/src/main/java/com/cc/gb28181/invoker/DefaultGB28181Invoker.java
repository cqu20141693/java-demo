package com.cc.gb28181.invoker;

import com.cc.gb28181.gateway.GB28181Device;
import com.cc.gb28181.message.*;
import com.cc.sip.RequestBuilder;
import com.cc.sip.SipLayer;
import com.cc.sip.SipProperties;
import com.cc.util.RequestIdSupplier;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sip.ClientTransaction;
import javax.sip.RequestEvent;
import javax.sip.header.CallIdHeader;
import javax.sip.header.Header;
import javax.sip.message.Request;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * GB28181 down invoker
 * wcc 2022/5/24
 */
@Data
@Slf4j
public class DefaultGB28181Invoker implements GB28181Invoker {
    public final static XmlMapper mapper = new XmlMapper();

    private final SipProperties properties;

    private final SipLayer sipLayer;
    // 应用使用caffeine 实现过期能力
    private Map<String, Object> processor = new ConcurrentHashMap<>();
    private boolean autoAck;
    private final RequestIdSupplier requestIdInc = new RequestIdSupplier();

    private Map<String, Consumer<RequestEvent>> handlers = new HashMap<>();

    public DefaultGB28181Invoker(SipProperties properties, SipLayer sipLayer, boolean autoAck) {
        this.properties = properties;
        this.sipLayer = sipLayer;
        this.autoAck = autoAck;
        // this.sipLayer.addListener(this);
    }

    private int nextSN() {
        return requestIdInc.nextId(id -> processor.containsKey(String.valueOf(id)));
    }

    public DefaultGB28181Invoker(SipProperties properties, SipLayer sipLayer) {
        this(properties, sipLayer, true);
    }

    @SneakyThrows
    private ClientTransaction createTransaction(String method,
                                                String xml,
                                                String deviceId,
                                                GB28181Device device,
                                                String viaTag,
                                                String fromTag,
                                                String toTag,
                                                Header... headers) {
        Request request = sipLayer
                .newRequestBuilder(properties)
                .requestLine(deviceId, device.getHost(), device.getPort())
                .method(method)
                .user(device.getId())
                .via(properties.getPublicAddress(), properties.getPublicPort(), device.getTransport(), viaTag)
                .from(properties.getSipId(), properties.getHostAndPort(), fromTag)
                .to(deviceId, device.getHostAndPort(), toTag)
                .content(xml.getBytes(properties.getCharset()), RequestBuilder.APPLICATION_MANSCDP_XML)
                .contact(properties.getSipId(), properties.getPort())
                .build();
        log.debug("prepare SIP request \n{}", request);
        for (Header header : headers) {
            request.addHeader(header);
        }
        return sipLayer.newTransaction(device.getTransport(), request);
    }

    private void doRequest(Function<Integer, String> bodyBuilder,
                           String requestMethod,
                           GB28181Device device,
                           String viaTag,
                           String fromTag,
                           String toTag,
                           Header... headers) {
        doRequest(bodyBuilder, requestMethod, device.getDeviceId(), device, viaTag, fromTag, toTag, headers);
    }

    @SuppressWarnings("all")
    private void doRequest(Function<Integer, String> bodyBuilder,
                           String requestMethod,
                           String deviceId,
                           GB28181Device device,
                           String viaTag,
                           String fromTag,
                           String toTag,
                           Header... headers) {
        int id = nextSN();
        ClientTransaction transaction = createTransaction(requestMethod,
                bodyBuilder.apply(id),
                deviceId,
                device,
                viaTag,
                fromTag,
                toTag,
                headers);
        CallIdHeader callIdHeader = (CallIdHeader) transaction.getRequest().getHeader(CallIdHeader.NAME);
        String callId = callIdHeader.getCallId();
        String SN = String.valueOf(deviceId + ":" + id);
        processor.put(SN, null);
        try {
            processor.put(callId, null);
            transaction.sendRequest();
        } catch (Throwable e) {
            processor.remove(SN);
            processor.remove(callId);
        }

    }

    @Override
    public void subscribe(@Nonnull GB28181Device device, int startAlarmPriority, int endAlarmPriority, @Nonnull String alarmMethod, @Nullable Date startAlarmTime, @Nullable Date endAlarmTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String startAlarmTimeString = startAlarmTime == null ? "" : format.format(startAlarmTime);
        String endAlarmTimeString = endAlarmTime == null ? "" : format.format(endAlarmTime);
        this.doRequest(sn ->
                        "<?xml version=\"1.0\" encoding=\"" + properties.getCharset() + "\"?>\r\n" +
                                "<Query>\r\n" +
                                "<CmdType>Alarm</CmdType>\r\n" +
                                "<SN>" + sn + "</SN>\r\n" +
                                "<DeviceID>" + device.getId() + "</DeviceID>\r\n" +
                                "<StartAlarmPriority>" + startAlarmPriority + "</StartAlarmPriority>\n" +
                                "<EndAlarmPriority>" + endAlarmPriority + "</EndAlarmPriority>\n" +
                                "<AlarmMethod>" + alarmMethod + "</AlarmMethod>\n" +
                                "<StartTime>" + startAlarmTimeString + "</StartTime>\n" +
                                "<EndTime>" + endAlarmTimeString + "</EndTime>\n" +
                                "</Query>\r\n",
                Request.SUBSCRIBE,
                device,
                null,
                "BK32B1U8DKDrB",
                null
        );
    }

    @Override
    public void deviceControl(GB28181Device device, DeviceControl command) {

    }

    @Override
    public GB28181Device requestDeviceInfo(GB28181Device device) {
        return null;
    }

    @Override
    public CatalogInfo readCatalog(GB28181Device device) {
        return null;
    }

    @Override
    public RecordFile queryRecord(GB28181Device device, RecordInfoRequest request) {
        return null;
    }

    @Override
    public PresetQuery queryPreset(GB28181Device device) {
        return null;
    }

    @Override
    public Void subscribeCatalog(GB28181Device device, Date from, Date to) {
        return null;
    }

    @Override
    public ConfigDownload downloadConfig(GB28181Device device, ConfigDownload.ConfigType... configType) {
        return null;
    }

    @Override
    public ClientTransaction request(GB28181Device device, Request request, boolean awaitAck) {
        return null;
    }

    @Override
    public Object request(ClientTransaction transaction, Request request, boolean awaitAck) {
        return null;
    }

    @Override
    public HandleDeviceMessage handleMessage() {
        return null;
    }

    @Override
    public void handleRequest(String method, Consumer<RequestEvent> handler) {
        log.info("add {} request handler", method);
        handlers.put(method, handler);
    }

    @Override
    public Void deviceConfigModifyName(String deviceId, String name, GB28181Device device) {
        return null;
    }
}
