package com.gow.codec.encode.batch;

import com.gow.codec.exception.EncodeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author gow
 * @date 2021/7/29
 */
public class BatchEncodeContext {
    private int totalSize = 0;
    private int index = 0;
    private byte[] totalControl;
    private byte[] totalBizTime = null;
    private final List<StreamLoop> streamLoops = new ArrayList<>();

    public byte[] encode() {
        byte[] bytes = new byte[totalSize];
        System.arraycopy(totalControl, 0, bytes, index, totalControl.length);
        index += totalControl.length;
        if (totalBizTime != null) {
            System.arraycopy(totalBizTime, 0, bytes, index, totalBizTime.length);
            index += totalBizTime.length;
        }
        streamLoops.forEach(streamLoop -> {
            bytes[index++] = streamLoop.getControl();
            if (streamLoop.getBizTime() != null) {
                System.arraycopy(streamLoop.getBizTime(), 0, bytes, index, streamLoop.getBizTime().length);
                index += streamLoop.getBizTime().length;
            }

            bytes[index++] = (byte) streamLoop.getStreamBytes().length;
            System.arraycopy(streamLoop.getStreamBytes(), 0, bytes, index, streamLoop.getStreamBytes().length);
            index += streamLoop.getStreamBytes().length;

            System.arraycopy(streamLoop.getLengthBytes(), 0, bytes, index, streamLoop.getLengthBytes().length);
            index += streamLoop.getLengthBytes().length;
            System.arraycopy(streamLoop.getData(), 0, bytes, index, streamLoop.getData().length);
            index += streamLoop.getData().length;
        });
        if (index != totalSize) {
            throw new EncodeException("encode failed,index=" + index + " totalSize=" + totalSize);
        }
        index = 0;
        return bytes;
    }

    public void setTotalControl(byte[] totalControl) {
        totalSize += totalControl.length;
        this.totalControl = totalControl;
    }

    public void setTotalBizTime(byte[] totalBizTime) {
        totalSize += totalBizTime.length;
        this.totalBizTime = totalBizTime;
    }

    public void addStreamLoop(StreamLoop streamLoop) {
        totalSize += 1;
        Optional.ofNullable(streamLoop.getBizTime()).ifPresent(bytes -> totalSize += bytes.length);
        totalSize += 1;
        if (streamLoop.getStreamBytes().length > 127) {
            throw new EncodeException(
                    "stream=" + new String(streamLoop.getStreamBytes()) + " must be less than 128");
        }
        totalSize += streamLoop.getStreamBytes().length;
        totalSize += streamLoop.getLengthBytes().length;
        totalSize += streamLoop.getData().length;
        streamLoops.add(streamLoop);
    }
}
