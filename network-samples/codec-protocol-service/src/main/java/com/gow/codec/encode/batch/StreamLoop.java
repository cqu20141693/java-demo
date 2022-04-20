package com.gow.codec.encode.batch;

import lombok.Data;

/**
 * @author gow
 * @date 2021/7/29
 */
@Data
public class StreamLoop {
    private byte control;
    private byte[] bizTime = null;
    private byte[] streamBytes;
    private byte[] lengthBytes;
    private byte[] data;
}
