package com.cc.core.search;

import lombok.Data;

@Data
public class BaseSearchDTO {
    /**
     * 页码
     */
    protected Integer page = 1;
    /**
     * 每页条数
     */
    protected Integer pageSize = 10;
}
