package com.example.userservice.util;

import java.math.BigDecimal;

public class ParseBigDecimal {
    public static BigDecimal parseStringToBigDecimal(String amount) {
        String normalizedAmount = amount.replace(",", "");
        return new BigDecimal(normalizedAmount);
    }
}
