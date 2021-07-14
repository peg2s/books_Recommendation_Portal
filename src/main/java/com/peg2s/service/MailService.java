package com.peg2s.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;


    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.mail.ru");
        mailSender.setPort(465);

        mailSender.setUsername("noreply.recommendations@mail.ru");
        mailSender.setPassword("1q2w3e!!!");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    public MailService() {
    }

    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setFrom("noreply.recommendations@mail.ru");
        emailMessage.setTo(emailTo);
        emailMessage.setSubject(subject);
        emailMessage.setText(message);
        mailSender.send(emailMessage);
    }
}
