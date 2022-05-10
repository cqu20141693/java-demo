package com.cc.flowable.API;

import com.cc.flowable.API.vo.HolidayReq;
import com.cc.flowable.component.FlowableService;
import com.gow.common.Result;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * rest api
 * wcc 2022/5/10
 */
@RestController
@RequestMapping("flowable")
public class FlowableApi {

    @Autowired
    private FlowableService flowableService;

    @PostMapping("holiday")
    public Result<String> holiday(@RequestBody HolidayReq req) {
        ProcessInstance instance = flowableService.holiday(req);
        flowableService.processTask();
        flowableService.history(instance);
        return Result.ok("success");
    }
}
