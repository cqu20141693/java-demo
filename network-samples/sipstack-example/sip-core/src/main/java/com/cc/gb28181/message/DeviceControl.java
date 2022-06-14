package com.cc.gb28181.message;

import com.cc.sip.SipDeviceMessage;
import com.cc.things.deivce.DeviceMessage;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 设备控制指令
 * wcc 2022/5/24
 */
@Getter
@Setter
@Slf4j
public class DeviceControl implements SipDeviceMessage {

    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    @JacksonXmlProperty(localName = "SN")
    private String sn;

    @JacksonXmlProperty(localName = "PTZCmd")
    private String ptzCmd;

    //Record,StopRecord
    @JacksonXmlProperty(localName = "RecordCmd")
    private String recordCmd;

    //Guard,ResetGuard
    @JacksonXmlProperty(localName = "GuardCmd")
    private String guardCmd;

    @JacksonXmlProperty(localName = "AlarmCmd")
    private String alarmCmd;

    @JacksonXmlProperty(localName = "IFameCmd")
    private String iFameCmd;

    @JacksonXmlProperty(localName = "DragZoomIn")
    private DragZoom dragZoomIn;

    @JacksonXmlProperty(localName = "DragZoomOut")
    private DragZoom dragZoomOut;

    //看守位控制命令
    @JacksonXmlProperty(localName = "HomePosition")
    private HomePosition homePosition;

    /**
     * 发送指令给平台的设备
     *
     * @return 是否成功
     */
    public Boolean sendMessage() {
        List<Boolean> jobs = new ArrayList<>();

        //控制指令
        if (hasPtzCmd()) {
            ControlCmd<?> cmd = parseCmd().orElseThrow(() -> new UnsupportedOperationException("不支持的控制指令:" + ptzCmd));
            jobs.add(cmd.sendMessage(deviceId));
        }

        //看守位
        if (hasHomePosition()) {
            jobs.add(homePosition.sendMessage(deviceId));
        }
        // TODO: 2021/6/10 其他

        if (jobs.isEmpty()) {
            throw new UnsupportedOperationException("不支持的设备控制指令");
        } else {
            return jobs.stream().allMatch(Boolean::booleanValue);
        }
    }

    public Optional<ControlCmdType> parseCmdType() {
        if (StringUtils.isEmpty(ptzCmd)) {
            return Optional.empty();
        }
        return ControlCmdType.of(ptzCmd);
    }

    public Optional<ControlCmd<?>> parseCmd() {
        return parseCmdType().map(type -> type.decode(ptzCmd));
    }

    public void setControlCmd(ControlCmd<?> cmd) {
        setPtzCmd(cmd.encode());
    }

    public boolean hasPtzCmd() {
        return StringUtils.hasText(ptzCmd);
    }

    public boolean hasRecordCmd() {
        return StringUtils.hasText(recordCmd);
    }

    public boolean hasGuardCmd() {
        return StringUtils.hasText(guardCmd);
    }

    public boolean hasAlarmCmd() {
        return StringUtils.hasText(alarmCmd);
    }

    public boolean hasIFameCmd() {
        return StringUtils.hasText(iFameCmd);
    }

    public boolean hasHomePosition() {
        return homePosition != null;
    }

    public boolean hasDragZoomOut() {
        return dragZoomOut != null;
    }

    public boolean hasDragZoomIn() {
        return dragZoomIn != null;
    }

    @Override
    public DeviceMessage toDeviceMessage() {
        return null;
    }

    /*
     拉框控制命令
     注:拉框放大命令将播放窗口选定框内的图像放大到整个播放窗口;
     拉框缩小命令将整个播放窗口的图像缩小到播放窗口选定框内;
     命令中的坐标系以播放窗口的左上角为原点,各坐标取值以像素为单位。
     */
    @Getter
    @Setter
    public static class DragZoom {
        //播放窗口长度像素值
        @JacksonXmlProperty(localName = "Length")
        private int length;

        //播放窗口宽度像素值
        @JacksonXmlProperty(localName = "Width")
        private int width;

