package cmo.gow.dubbo.spi.impl;

import com.wujt.spi.Protocol;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Exporter;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;

@Slf4j
public class HttpProtocol implements Protocol {
    @Override
    public int getDefaultPort() {
        log.info("invoke getDefaultPort method");
        return 8080;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        return null;
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        return null;
    }

    @Override
    public void destroy() {
        log.info("invoke destory method");
    }
}
