package com.cc.gb28181.message;

import com.cc.gb28181.XmlUtils;
import com.cc.gb28181.media.MediaRecordInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

@Getter
@Setter
public class RecordFile {
    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    @JacksonXmlProperty(localName = "Name")
    private String name;

    @JacksonXmlProperty(localName = "FilePath")
    private String filePath;

    @JacksonXmlProperty(localName = "Address")
    private String address;

    @JacksonXmlProperty(localName = "StartTime")
    private String startTime;

    @JacksonXmlProperty(localName = "EndTime")
    private String endTime;

    @JacksonXmlProperty(localName = "Secrecy")
    private String secrecy;

    @JacksonXmlProperty(localName = "Type")
    private String type;

    @JacksonXmlProperty(localName = "RecorderID")
    private String recorderId;

    @JacksonXmlProperty(localName = "FileSize")
    private String fileSize;

    public MediaRecordInfo toMediaRecordInfo() {
        MediaRecordInfo info = new MediaRecordInfo();
        info.setName(name);
        info.setChannelId(deviceId);
        info.setFilePath(filePath);
        info.setStartTime(parseDate(startTime).toInstant(ZoneOffset.of("+8")).toEpochMilli());
        info.setEndTime(parseDate(endTime).toInstant(ZoneOffset.of("+8")).toEpochMilli());
        info.setType(type);
        info.setRecorderId(recorderId);
        info.setSecrecy(secrecy);
        if (StringUtils.hasText(fileSize)) {
            info.setFileSize(Long.parseLong(fileSize));
        }
        return info;
    }

    private LocalDateTime parseDate(String startTime) {
        return LocalDateTime.now();
    }

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public String toXml() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("<Item>");

        joiner.add("<DeviceID>" + deviceId + "</DeviceID>");
        joiner.add("<Name>" + XmlUtils.safeString(name) + "</Name>");
        joiner.add("<StartTime>" + dateTimeFormatter.format(parseDate(startTime)) + "</StartTime>");
        joiner.add("<EndTime>" + dateTimeFormatter.format(parseDate(endTime)) + "</EndTime>");
        joiner.add("<FilePath>" + XmlUtils.safeString(filePath) + "</FilePath>");
        joiner.add("<Address>" + XmlUtils.safeString(address) + "</Address>");
        joiner.add("<Secrecy>" + XmlUtils.safeString(secrecy) + "</Secrecy>");
        joiner.add("<FileSize>" + XmlUtils.safeString(fileSize) + "</FileSize>");
        joiner.add("<Type>" + XmlUtils.safeString(type) + "</Type>");
        joiner.add("<RecorderID>" + XmlUtils.safeString(recorderId) + "</RecorderID>");

        joiner.add("</Item>");

        return joiner.toString();
    }
}