        //拉框中心的横轴坐标像素值
        @JacksonXmlProperty(localName = "MidPointX")
        private int midPointX;

        //拉框中心的纵轴坐标像素值
        @JacksonXmlProperty(localName = "MidPointY")
        private int midPointY;

        //拉框长度像素值
        @JacksonXmlProperty(localName = "LengthX")
        private int lengthX;

        //拉框宽度像素值
        @JacksonXmlProperty(localName = "LengthY")
        private int lengthY;

        public String toXml() {

            return "<Length>" + length + "</Length>" +
                    "<Width>" + width + "</Width>" +
                    "<MidPointX>" + midPointX + "</MidPointX>" +
                    "<MidPointY>" + midPointY + "</MidPointY>" +
                    "<LengthX>" + lengthX + "</LengthX>" +
                    "<LengthY>" + lengthY + "</LengthY>";
        }
    }

    //看守位控制命令
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HomePosition {

        //看守位使能1:开启,0:关闭
        @JacksonXmlProperty(localName = "Enabled")
        private int enabled;

        //自动归位时间间隔,开启看守位时使用,单位:秒(s)
        @JacksonXmlProperty(localName = "ResetTime")
        private Integer resetTime;

        //调用预置位编号,开启看守位时使用,取值范围0~255
        @JacksonXmlProperty(localName = "PresetIndex")
        private Integer presetIndex;

        public String toXml() {
            StringBuilder builder = new StringBuilder();
            builder.append("<HomePosition>");

            builder.append("<Enabled>").append(enabled).append("</Enabled>");
            if (resetTime != null) {
                builder.append("<ResetTime>").append(resetTime).append("</ResetTime>");
            }
            if (presetIndex != null) {
                builder.append("<PresetIndex>").append(presetIndex).append("</PresetIndex>");
            }
            builder.append("</HomePosition>");


            return builder.toString();
        }

        public Boolean sendMessage(String channelId) {

            return true;
        }
    }

    @Getter
    @Setter
    public static class AlarmCmdInfo {

        //复位报警的报警方式属性
        @JacksonXmlProperty(localName = "AlarmMethod")
        private String alarmMethod;

        @JacksonXmlProperty(localName = "AlarmType")
        private String alarmType;
    }

    public String toXml(int sn, String charset) {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
        joiner.add("<Control>");
        joiner.add("<CmdType>DeviceControl</CmdType>");
        joiner.add("<SN>" + sn + "</SN>");
        joiner.add("<DeviceID>" + deviceId + "</DeviceID>");

        if (hasPtzCmd()) {
            joiner.add("<PTZCmd>" + getPtzCmd() + "</PTZCmd>");
        }
        if (hasHomePosition()) {
            joiner.add(homePosition.toXml());
        }

        if (hasDragZoomIn()) {
            joiner.add("<DragZoomIn>").add(dragZoomIn.toXml()).add("</DragZoomIn>");
        }
        if (hasDragZoomOut()) {
            joiner.add("<DragZoomOut>").add(dragZoomOut.toXml()).add("</DragZoomOut>");
        }

        if (hasRecordCmd()) {
            joiner.add("<RecordCmd>" + recordCmd + "</RecordCmd>");
        }
        if (hasGuardCmd()) {
            joiner.add("<GuardCmd>" + guardCmd + "</GuardCmd>");
        }
        if (hasAlarmCmd()) {
            joiner.add("<AlarmCmd>" + alarmCmd + "</AlarmCmd>");
        }
        if (hasIFameCmd()) {
            joiner.add("<IFameCmd>" + iFameCmd + "</IFameCmd>");
        }
        joiner.add("<Info>");
        joiner.add("<ControlPriority>10</ControlPriority>");
        joiner.add("</Info>");
        joiner.add("</Control>");

        return joiner.toString();
    }

    public interface ControlCmd<SELF extends ControlCmd<SELF>> {

        SELF decode(String hexString);

        String encode();

        default boolean isPTZ() {
            return this instanceof PTZCmd;
        }

        default PTZCmd asPtz() {
            return ((PTZCmd) this);
        }

