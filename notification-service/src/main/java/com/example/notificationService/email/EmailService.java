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
    public void sendMail(String email, String subject, Object object, String template) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            Context context = new Context();

            String html = switch (template) {
                case "thank-you" -> {
                    context.setVariable("order", object);
                    yield templateEngine.process(template, context);
                }
                case "forgot-password" -> {
                    context.setVariable("forgot_password", object);
                    yield templateEngine.process(template, context);
                }
                case "contact" -> {
                    context.setVariable("contact", object);

                    yield templateEngine.process(template, context);
                }
                case "return-item" -> {
                    context.setVariable("return_item", object);
                    yield templateEngine.process(template, context);
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
}
