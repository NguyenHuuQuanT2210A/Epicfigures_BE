package com.example.notificationService.email;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
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

import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@Service
public class EmailService {
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

            //            if (template.equals("thank-you")) {
//                Context contextOrder = setContextOrder(emailParameters);
//                html = templateEngine.process(template, contextOrder);
//            } else if (template.equals("forgot-password")) {
//                Context contextForgotPassword = setContextForgotPassword(emailParameters);
//                html = templateEngine.process(template, contextForgotPassword);
//            }else if (template.equals("contact")){
//                Context contextContact = setContextContact(emailParameters);
//                html = templateEngine.process(template, contextContact);
//            }

            String html = switch (template) {
                case "thank-you" -> {
                    Context contextOrder = setContextOrder(emailParameters);
                    yield templateEngine.process(template, contextOrder);
                }
                case "forgot-password" -> {
                    Context contextForgotPassword = setContextForgotPassword(emailParameters);
                    yield templateEngine.process(template, contextForgotPassword);
                }
                case "contact" -> {
                    Context contextContact = setContextContact(emailParameters);
                    yield templateEngine.process(template, contextContact);
                }
                case "return-item" -> {
                    Context contextReturnItem = setContextReturnItem(emailParameters);
                    yield templateEngine.process(template, contextReturnItem);
                }
                default -> "";
            };

            // Send attach files
//            if (files != null) {
//                for (MultipartFile file : files) {
//                    helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
//                }
//            }

            helper.setFrom(fromMail, "Epicfigures Shop");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(html, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException | jakarta.mail.MessagingException e) {
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
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
        context.setVariable("linkReset", emailParameters.get(2) + "?secretKey=" + emailParameters.get(3));
        return context;
    }

    private Context setContextContact(List<Object> emailParameters){
        Context context = new Context();
        context.setVariable("userName", emailParameters.get(0));
        context.setVariable("email", emailParameters.get(1));
        context.setVariable("phoneNumber", emailParameters.get(2));
        context.setVariable("note", emailParameters.get(3));
        return context;
    }

    private Context setContextReturnItem(List<Object> emailParameters){
        Context context = new Context();
        context.setVariable("userName", emailParameters.get(0));
        context.setVariable("email", emailParameters.get(1));
        context.setVariable("orderCode", emailParameters.get(2));
        context.setVariable("status", emailParameters.get(3));
        context.setVariable("statusNote", emailParameters.get(3));
        return context;
    }
}