        default boolean isPreset() {
            return this instanceof PresetCmd;
        }

        default PresetCmd asPreset() {
            return ((PresetCmd) this);
        }

        Boolean sendMessage(String channelId);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PTZCmd extends AbstractControlCmd<PTZCmd> {
        private Map<Direct, Integer> directAndSpeed;

        public PTZCmd() {
            this(new HashMap<>());
        }

        public Map<String, Integer> toStringKey() {
            return directAndSpeed
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> e.getKey().name(), Map.Entry::getValue));
        }

        @Override
        public final Boolean sendMessage(String channelId) {
            log.info("send message by invoker");
            return true;
        }

        @Override
        protected int getCode() {
            if (directAndSpeed == null) {
                return 0;
            }
            int code = 0;
            for (Map.Entry<Direct, Integer> entry : directAndSpeed.entrySet()) {
                code = entry.getKey().merge(code);
            }
            return code;
        }

        @Override
        protected void setCode(int code) {
            Direct[] directs = Direct.values(code);
            for (Direct direct : directs) {
                directAndSpeed.put(direct, 0);
            }
        }

        @Override
        protected int getData1() {
            return directAndSpeed.getOrDefault(Direct.LEFT, directAndSpeed.getOrDefault(Direct.RIGHT, 0));
        }

        @Override
        protected void setData1(int data1) {
            if (data1 != 0) {
                if (directAndSpeed.containsKey(Direct.LEFT)) {
                    directAndSpeed.put(Direct.LEFT, data1);
                }
                if (directAndSpeed.containsKey(Direct.RIGHT)) {
                    directAndSpeed.put(Direct.RIGHT, data1);
                }
            }
        }

        @Override
        protected int getData2() {
            return directAndSpeed.getOrDefault(Direct.UP, directAndSpeed.getOrDefault(Direct.DOWN, 0));
        }

        @Override
        protected void setData2(int data2) {
            if (data2 != 0) {
                if (directAndSpeed.containsKey(Direct.UP)) {
                    directAndSpeed.put(Direct.UP, data2);
                }
                if (directAndSpeed.containsKey(Direct.DOWN)) {
                    directAndSpeed.put(Direct.DOWN, data2);
                }
            }
        }

        @Override
        protected int getCompose() {
            return directAndSpeed.getOrDefault(Direct.ZOOM_IN, directAndSpeed.getOrDefault(Direct.ZOOM_OUT, 0)) & 0xF;
        }

        @Override
        protected void setCompose(int compose) {
            if (compose != 0) {
                if (directAndSpeed.containsKey(Direct.ZOOM_IN)) {
                    directAndSpeed.put(Direct.ZOOM_IN, compose);
                }
                if (directAndSpeed.containsKey(Direct.ZOOM_OUT)) {
                    directAndSpeed.put(Direct.ZOOM_OUT, compose);
                }
            }
        }

//        private String doEncode() {
//            int code = 0;
//            StringBuilder cmd = new StringBuilder("A50F4D");
//            for (Map.Entry<Direct, Integer> entry : directAndSpeed.entrySet()) {
//                code = entry.getKey().merge(code);
//            }
//            //字节4: 控制码
//            cmd.append(String.format("%02X", code), 0, 2);
//            //字节5: 水平控制速度
//            int lrSpeed = directAndSpeed.getOrDefault(Direct.LEFT, directAndSpeed.getOrDefault(Direct.RIGHT, 0));
//            cmd.append(String.format("%02X", lrSpeed), 0, 2);
//            //字节6: 垂直控制速度
//            int udSpeed = directAndSpeed.getOrDefault(Direct.UP, directAndSpeed.getOrDefault(Direct.DOWN, 0));
//            cmd.append(String.format("%02X", udSpeed), 0, 2);
//            //字节7: 缩放控制速度
//            int zoomSpeed = directAndSpeed.getOrDefault(Direct.ZOOM_IN, directAndSpeed.getOrDefault(Direct.ZOOM_OUT, 0)) & 0xF;
//            cmd.append(String.format("%X", zoomSpeed), 0, 1)
//               .append("0");
//
//            //校验码
//            int checkCode = (0XA5 + 0X0F + 0X4D + code + lrSpeed + udSpeed + (zoomSpeed << 4)) % 256;
//            cmd.append(String.format("%02X", checkCode), 0, 2);
//            return cmd.toString();
//        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PresetCmd extends AbstractControlCmd<PresetCmd> {
        private PresetOperation operation;

