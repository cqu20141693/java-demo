package com.wcc.scada.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcc.scada.core.entity.Folder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件夹表映射
 * wcc 2022/7/27
 */
@Mapper
public interface FolderMapper extends BaseMapper<Folder> {
}
