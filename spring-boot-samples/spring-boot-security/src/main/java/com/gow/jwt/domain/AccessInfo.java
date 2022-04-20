package com.gow.jwt.domain;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gow
 * @date 2021/8/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessInfo {
    /**
     * 用户唯一标识
     */
    private String userKey;
    /**
     * 用户扩展信息
     */
    private String extend;
    private PlatformEnum platform;
    /**
     * 最大有效期的时间点
     */
    private Date beforeDate;

}
