package com.gow;

import com.gow.task.async.AsyncTaskProcessor;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;

/**
 * @author gow
 * @date 2021/7/4 0004
 */
@Slf4j
@SpringBootApplication
public class AsyncApp implements CommandLineRunner {
    @Autowired
    private AsyncTaskProcessor asyncTaskProcessor;

    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private TaskScheduler taskScheduler;

    public static void main(String[] args) {
        SpringApplication.run(AsyncApp.class);
    }

    @Override
    public void run(String... args) throws Exception {
        asyncTaskProcessor.handleData();
        asyncTaskProcessor.handleOther();
        taskExecutor.execute(() -> log.info("taskExecutor bean invoked by manual"));

        taskScheduler.schedule(() -> log.info("taskScheduler bean invoked by manual"),
                new Date(System.currentTimeMillis() + 60 * 1000));
    }
}
