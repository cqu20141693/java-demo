package com.wcc.scada.api;

import com.gow.common.Result;
import com.wcc.scada.core.domian.dto.FolderAddReq;
import com.wcc.scada.core.domian.vo.FolderVo;
import com.wcc.scada.core.service.FolderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文件夹控制器
 * wcc 2022/7/27
 */
@RestController
@RequestMapping("api/user")
@Api(tags = "Folder API")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @GetMapping("/folder")
    @ApiOperation("getFolder")
    public Result<FolderVo> getFolder() {
        return Result.ok((folderService.getFolder()));
    }

    @PostMapping("/folder")
    @ApiOperation("addFolder")
    public Result<Boolean> addFolder(@RequestBody FolderAddReq req) {
        return Result.ok((folderService.save(req)));
    }
}
