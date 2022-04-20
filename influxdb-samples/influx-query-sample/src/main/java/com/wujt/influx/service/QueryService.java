package com.wujt.influx.service;

/**
 * @author wujt
 */
public interface QueryService {

    Object query();

    Object pageQuery(Integer page, Integer pageSize);
}
