package com.example.notificationService.email;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;

@Service
public class EmailService {

    private static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Async
    public void sendMail(String email, String subject, List<Object> emailParameters, String template) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String html = "";
            if (template.equals("thank-you")) {
                Context contextOrder = setContextOrder(emailParameters);
                html = templateEngine.process(template, contextOrder);
            } else if (template.equals("forgot-password")) {
                Context contextForgotPassword = setContextForgotPassword(emailParameters);
                html = templateEngine.process(template, contextForgotPassword);
            }

            helper.setFrom(fromMail);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(html, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException | jakarta.mail.MessagingException e) {
            LOGGER.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }

    private Context setContextOrder(List<Object> emailParameters){
        Context context = new Context();
        context.setVariable("userName", emailParameters.get(0));
        context.setVariable("total", emailParameters.get(1));
        return context;
    }

    private Context setContextForgotPassword(List<Object> emailParameters){
        Context context = new Context();
        context.setVariable("userName", emailParameters.get(0));
        context.setVariable("email", emailParameters.get(1));
        context.setVariable("linkReset", "http://localhost:3000/reset-password?secretKey=" + emailParameters.get(2));
        return context;
    }
}
