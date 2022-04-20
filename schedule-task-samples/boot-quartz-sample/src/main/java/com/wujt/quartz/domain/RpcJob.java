package com.wujt.quartz.domain;

import com.wujt.quartz.model.RpcEnum;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * quartz 自支持job: 将执行代码和job 捆绑
 *
 * @author wujt
 */
@Slf4j
public class RpcJob extends AbstractJob {
    /**
     * rpc 类型
     */
    private RpcEnum rpcType = RpcEnum.http;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        switch (rpcType) {
            case grpc:
                log.info("grpc is not support at present");
                break;
            case dubbo:
                log.info("dubbo is not support at present");
                break;
            case http:


        }
    }
}
