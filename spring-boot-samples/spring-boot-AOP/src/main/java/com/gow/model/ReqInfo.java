package com.gow.model;

import java.util.Set;
import lombok.Data;

/**
 * @author gow
 * @date 2022/1/18
 */
@Data
public class ReqInfo {
    private Set<String> roles;
    private Long time;
}
