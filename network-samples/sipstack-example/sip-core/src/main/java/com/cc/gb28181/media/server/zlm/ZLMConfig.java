package com.cc.gb28181.media.server.zlm;

import com.alibaba.fastjson.JSONObject;
import com.cc.gb28181.media.MediaConfig;
import com.cc.gb28181.media.server.MediaFormat;
import com.cc.gb28181.media.server.MediaInfo;
import com.cc.gb28181.media.server.ServerMediaRecordFile;
import com.gow.exception.CommonException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.Pattern;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class ZLMConfig extends MediaConfig {

    private String type = "zlmedia";

    private String mediaServerId;
    private String mediaConfigId;
    private String mediaGatewayId;

    private String networkId;

    // java -Dzlmedia.live.flv.suffix=.flv
    private static final String liveFlvSuffix = System.getProperty("zlmedia.live.flv.suffix", ".flv");

    // java -Dzlmedia.live.mp4.suffix=.live.mp4
    private static final String liveMp4Suffix = System.getProperty("zlmedia.live.mp4.suffix", ".live.mp4");

    @Schema(description = "播放器地址,ZLMedia http端口对应的公网地址")
    private String publicHost;

    @Schema(description = "API地址,ZLMedia的内网地址")
    private String apiHost;

    @Schema(description = "API端口")
    private int apiPort;

    @Schema(description = "ZLMedia暴露RTP端口的公网IP地址")
    @Pattern(regexp = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$")
    private String rtpIp;

    @Schema(description = "RTP端口")
    private int rtpPort = 10000;

    //是否开启动态RTP端口
    @Schema(description = "是否开启动态RTP端口")
    private boolean dynamicRtpPort = false;

    //动态RTP端口范围
    @Schema(description = "动态RTP端口范围")
    private int[] dynamicRtpPortRange = new int[]{10001, 10101};

    @Deprecated
    private int httpPort = 80;

    @Schema(description = "rtmp端口")
    @Deprecated
    private int rtmpPort = 1935;

    @Schema(description = "rtsp端口")
    @Deprecated
    private int rtspPort = 554;

    @Schema(description = "WebRTC端口")
    @Deprecated
    private int rtcPort = 1935;

    private String app = "live";

    private String vhost = "__defaultVhost__";

    private String secret = "";

    private String streamIdPrefix = "";

    private List<MediaPlayerConfig> playerConfig;

    @Schema(description = "全部支持的格式")
    private List<MediaFormat> formats = new ArrayList<>(Arrays.asList(MediaFormat.values()));

    public String getRtpIp() {
        if (StringUtils.isEmpty(rtpIp)) {
            return publicHost;
        }
        return rtpIp;
    }

    public Optional<MediaPlayerConfig> getPlayerConfig(MediaFormat mediaFormat) {

        return getPlayerConfig()
                .stream()
                .filter(conf -> mediaFormat == conf.format)
                .findAny();
    }

    public List<MediaPlayerConfig> getPlayerConfigs(MediaFormat... mediaFormat) {
        Set<MediaFormat> formats = new HashSet<>(Arrays.asList(mediaFormat));
        return getPlayerConfig()
                .stream()
                .filter(conf -> formats.contains(conf.format))
                .collect(Collectors.toList());
    }

    public List<MediaPlayerConfig> getPlayerConfig() {
        if (playerConfig == null) {
            playerConfig = new ArrayList<>();
            for (MediaFormat format : formats) {
                if (format == null) {
                    continue;
                }
                MediaPlayerConfig config = new MediaPlayerConfig();
                config.enabled = true;
                config.format = format;
                switch (format) {
                    case rtc:
                        config.port = rtcPort;
                        break;
                    case rtsp:
                        config.port = rtspPort;
                        break;
                    case mp4:
                    case flv:
                    case hls:
                        config.port = httpPort;
                        config.localPort = apiPort;
                        break;
                    case rtmp:
                        config.port = rtmpPort;
                        break;
                }
                playerConfig.add(config);
            }
        }
        return playerConfig;
    }

    public String createPlayerAddress(MediaPlayerConfig config, boolean localPlayer) {
        return createSchemaHost(config.schema(localPlayer),
                (localPlayer ? apiHost : publicHost) + ":" +
                        config.port(localPlayer, this::getDefaultLocalPort));
    }

    public void acceptPlayerAddress(String app, String streamId, MediaInfo streamInfo, boolean localPlayer) {

        for (MediaPlayerConfig config : getPlayerConfig()) {
            if (config.isEnabled()) {

                // schema://host:port/app/streamId
                String address = createPlayerAddress(config, localPlayer) + app + "/" + streamId;

                config.getFormat().getAddressSetter().accept(streamInfo, address + config.suffix());
            }
        }
        //如果没有设置rtmp,则默认添加本地rtmp地址
        //场景: 在级联代理视频流时，使用本地rtmp地址速度更快.
        if (StringUtils.isEmpty(streamInfo.getRtmp())) {
            streamInfo.setRtmp("rtmp://127.0.0.1/" + app + "/" + streamId);
        }
    }

    public void acceptRecordPlayerAddress(String app,
                                          String date,
                                          String time,
                                          ServerMediaRecordFile mediaInfo,
                                          boolean localPlayer) {
        app = app == null ? this.app : app;
        acceptRecordPlayerAddress(createRecordFilePath(app, mediaInfo.getStreamId(), date, time), mediaInfo, localPlayer);
    }

    public void acceptRecordPlayerAddress(String path, MediaInfo mediaInfo, boolean localPlayer) {
        for (MediaPlayerConfig config : getPlayerConfigs(MediaFormat.rtsp, MediaFormat.rtmp, MediaFormat.mp4)) {
            if (config.isEnabled()) {

                String address = createPlayerAddress(config, localPlayer) + path;
                if (!address.endsWith(".mp4")) {
                    address = address + ".mp4";
                }
                config.getFormat().getAddressSetter().accept(mediaInfo, address);
            }
        }
        //如果没有设置rtmp,则默认添加本地rtmp地址
        //场景: 在级联代理视频流时，使用本地rtmp地址速度更快.
        if (StringUtils.isEmpty(mediaInfo.getRtmp())) {
            mediaInfo.setRtmp("rtmp://127.0.0.1/" + path);
        }
    }


    public int getDefaultLocalPort(MediaFormat format) {
        switch (format) {
            case flv:
            case hls:
            case mp4:
                return apiPort;
            case rtc:
                return rtcPort;
            case rtmp:
                return rtmpPort;
            case rtsp:
                return rtspPort;
        }
        return httpPort;
    }

    public String createRecordFilePath(String app, String streamId, String date, String time) {
        return String.join("/", "record", app, streamId, date, time);
    }

    private String createSchemaHost(String schema, String host) {
        if (!host.startsWith(schema)) {
            host = schema + "://" + host;
        }
        if (!host.endsWith("/")) {
            return host + "/";
        }
        return host;
    }

    @SneakyThrows
    public JSONObject apiRequest(RestTemplate restClient,
                                 String api,
                                 Map<String, String> params) {
        api = (api.startsWith("/") ? api : "/" + api);
        String fApi = "/index/api" + api;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        uriBuilder.scheme("http")
                .host(apiHost)
                .port(apiPort)
                .path(fApi)
                .queryParam("secret", secret)
                .queryParam("vhost", vhost);
        if (!params.containsKey("app")) {
            uriBuilder.queryParam("app", app);
        }
        params.forEach(uriBuilder::queryParam);

        UriComponents url = uriBuilder.build();
        ResponseEntity<JSONObject> response = restClient.exchange(url.toUri(), HttpMethod.GET, null, JSONObject.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CommonException("流媒体服务请求失败[" + fApi + "]:" + response.getStatusCode());
        }
        JSONObject body = response.getBody();
        if (body.getIntValue("code") != 0) {
            throw new CommonException("流媒体服务请求失败[" + fApi + "]:" + body.getString("msg"));
        }

        return body;

    }

    @Getter
    @Setter
    public static class MediaPlayerConfig {
        @Schema(description = "是否开启")
        private boolean enabled;

        @Schema(description = "流媒体格式")
        private MediaFormat format;

        @Schema(description = "公网端口")
        private int port;

        @Schema(description = "内网端口")
        private int localPort;

        @Schema(description = " 是否开启TLS")
        private boolean tls;

        public int port(boolean localPlayer, Function<MediaFormat, Integer> defaultLocalPort) {
            if (localPlayer) {
                if (localPort <= 0) {
                    return defaultLocalPort.apply(format);
                }
                return localPort;
            }
            return port;
        }

        public String schema(boolean localPlayer) {
            if (localPlayer) {
                return format.getSchema();
            }
            return tls ? format.getTlsSchema() : format.getSchema();
        }

        public String suffix() {
            switch (format) {
                case hls:
                    return "/hls.m3u8";
                case flv:
                    return liveFlvSuffix;
                case mp4:
                    return liveMp4Suffix;
                default:
                    return "";
            }
        }
    }
}
