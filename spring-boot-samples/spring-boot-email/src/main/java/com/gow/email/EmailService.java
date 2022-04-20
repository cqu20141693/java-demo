package com.gow.email;

import java.io.File;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/4 0004
 */
@Component
@Slf4j
public class EmailService {
    @Resource
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;


    /**
     * 发送纯文本邮件.
     *
     * @param to      目标email 地址
     * @param subject 邮件主题
     * @param text    纯文本内容
     */
    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    /**
     * 发送html邮件
     *
     * @param to      收件邮箱地址
     * @param subject 主题
     * @param content 内容
     * @throws Exception 异常
     */
    public void sendHtmlMail(String to, String subject, String content) throws Exception {
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        helper.setFrom(from);
        this.javaMailSender.send(message);
    }

    /**
     * 发送邮件并携带附件.
     * 请注意 from 、 to 邮件服务器是否限制邮件大小
     *
     * @param to        目标email 地址
     * @param subject   邮件主题
     * @param text      纯文本内容
     * @param filePaths 附件的路径 当然你可以改写传入文件
     */
    public void sendMailWithAttachment(String to, String subject, String text, String[] filePaths)
            throws MessagingException {


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // use the true flag to indicate you need a multipart message
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        // let's attach the  file
        FileSystemResource file;
        for (String filePath : filePaths) {
            file = new FileSystemResource(new File(filePath));
            String fileName = file.getFilename();
            helper.addAttachment(fileName, file);
        }
        javaMailSender.send(mimeMessage);

    }

    /**
     * 发送富文本邮件.
     *
     * @param to      目标email 地址
     * @param subject 邮件主题
     * @param text    纯文本内容
     * @param rscPath 附件的路径 当然你可以改写传入文件
     */
    public void sendRichMail(String to, String subject, String text, String rscPath, String rscId)
            throws MessagingException {
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            // use the true flag to indicate the text included is HTML
            helper.setText(text, true);
            // 图片占位写法  如果图片链接写入模板 注释下面这一行
            // let's include the Sample file
            helper.addInline(rscId, new FileSystemResource(rscPath));
            javaMailSender.send(mimeMessage);

        } catch (MessagingException ex) {
            log.error("发送图片邮件异常：{}", ex);
        }
    }
}