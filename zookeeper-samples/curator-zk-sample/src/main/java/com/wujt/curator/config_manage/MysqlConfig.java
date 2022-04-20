package com.wujt.curator.config_manage;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wujt
 */
@AllArgsConstructor
@Data
public class MysqlConfig {
    private String url;
    private String driver;
    private String username;
    private String password;
}