package com.wcc.scada.core.domian.dto;

import com.gow.spring.session.SessionHolder;
import com.wcc.scada.core.entity.Folder;
import lombok.Data;

/**
 * wcc 2022/7/27
 */
@Data
public class FolderAddReq {
    private String type;
    private String name;

    public Folder toFolder() {
        Folder folder = new Folder();
        folder.setName(name);
        folder.setType(type);
        folder.setUserId(SessionHolder.getUserId());
        return folder;
    }
}
