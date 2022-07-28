package com.wcc.scada.api;

import com.gow.common.Result;
import com.wcc.scada.core.entity.Command;
import com.wcc.scada.core.service.CommandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wcc
 * @since 2022-07-27
 */
@RestController
@RequestMapping("/command")
@Api(tags = "command api")
public class CommandController {

    @Autowired
    private CommandService commandService;

    @ApiOperation("getByCmdTag")
    @GetMapping("getByCmdTag")
    public Result<List<Command>> getByCmdTag(@RequestParam("cmdTag") String cmdTag) {
        return Result.ok(commandService.query().eq("cmd_tag", cmdTag).list());
    }

    @ApiOperation("saveCommand")
    @PostMapping("saveCommand")
    public Result<Boolean> saveCommand(@RequestBody Command command) {
        return Result.ok(commandService.save(command));
    }

}
