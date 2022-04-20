package com.wujt.quartz.dao;

import com.wujt.quartz.domain.JobAndTrigger;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wujt
 */
@Mapper
public interface JobAndTriggerMapper {
    List<JobAndTrigger> getJobAndTriggerDetails();
}