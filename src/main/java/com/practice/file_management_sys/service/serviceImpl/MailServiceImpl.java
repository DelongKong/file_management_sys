package com.practice.file_management_sys.service.serviceImpl;

import com.practice.file_management_sys.service.MailService;
import com.practice.file_management_sys.utils.GenerateVerificationCodeUtils;
import com.practice.file_management_sys.utils.RedisClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service("mailService")
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @Resource
    private RedisClientUtils redisUtils;

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Override
    @Async
    public void sendVerificationCodeEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        String verificationCode = GenerateVerificationCodeUtils.generateCode();
        String content = "dear user, this is your verification code: " + verificationCode + "   " +
                "this code will expire 5 minutes";
        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject("verification");
        message.setText(content);
        mailSender.send(message);
        LOGGER.info("the verification code has been sent to user: " + to );

        redisUtils.set(to, verificationCode);
        redisUtils.expire(to, 60*5);
    }

    @Override
    public void sendWelcomeEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
        LOGGER.info("user: " + to + " registered successfully");
    }

}
