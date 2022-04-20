package com.wujt;

import com.gow.common.Result;
import com.wujt.model.SendCmdRequest;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author gow
 * @date 2021/7/12
 */
@RequestMapping("/api/device/operation")
@FeignClient(name = "device-operation",url = "localhost:10093")
public interface CommandOperationClient {
    @RequestMapping(value = "/sendCmd", method = RequestMethod.POST)
    Result<String> sendCmd(@Valid @RequestBody SendCmdRequest sendCmdRequest);

}
