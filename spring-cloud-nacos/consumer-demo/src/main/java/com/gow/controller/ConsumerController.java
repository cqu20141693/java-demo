package com.gow.controller;

import com.gow.codec.model.EncodeTypeEnum;
import com.gow.common.Result;
import com.wujt.CommandOperationClient;
import com.wujt.TestClient;
import com.wujt.model.SendCmdRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujt
 */
@RestController
@RequestMapping("consumer")
public class ConsumerController {

    @Autowired
    private TestClient testClient;

    @Autowired
    private CommandOperationClient commandOperationClient;

    @RequestMapping("")
    public Result<String> testFeginURIEncode(@RequestParam("value") String value) {
        System.out.println("param=" + value);
        String info = testClient.getInfo(value);
        System.out.println("result=" + info);
        return Result.ok(info);
    }

    @PostMapping("/sendCmd")
    Result<String> sendCmd(@Valid @RequestBody SendCmdRequest sendCmdRequest) {
        commandOperationClient.sendCmd(sendCmdRequest);
        commandOperationClient.sendCmd(getRequest(sendCmdRequest, "int", "1"));
        commandOperationClient.sendCmd(getRequest(sendCmdRequest, "long", "10"));
        commandOperationClient.sendCmd(getRequest(sendCmdRequest, "float", "1.0"));
        commandOperationClient.sendCmd(getRequest(sendCmdRequest, "double", "1.01"));
        commandOperationClient.sendCmd(getRequest(sendCmdRequest, "string", "string"));
        commandOperationClient.sendCmd(getRequest(sendCmdRequest, "json",
                "{\"groupKey\":\"tPH6EZy6UbIxHxkg\",\"sn\":\"dVwEOrHihSRHtZTm\",\"status\":\"complete\","
                        + "\"time\":1635478111090}"));
        commandOperationClient.sendCmd(getRequest(sendCmdRequest,
                "bin", new String(Base64.getEncoder().encode("gow".getBytes(StandardCharsets.UTF_8)))));
        return Result.ok("success");
    }

    private SendCmdRequest getRequest(SendCmdRequest sendCmdRequest, String type, String value) {
        SendCmdRequest request = new SendCmdRequest();
        BeanUtils.copyProperties(sendCmdRequest, request);
        request.setType(type);
        request.setContent(value);
        return request;
    }

    @PostMapping("/sendCmd1")
    Result<String> sendCmd1(@Valid @RequestBody SendCmdRequest sendCmdRequest) {
        if ("bin".equals(sendCmdRequest.getType())) {
            EncodeTypeEnum encodeTypeEnum = EncodeTypeEnum.parseFromType(sendCmdRequest.getType());
            Object convert = encodeTypeEnum.getTypeConvert().strDataConvert((String) sendCmdRequest.getContent());
            sendCmdRequest.setContent(convert);
        }
        return commandOperationClient.sendCmd(sendCmdRequest);
    }

    @PostMapping("/sendCmd2")
    Result<String> sendCmd2(@Valid @RequestBody SendCmdRequest sendCmdRequest) {
        if ("bin".equals(sendCmdRequest.getType())) {
            EncodeTypeEnum encodeTypeEnum = EncodeTypeEnum.parseFromType(sendCmdRequest.getType());
            byte[] convert =
                    (byte[]) encodeTypeEnum.getTypeConvert().strDataConvert((String) sendCmdRequest.getContent());
            byte[] bytes = new byte[convert.length];
            for (int i = 0; i < convert.length; i++) {
                bytes[i] = convert[i];
            }
            sendCmdRequest.setContent(bytes);
        }
        return commandOperationClient.sendCmd(sendCmdRequest);
    }
}
