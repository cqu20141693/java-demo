package com.wcc.scada.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wcc.scada.core.domian.dto.FolderAddReq;
import com.wcc.scada.core.domian.vo.FolderVo;
import com.wcc.scada.core.entity.Folder;

/**
 * wcc 2022/7/27
 */
public interface FolderService extends IService<Folder> {

    Boolean save(FolderAddReq addReq);

    FolderVo getFolder();
}
