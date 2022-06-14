package com.cc.gb28181.media;


public interface MediaConstants {

    interface DeviceConfigKey {
        //播放密钥配置Key
        String play_key = "play_key";

        //key
        String playKeyParam = "key";
    }

    //云台控制
    interface Ptz {
        String functionId = "PTZ";
        String arg_channelId = "channel";
        String arg_direct = "direct";
        String arg_speed = "speed";

        //多个方向
        String arg_directs = "directs";
    }

    //预置位控制
    interface Preset {
        String functionId = "Preset";
        String arg_channelId = "channel";
        //操作:SET,CALL,DEL
        String arg_operation = "operation";
        //预置位编号
        String arg_presetIndex = "presetIndex";
    }

    //查询预置位
    interface QueryPreset {
        String functionId = "QueryPreset";

    }

    //看守位控制
    interface HomePosition {
        String functionId = "HomePosition";
        String arg_channelId = "channel";
        //是否开启
        String arg_enabled = "enabled";
        //重置时间
        String arg_resetTime = "resetTime";
        //预置位编号
        String arg_presetIndex = "presetIndex";

    }

    //查询录像
    interface QueryRecordInfo {
        String functionId = "QueryRecordList";
        String arg_channelId = "channel";

        String arg_startTime = "startTime";
        String arg_endTime = "endTime";
        String arg_type = "type";
        String arg_local = "local";
        String arg_others = "others";

    }

    //开始录像
    interface StartRecord {
        String functionId = "StartRecord";
        String arg_channelId = "channel";

        String arg_local = "local";
    }

    //停止录像
    interface StopRecord {
        String functionId = "StopRecord";
        String arg_channelId = "channel";
        String arg_local = "local";
        String arg_streamId = "streamId";
    }

    //开始推流指令
    interface StartPushStreaming {
        String functionId = "StartPushStreaming";
        String arg_channelId = "channel";
        String arg_local_player = "localPlayer";

        //历史回放参数
        String arg_startTime = "startTime";
        String arg_endTime = "endTime";
        String arg_download_speed = "downloadSpeed";

    }

    //媒体流操作
    interface StreamOperation {
        String functionId = "StreamOperation";
        String arg_channelId = "channel";
        String arg_streamId = "streamId";
        String arg_action = "action";
        String arg_scale = "scale";
        String arg_range = "range";
    }

    //停止直播指令
    interface StopPushStreaming {
        String functionId = "StopPushStreaming";
        String arg_channelId = "channel";
        String arg_streamId = "streamId";
    }

    //国标订阅告警
    interface SubscribeAlarm {
        String functionId = "SubscribeAlarm";
        String arg_start_alarm_priority = "StartAlarmPriority";
        String arg_end_alarm_priority = "EndAlarmPriority";
        String arg_alarm_method = "AlarmMethod";
        String arg_start_alarm_time = "StartAlarmTime";
        String arg_end_alarm_time = "EndAlarmTime";

    }

    //国标停止级联推流指令
    interface StopCascadePushStreaming {
        String functionId = "StopCascadePushStreaming";
        //级联ID
        String arg_cascadeId = "cascadeId";
        //指令ID
        String arg_callId = "callId";
    }

    //国标级联推流指令
    interface StartCascadePushStreaming {
        String functionId = "StartCascadePushStreaming";
        //通道ID
        String arg_channelId = "channelId";
        //级联ID
        String arg_cascadeId = "cascadeId";
        //推流地址,ip:port
        String arg_address = "address";
        //指令ID
        String arg_callId = "callId";
        //推流ssrc
        String arg_ssrc = "ssrc";
        //流描述
        String arg_mediaDesc = "mediaDescription";
        //输入sdp
        String arg_sdp = "sdp";
        //输出sdp结果
        String output_sdp = "sdp";
    }

    interface DeviceConfig {
        String functionId = "DeviceConfig";
        String deviceId = "deviceId";
        String name = "deviceName";
        String expiration = "expiration";
        String heartBeatInterval = "heartBeatInterval";
        String heartBeatCount = "heartBeatCount";
    }
}
