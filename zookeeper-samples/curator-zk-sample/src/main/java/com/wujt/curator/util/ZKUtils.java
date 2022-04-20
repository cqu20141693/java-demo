package com.wujt.curator.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author wujt
 */
public class ZKUtils {

    public static CuratorFramework getClient() {

        CuratorFramework curatorFramework = CuratorFrameworkFactory.
                builder().connectString("10.233.120.148:2181").
                sessionTimeoutMs(2000).retryPolicy(new
                ExponentialBackoffRetry(1000, 3)).
                namespace("").build();
        return curatorFramework;
    }
}
