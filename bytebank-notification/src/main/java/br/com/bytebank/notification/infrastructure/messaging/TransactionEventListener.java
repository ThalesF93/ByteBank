package br.com.bytebank.notification.infrastructure.messaging;

import br.com.bytebank.notification.application.service.EmailService;
import br.com.bytebank.notification.infrastructure.config.RabbitMQConfig;
import br.com.bytebank.notification.infrastructure.messaging.event.TransactionCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventListener {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.TRANSACTION_COMPLETED)
    public void onTransferenceCompleted(TransactionCompletedEvent event){
        log.info("Event received: CustomerCreatedEvent transactionId={}", event.transactionId() );

        emailService.sendTransactionEmail(event.customerEmail(), event.customerName(), event.operationType(), event.amount());
    }
}
