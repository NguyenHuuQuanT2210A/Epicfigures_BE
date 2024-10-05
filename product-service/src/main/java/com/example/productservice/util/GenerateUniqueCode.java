package com.example.productservice.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GenerateUniqueCode {
    public static String generateProductCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

    public static String generateOrderCode() {
        Date now = new Date();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");

        String yearPart = yearFormat.format(now);
        String monthPart = monthFormat.format(now);

        String prefix = "ODR";

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomPart = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            randomPart.append(characters.charAt(random.nextInt(characters.length())));
        }

        return prefix + yearPart + monthPart + randomPart.toString();
    }
}
