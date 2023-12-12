package com.softuni.angelovestates.util;


//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@EnableAsync
@Configuration
@Service
public class EmailSender {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendRegistrationSuccessEmail(String email, String firstName) {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

            try {
                message.setTo(email);
                message.setSubject("Registration successful!");
                String welcomeMessage = "Welcome, %s!\n\n" +
                                "Your registration was successful! Thank you for using AngelovEstates!\n" +
                                "Best wishes!";
                message.setText(String.format(welcomeMessage, firstName));
                javaMailSender.send(message.getMimeMessage());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

    }
}
