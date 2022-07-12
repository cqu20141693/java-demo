package com.wujt;

import com.wujt.rabbimq.Sender;
import com.wujt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/23
 */
@SpringBootApplication
@EnableScheduling
public class AdminDemo implements CommandLineRunner {

    @Autowired
    private Sender sender;

    public static void main(String[] args) {
        SpringApplication.run(AdminDemo.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        sendMsg();
    }

    @Scheduled(fixedDelay = 5000, initialDelay = 1000)
    public void schedule() {
        sendMsg();
    }

    private void sendMsg() {
        User user = new User();
        user.setUserName("cc");
        user.setAge(27);
        sender.sendDirect(user);
    }
    @Scheduled(fixedDelay = 10000, initialDelay = 2000)
    public void schedule1() {
        sendTopic();
    }

    private void sendTopic() {
        User user = new User();
        user.setUserName("cc");
        user.setAge(28);
        sender.sendTopic(user);
    }
    @Scheduled(fixedDelay = 15000, initialDelay = 3000)
    public void schedule2() {
        sendFanout();
    }

    private void sendFanout() {
        User user = new User();
        user.setUserName("cc");
        user.setAge(29);
        sender.sendFanout(user);
    }
}
