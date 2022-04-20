package com.gow;

import com.gow.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gow
 * @date 2021/7/4 0004
 */
@SpringBootApplication
public class EmailApp implements CommandLineRunner {


    @Autowired
    private EmailService emailService;

    public static void main(String[] args) {
        SpringApplication.run(EmailApp.class);
    }

    @Override
    public void run(String... args) throws Exception {

        String to = "1533181183@qq.com";
        emailService.sendMail(to, "test subject", "gow test 163 send");


        String text = "<html><body><img src='cid:identifier1234'></body></html>";
        String filePath = "c:/Sample.jpg";
        emailService.sendRichMail(to, "test rich main", text, filePath, "identifier1234");
    }
}
