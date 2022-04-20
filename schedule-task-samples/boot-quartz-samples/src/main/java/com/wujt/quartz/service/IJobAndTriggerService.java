package com.wujt.quartz.service;

import com.github.pagehelper.PageInfo;
import com.wujt.quartz.domain.JobAndTrigger;

/**
 * @author wujt
 */
public interface IJobAndTriggerService {
    PageInfo<JobAndTrigger> queryJob(int pageNum, int pageSize);
}