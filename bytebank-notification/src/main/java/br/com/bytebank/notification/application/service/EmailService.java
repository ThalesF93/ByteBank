package br.com.bytebank.notification.application.service;

import java.math.BigDecimal;

public interface EmailService {
    void sendTransactionEmail(String to, String name, String type, BigDecimal amount);
}
