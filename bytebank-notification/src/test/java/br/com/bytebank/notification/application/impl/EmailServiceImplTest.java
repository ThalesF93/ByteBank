package br.com.bytebank.notification.application.impl;

import br.com.bytebank.notification.application.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    private JavaMailSender mailSender;
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        emailService = new EmailServiceImpl(mailSender);
        ReflectionTestUtils.setField(emailService, "sender", "no-reply@bytebank.com");
    }

    @Test
    @DisplayName("Should send transaction email with correct data")
    void shouldSendTransactionEmailWithCorrectData() {
        String target = "cliente@email.com";
        String name = "João";
        String type = "TRANSFER";
        BigDecimal amount = new BigDecimal("100.00");

        emailService.sendTransactionEmail(target, name, type, amount);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage message = messageCaptor.getValue();

        assertThat(message.getFrom()).isEqualTo("no-reply@bytebank.com");
        assertThat(message.getTo()).containsExactly(target);
        assertThat(message.getSubject()).isEqualTo("Bytebank - Transactions");
        assertThat(message.getText()).isEqualTo(
                "Hello João,\n\n Transference Successfully done\nType: TRANSFER\nAmount: $ 100.00\n\nByteBank"
        );
    }
}