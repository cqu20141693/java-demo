package com.cc.gb28181.media;

import com.cc.gb28181.media.server.*;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 媒体服务器
 * wcc 2022/6/3
 */
public interface MediaServer {


    String getId();

    /**
     * @return 获取访问服务的地址
     */
    String getPublicHost();

    default String getRtpIp() {
        return getPublicHost();
    }

    /**
     * 启动服务
     *
     * @return void
     */
    Void start();

    void stop();

    Boolean isAlive();

    /**
     * 根据流ID获取流播放信息
     *
     * @param streamId streamId
     * @return 流信息
     */
    StreamInfo getStreamInfo(String streamId, boolean localPlayer);

    /**
     * @param streamId
     * @return
     */
    default StreamInfo getStreamInfo(String streamId) {
        return getStreamInfo(streamId, false);
    }

    /**
     * 根据RTP的SSRC值获取视频流信息
     *
     * @param streamId streamId
     * @param ssrc     SSRC
     * @return 视频流信息
     */
    default StreamInfo getRtpStreamInfo(String streamId, String ssrc) {
        return getRtpStreamInfo(streamId, ssrc, false);
    }

    StreamInfo getRtpStreamInfo(String streamId, String ssrc, boolean localPlayer);

    StreamInfo getRtpStreamInfo(StreamInfo streamInfo, boolean localPlayer);

    /**
     * 使用streamId,获取一个RTP端口
     *
     * @param streamId streamId
     * @return rtp端口号
     */
    Integer getRtpPort(String streamId);

    /**
     * 停止流
     *
     * @param streamId 流ID
     * @return void
     */
    Void stopStream(String streamId);

    /**
     * 获取全部流信息
     *
     * @return 流信息
     */
    StreamStateInfo getAllStreamInfo();

    default StreamStateInfo getStreamRealInfo(String streamId) {
        throw new UnsupportedOperationException("unsupported startRecord yet");
    }

    StreamStateEvent listen();

    /**
     * 监听流播放事件,流中的元素为流ID
     *
     * @return 事件流
     */
    String listenOnPlay();

    /**
     * 监听流停止播放事件,流中的元素为流ID
     *
     * @return 事件流
     */
    String listenOnStop();

    /**
     * 监听无人观看事件
     *
     * @return 流ID
     */
    String listenOnNoReader();

    /**
     * 通过URL进行转码，将输入流的url转码为输出流url
     * <p>
     * 通过{@link URL#getProtocol()}来指定流媒体类型
     * <p>
     * 通过{@link URL#getQuery()}来指定转码参数
     * <p>
     * 如:
     * <pre>
     *     输入 rtsp://admin:admin@127.0.0.1/h264/ch1/live_stream
     *     输出 rtp://127.0.0.1:7000/storage/stream_01.mp4?transport=udp
     * </pre>
     *
     * @param inputStreamUrl  输入流地址,支持输入多个流,选择合适的流来转换
     * @param outputStreamUrl 输出流地址,为null时不输出流,只拉流
     * @return 返回流信息
     */
    StreamInfo convert(List<URI> inputStreamUrl, @Nullable URI outputStreamUrl, boolean localPlayer);

    default StreamInfo convert(List<URI> inputStreamUrl, @Nullable URI outputStreamUrl) {
        return convert(inputStreamUrl, outputStreamUrl, false);
    }

    /**
     * 停止转码
     *
     * @param streamId 流ID
     * @return void
     */
    Void stopConvert(String streamId);

    /**
     * 获取视频流的录像信息
     *
     * @param streamId 流ID
     * @return 录像信息
     */
    ServerMediaRecordFile getRecordInfo(String streamId, Map<String, Object> args, boolean localPlayer);

    MediaInfo createRecordMedia(String filePath, boolean localPlayer);


    /**
     * 开始录像
     *
     * @param streamId 流ID
     * @return void
     */
    default Map<String, Object> startRecord(String streamId) {
        throw new UnsupportedOperationException("unsupported startRecord yet");
    }

    /**
     * 指定一个媒体信息进行录制
     *
     * @param streamId   流ID
     * @param streamInfo 媒体信息
     * @return 自定义参数信息
     */
    default Map<String, Object> startRecord(String streamId, MediaInfo streamInfo) {
        throw new UnsupportedOperationException("unsupported startRecord yet");
    }

    /**
     * 流是否录制中
     *
     * @param streamId 流ID
     * @return void
     */
    default Boolean isRecording(String streamId, Map<String, Object> args) {
        throw new UnsupportedOperationException("unsupported stopRecord yet");
    }

    /**
     * 停止录像
     *
     * @param streamId 流ID
     * @return void
     */
    default Void stopRecord(String streamId, Map<String, Object> args) {
        throw new UnsupportedOperationException("unsupported stopRecord yet");
    }
}
