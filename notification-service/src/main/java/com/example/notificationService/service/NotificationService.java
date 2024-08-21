package com.example.notificationService.service;

import com.example.common.event.CreateEventToNotification;
import com.example.notificationService.email.EmailService;

import com.example.common.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class NotificationService {
    private final EmailService emailService;
    private final RestTemplate restTemplate;

    public void sendMailOrder(CreateEventToNotification orderSendMail) {
        UserDTO user = restTemplate.getForObject("http://localhost:8081/api/v1/user/" + orderSendMail.getUserId(), UserDTO.class);
        String username = user.getUsername();

        String htmlContent = "<div class=\"container\" style=\"font-family: Arial, sans-serif;\n" +
                "    background-color: #f4f4f4;\n" +
                "    margin: 0;\n" +
                "    padding: 0;\">\n" +
                "    <div style=\"@media screen and (max-width: 600px) { max-width: 100%; border-radius: 0; }; max-width: 600px;\n" +
                "    margin: 20px auto;\n" +
                "    background-color: #fff;\n" +
                "    padding: 20px;\n" +
                "    border-radius: 8px;\n" +
                "    box-shadow: 0 0 10px rgba(0,0,0,0.1); \">\n" +
                "        <h2 style=\"color: #333;\">Order Successfully</h2>\n" +
                "        <div class=\"customer-info\">\n" +
                "            <h3>Customer Information</h3>\n" +
                "               <p>User : ${userName}</p>" +
                "        </div>\n" +
                "        <div class=\"product-info\">\n" +
                "            <h3>Product Information</h3>\n" +
                "               <p><span>Total:</span> ${total}</p>" +
//                "            <table style=\"width: 100%;\n" +
//                "            border-collapse: collapse;\n" +
//                "            margin-top: 20px;\">\n" +
//                "                <tr>\n" +
//                "                    <th style=\" background-color: #f2f2f2; padding: 10px;text-align: left;border-bottom: 1px solid #ddd;\">Name</th>\n" +
//                "                    <th style=\" background-color: #f2f2f2; padding: 10px;text-align: left;border-bottom: 1px solid #ddd;\">TotalAmount</th>\n" +
//                "                </tr>\n" +
//                "               <tbody>" +
//                "                    ${orderItems}" +
//                "               </tbody>" +
//                "            </table>\n" +
                "        </div>\n" +
                "        <div class=\"total-wrapper\" style=\"margin-top: 20px; text-align: right;\">\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n";

        htmlContent.replace("${userName}", username);

        htmlContent.replace("${total}", orderSendMail.getPrice().toString());

        emailService.sendMail(orderSendMail.getEmail(), "Order successfully", htmlContent);
    }
}

