package com.cc.flowable;

import com.alibaba.fastjson.JSONObject;
import com.cc.flowable.component.FlowableService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.cc.flowable.component.CommonProcess.holidayRequest;

/**
 * flowable 测试
 * wcc 2022/5/10
 */
@SpringBootApplication
@Slf4j
public class FlowableApp implements CommandLineRunner {

    @Autowired
    private ProcessEngine processEngine;

    public static void main(String[] args) {
        SpringApplication.run(FlowableApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("start init common process definition");
        // 配置数据库
        // 查询执行引擎
        FlowableService flowableService = new FlowableService(processEngine);
        Deployment deployment = flowableService.classpathProcessDefinition(holidayRequest);
        ProcessDefinition definition = flowableService.restAPI(deployment);
        log.info("add process definition name={},key{},category={},deployId={},id={}",definition.getName(),definition.getKey(),definition.getCategory(), definition.getDeploymentId(),definition.getId());
    }
}
