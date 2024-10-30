package com.example.paymentService.util;

import java.math.BigDecimal;

public class ParseBigDecimal {
    public static BigDecimal parseStringToBigDecimal(String amount) {
        String normalizedAmount = amount.replace(",", "");
        return new BigDecimal(normalizedAmount);
    }
}
