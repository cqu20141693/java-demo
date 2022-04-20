package com.gow.camera.api.lecheng.model.resp;

import lombok.Data;

/**
 * @author gow
 * @date 2021/8/12
 */
@Data
public class FrameReverseStatus {
    //设备翻转状态，normal：正常，reverse：翻转
    private String direction;
}
