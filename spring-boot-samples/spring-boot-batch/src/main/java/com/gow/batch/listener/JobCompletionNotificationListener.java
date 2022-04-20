package com.gow.batch.listener;

import com.gow.batch.gen.dao.PeopleMapper;
import com.gow.batch.gen.model.People;
import com.gow.batch.gen.model.PeopleExample;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/9/8
 */
@Component
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    @Autowired
    private PeopleMapper peopleMapper;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            PeopleExample example = new PeopleExample();
            example.createCriteria().andIdGreaterThan(0L);
            List<People> peopleList = peopleMapper.selectByExample(example);
            peopleList.forEach(person -> log.info("Found <" + person + "> in the database."));
        }
    }
}
