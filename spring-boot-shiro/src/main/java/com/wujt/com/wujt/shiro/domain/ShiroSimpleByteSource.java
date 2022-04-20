package com.wujt.com.wujt.shiro.domain;

import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

/**
 * shiro认证缓存序列化
 * 解决 SimpleByteSource 没有实现 Serializable 接口的问题
 * <p>
 * clx
 */
public class ShiroSimpleByteSource extends SimpleByteSource implements Serializable {

    public ShiroSimpleByteSource(byte[] bytes) {
        super(bytes);
    }

    public ShiroSimpleByteSource(char[] chars) {
        super(chars);
    }

    public ShiroSimpleByteSource(String string) {
        super(string);
    }

    public ShiroSimpleByteSource(ByteSource source) {
        super(source);
    }

    public ShiroSimpleByteSource(File file) {
        super(file);
    }

    public ShiroSimpleByteSource(InputStream stream) {
        super(stream);
    }
}
