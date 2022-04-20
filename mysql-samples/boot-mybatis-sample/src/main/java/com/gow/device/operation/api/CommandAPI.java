package com.gow.device.operation.api;

import com.gow.common.Result;
import com.gow.device.operation.gen.dao.CommandMapper;
import com.gow.device.operation.gen.model.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gow
 * @date 2021/7/14 0014
 */
@RestController
@RequestMapping("/device/operation")
public class CommandAPI {

    @Autowired
    private CommandMapper commandMapper;

    @GetMapping("getCmd")
    Result<Command> getCmd(@RequestParam("index") Integer index) {
        Command command = commandMapper.selectByPrimaryKey(index);
        return Result.ok(command);
    }

}
