package com.gow.scada.gen.controller;

import com.gow.common.Result;
import com.gow.scada.gen.dao.FolderMapper;
import com.gow.scada.gen.model.Folder;
import com.gow.scada.gen.model.FolderExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 文件夹控制器
 * wcc 2022/7/26
 */
@RestController
@RequestMapping("api/user")
public class FolderController {

    @Autowired
    private FolderMapper folderMapper;

    @GetMapping("/folder")
    public Result<List<Folder>> getUserFolder(@RequestParam("userId") Long userId) {
        FolderExample example = new FolderExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return Result.ok(folderMapper.selectByExample(example));
    }
}
