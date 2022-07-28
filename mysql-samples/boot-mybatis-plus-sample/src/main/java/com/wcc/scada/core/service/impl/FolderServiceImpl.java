package com.wcc.scada.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcc.scada.core.domian.dto.FolderAddReq;
import com.wcc.scada.core.domian.vo.FolderVo;
import com.wcc.scada.core.entity.Folder;
import com.wcc.scada.core.mapper.FolderMapper;
import com.wcc.scada.core.service.FolderService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gow.spring.session.SessionHolder.getUserId;

/**
 * wcc 2022/7/27
 */
@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, Folder> implements FolderService {
    @Override
    public Boolean save(FolderAddReq addReq) {

        Folder entity = addReq.toFolder();
        boolean save = save(entity);
        return save;
    }

    @Override
    public FolderVo getFolder() {
        QueryWrapper<Folder> wrapper = new QueryWrapper<>();

        wrapper.eq("user_id", getUserId());
        List<Folder> list = list(wrapper);

        return FolderVo.parse(list);
    }


}
