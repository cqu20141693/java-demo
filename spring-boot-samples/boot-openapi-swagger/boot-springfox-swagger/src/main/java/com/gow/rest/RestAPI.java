package com.gow.rest;

import com.gow.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gow
 * @date 2021/7/12 0012
 */
@Api(tags = "REST API ")
@RestController
@RequestMapping("rest/")
@Slf4j
public class RestAPI {

    @ApiOperation(value = "addRequest", notes = "创建用户请求")
    @PostMapping("createUser")
    public Result<AddUserRequest> createUser(@ApiParam(value = "添加用户请求", required = true)
                                             @RequestBody AddUserRequest addUserRequest) {
        log.info("request={}", addUserRequest);
        return Result.ok(addUserRequest);
    }
}