package com.gow.camera.api.lecheng.model.resp;

import java.util.List;
import lombok.Data;

/**
 * @author gow
 * @date 2021/8/12
 */
@Data
public class DeviceLiveInfo {
    private Integer count;
    private List<DeviceLive> lives;

}
