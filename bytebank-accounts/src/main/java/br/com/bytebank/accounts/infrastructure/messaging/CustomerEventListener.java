package br.com.bytebank.accounts.infrastructure.messaging;

import br.com.bytebank.accounts.api.dtos.request.AccountRequestDTO;
import br.com.bytebank.accounts.application.service.AccountService;
import br.com.bytebank.accounts.domain.entity.Account;
import br.com.bytebank.accounts.infrastructure.config.RabbitMQConfig;
import br.com.bytebank.accounts.infrastructure.messaging.event.CustomerCreatedEvent;
import br.com.bytebank.accounts.infrastructure.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerEventListener {

    private final AccountService service;
    private final AccountEventPublisher eventPublisher;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_CUSTOMER_CREATED)
    public void onCustomerCreated(CustomerCreatedEvent event) {
        log.info("Event received: CustomerCreatedEvent customerId={}", event.customerId());
        service.openAccount(new AccountRequestDTO(event.customerId()));
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_CUSTOMER_CREATED_DLQ)
    public void onCustomerCreatedFailed(CustomerCreatedEvent event) {
        log.error("FAILED to open account after retries. customerId={}", event.customerId());

        eventPublisher.publishAccountFailed(event.customerId());
    }
}