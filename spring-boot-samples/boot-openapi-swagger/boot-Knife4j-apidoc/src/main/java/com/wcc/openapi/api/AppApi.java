package com.wcc.openapi.api;

import com.gow.common.Result;
import com.wcc.openapi.domain.app.AppInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * wcc 2022/8/26
 */
@RestController
@RequestMapping("/open/api/app")
@Api(value = "app 模块接口", tags = "app")
public class AppApi {


    @GetMapping("getAppInfo")
    @ApiOperation(value = "获取应用信息")
    public Result<AppInfo> getAppInfo(@RequestParam("app") @ApiParam("appID 唯一标识") String app) {
        return Result.ok(new AppInfo().setAppId(app)
                .setAppSecret(RandomStringUtils.randomAlphanumeric(8)));
    }
}
