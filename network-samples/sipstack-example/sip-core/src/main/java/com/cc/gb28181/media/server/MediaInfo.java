package com.cc.gb28181.media.server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@Setter
public class MediaInfo {

    private static final long serialVersionUID = 1L;

    @Schema(description = "rtsp播放地址")
    private String rtsp;

    @Schema(description = "rtmp播放地址")
    private String rtmp;

    @Schema(description = "flv播放地址")
    private String flv;

    @Schema(description = "mp4播放地址")
    private String mp4;

    @Schema(description = "hls播放地址")
    private String hls;

    @Schema(description = "rtc播放地址")
    private String rtc;

    public List<URI> toStreamUrls() {
        List<URI> uris = new ArrayList<>();

        toUri(rtsp).ifPresent(uris::add);
        toUri(rtmp).ifPresent(uris::add);
        toUri(rtc).ifPresent(uris::add);
        toUri(mp4).ifPresent(uris::add);
        toUri(flv).ifPresent(uris::add);

        return uris;
    }

    private Optional<URI> toUri(String uri) {
        if (StringUtils.hasText(uri)) {
            return Optional.of(URI.create(uri));
        }
        return Optional.empty();
    }

    public void setStreamUrl(URI url) {
        String urlString = String.valueOf(url);
        switch (url.getScheme().toLowerCase()) {
            case "file":
            case "http":
            case "ftp":
            case "https": {
                String path = url.getPath();
                String extension = FilenameUtils.getExtension(path);
                switch (extension.toLowerCase(Locale.ROOT)) {
                    case "flv":
                        setFlv(urlString);
                        break;
                    case "mp4":
                        setMp4(urlString);
                        break;
                    case "m3u8":
                        setHls(urlString);
                        break;
                }
                break;
            }
            case "rtsps":
            case "rtsp":
                setRtsp(urlString);
                break;
            case "rtmps":
            case "rtmp":
                setRtmp(urlString);
                break;
            case "rtc":
            case "webrtc":
                setRtc(urlString);
                break;
        }
    }

    public void appendUrlParameter(Map<String, Object> param) {
        String paramString = createEncodedUrlParams(param);
        appendUrlParameter(flv, paramString, this::setFlv);
        appendUrlParameter(mp4, paramString, this::setMp4);
        appendUrlParameter(hls, paramString, this::setHls);
        appendUrlParameter(rtmp, paramString, this::setRtmp);
        appendUrlParameter(rtsp, paramString, this::setRtsp);
        appendUrlParameter(rtc, paramString, this::setRtc);
    }

    static final URLCodec urlCodec = new URLCodec();

    @SneakyThrows
    public static String urlEncode(String url) {
        return urlCodec.encode(url);
    }

    public static String createEncodedUrlParams(Map<?, ?> maps) {
        return maps.entrySet()
                .stream()
                .map(e -> urlEncode(String.valueOf(e.getKey())) + "=" + urlEncode(String.valueOf(e.getValue())))
                .collect(Collectors.joining("&"));

    }

    private void appendUrlParameter(String url, String param, Consumer<String> consumer) {
        if (StringUtils.isEmpty(url)) {
            return;
        }
        if (url.contains("?")) {
            consumer.accept(url + "&" + param);
        } else {
            consumer.accept(url + "?" + param);
        }
    }

    public static int getDefaultPort(String protocol) {
        switch (protocol) {
            case "http":
                return 80;
            case "https":
                return 443;
            case "rtsp":
                return 554;
            case "rtmp":
                return 1935;
            default:
                return -1;
        }
    }
}
