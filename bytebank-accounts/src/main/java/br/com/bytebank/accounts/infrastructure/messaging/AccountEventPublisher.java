package br.com.bytebank.accounts.infrastructure.messaging;

import br.com.bytebank.accounts.infrastructure.config.RabbitMQConfig;
import br.com.bytebank.accounts.infrastructure.messaging.event.AccountFailedEvent;
import br.com.bytebank.accounts.infrastructure.messaging.event.AccountOpenedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishAccountOpened(UUID customerId, UUID accountId) {
        var event = new AccountOpenedEvent(customerId, accountId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_ACCOUNT,
                RabbitMQConfig.ROUTING_KEY_ACCOUNT_OPENED,
                event
        );
        log.info("Event published: AccountOpenedEvent customerId={} accountId={}",
                customerId, accountId);
    }

    public void publishAccountFailed(UUID customerId) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_ACCOUNT,
                RabbitMQConfig.ROUTING_KEY_ACCOUNT_FAILED,
                new AccountFailedEvent(customerId)
        );
}}