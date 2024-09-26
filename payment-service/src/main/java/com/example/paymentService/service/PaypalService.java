package com.example.paymentService.service;

import com.example.paymentService.dto.response.OrderResponse;
import com.paypal.api.payments.*;
import com.paypal.api.payments.Links;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PaypalService {
    private final APIContext apiContext;
    private static final BigDecimal EXCHANGE_RATE = new BigDecimal("23000");

    public static BigDecimal convertVNDToUSD(BigDecimal amountVND) {
        return amountVND.divide(EXCHANGE_RATE, 2, BigDecimal.ROUND_HALF_UP);
    }

    public String createPayment(String orderId, OrderResponse orderResponse, String urlReturn) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format(Locale.forLanguageTag("USD"),"%.2f", convertVNDToUSD(orderResponse.getTotalPrice()).doubleValue()));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(orderResponse.getNote());

        List<Transaction> transactions = new java.util.ArrayList<>(Collections.singletonList(transaction));

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(urlReturn + "/" + orderId + "?success=false");
        redirectUrls.setReturnUrl(urlReturn + "/" + orderId + "?success=true");

        payment.setRedirectUrls(redirectUrls);
        Payment createdPayment = payment.create(apiContext);

        return createdPayment.getLinks().stream()
                .filter(link -> link.getRel().equals("approval_url"))
                .findFirst()
                .map(Links::getHref)
                .orElse(null);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }
}