        private int index;

        @Override
        protected int getCode() {
            return operation.getCode();
        }

        @Override
        protected void setCode(int code) {
            operation = PresetOperation
                    .of(code)
                    .orElseThrow(() -> new IllegalArgumentException("unknown preset code:" + code));
        }

        @Override
        protected int getData1() {
            return 0;
        }

        @Override
        protected void setData1(int data1) {

        }

        @Override
        protected int getData2() {
            return index;
        }

        @Override
        protected void setData2(int data2) {
            this.index = data2;
        }

        @Override
        protected int getCompose() {
            return 0;
        }

        @Override
        protected void setCompose(int compose) {

        }

        @Override
        public Boolean sendMessage(String channelId) {
            log.info("send message by invoker");
            return true;
        }
    }

    private static abstract class AbstractControlCmd<SELF extends AbstractControlCmd<SELF>> implements ControlCmd<SELF> {

        //字节4: 控制码
        protected abstract int getCode();

        protected abstract void setCode(int code);

        //字节5: 数据1
        protected abstract int getData1();

        protected abstract void setData1(int data1);

        //字节6: 数据2
        protected abstract int getData2();

        protected abstract void setData2(int data2);

        //字节7: 组合码
        protected abstract int getCompose();

        protected abstract void setCompose(int compose);

        public String encode() {
            int code = getCode();
            StringBuilder cmd = new StringBuilder("A50F4D");

            //字节4: 控制码
            cmd.append(String.format("%02X", code), 0, 2);
            //字节5: 数据1
            int data1 = getData1();
            cmd.append(String.format("%02X", data1), 0, 2);
            //字节6: 数据2
            int data2 = getData2();
            cmd.append(String.format("%02X", data2), 0, 2);
            //字节7: 组合码
            int compose = getCompose() & 0xF;
            cmd.append(String.format("%X", compose), 0, 1).append("0");

            //校验码
            int checkCode = (0XA5 + 0X0F + 0X4D + code + data1 + data2 + (compose << 4)) % 256;
            cmd.append(String.format("%02X", checkCode), 0, 2);
            return cmd.toString();
        }

        public SELF decode(String hexString) {
            char[] ptzCmdArr = hexString.toCharArray();
            //控制码
            int code = Integer.parseInt(new String(ptzCmdArr, 6, 2), 16);
            setCode(code);

            {
                //数据1
                setData1(Integer.parseInt(new String(ptzCmdArr, 8, 2), 16));
            }
            {
                //数据2
                setData2(Integer.parseInt(new String(ptzCmdArr, 10, 2), 16));
            }
            {
                //组合码
                setCompose(Integer.parseInt(new String(ptzCmdArr, 12, 1), 16));
            }
            return (SELF) this;
        }

    }

    @AllArgsConstructor
    public enum ControlCmdType {

        //云台控制
        PTZ(PTZCmd::new) {
            @Override
            boolean match(int code) {
                return 0 == code || Direct.test(code);
            }
        },
        //预置位
        Preset(PresetCmd::new) {
            @Override
            boolean match(int code) {
                return code == 0x81 || code == 0x82 || code == 0x83;
            }
        };

        private final Supplier<ControlCmd<?>> supplier;

        @SuppressWarnings("all")
        ControlCmd<?> decode(String code) {
            return supplier.get().decode(code);
        }

        abstract boolean match(int code);

        public static Optional<ControlCmdType> of(int code) {
            for (ControlCmdType value : values()) {
                if (value.match(code)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }

        public static Optional<ControlCmdType> of(String hex) {
            char[] ptzCmdArr = hex.toCharArray();
            return of(Integer.parseInt(new String(ptzCmdArr, 6, 2), 16));
        }

    }


}
