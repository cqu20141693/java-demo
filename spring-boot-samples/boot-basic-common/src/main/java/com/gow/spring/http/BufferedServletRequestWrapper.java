package com.gow.spring.http;

import com.gow.common.GlobalConstants;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @author wujt  2021/5/24
 */
public class BufferedServletRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] bodyCopier;

    public BufferedServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        bodyCopier = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStreamCopier(bodyCopier);
    }

    public byte[] getCopy() {
        return this.bodyCopier;
    }

    public String getBody() throws UnsupportedEncodingException {
        return new String(this.bodyCopier, GlobalConstants.ENCODE_UTF8);
    }

    private class ServletInputStreamCopier extends ServletInputStream {
        private ByteArrayInputStream bais;

        public ServletInputStreamCopier(byte[] in) {
            this.bais = new ByteArrayInputStream(in);
        }

        @Override
        public boolean isFinished() {
            return bais.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public int read() throws IOException {
            return this.bais.read();
        }
    }
}
