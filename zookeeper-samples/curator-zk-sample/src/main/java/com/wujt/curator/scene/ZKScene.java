package com.wujt.curator.scene;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author wujt
 */
@Component
public class ZKScene {

    @Autowired
    private CuratorFramework curatorFramework;

    @PostConstruct
    public void init(){
    }
}
