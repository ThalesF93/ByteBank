package br.com.bytebank.notification.application.impl;

import br.com.bytebank.notification.application.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("$spring.mail.username")
    private String sender;

    @Override
    public void sendTransactionEmail(String target, String name, String type, BigDecimal amount) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(target);
        message.setSubject("Bytebank - Transactions");
        message.setText(String.format(
                "Hello %s,\n\n Transference Successfully done\nType: %s\nAmount: $ %s\n\nByteBank",
                name, type, amount
        ));
        mailSender.send(message);
    }
}
