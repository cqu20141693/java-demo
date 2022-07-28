package com.wcc.scada.core.domian.vo;

import com.gow.spring.session.SessionHolder;
import com.wcc.scada.core.entity.Folder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹VO
 * wcc 2022/7/27
 */
@Data
public class FolderVo {
    private List<String> topology = new ArrayList<>();
    private List<String> system = new ArrayList<>();
    private List<String> user = new ArrayList<>();
    private Long userId;

    public static FolderVo parse(List<Folder> list) {
        FolderVo folderVo = new FolderVo();
        folderVo.setUserId(SessionHolder.getUserId());
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(folder -> {
                if ("topology".equals(folder.getType())) {
                    folderVo.getTopology().add(folder.getName());
                }
                if ("system".equals(folder.getType())) {
                    folderVo.getSystem().add(folder.getName());
                }
                if ("user".equals(folder.getType())) {
                    folderVo.getUser().add(folder.getName());
                }
            });
        }
        return folderVo;
    }
}
