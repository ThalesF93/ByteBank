package br.com.bytebank.accounts.infrastructure.messaging;

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

    private final AccountRepository accountRepository;
    private final AccountEventPublisher eventPublisher;

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.QUEUE_CUSTOMER_CREATED)
    public void onCustomerCreated(CustomerCreatedEvent event) {
        log.info("Event received: CustomerCreatedEvent customerId={}", event.customerId());

        if (accountRepository.existsByCustomerId(event.customerId())) {
            log.warn("Account already exists for customerId={}, skipping", event.customerId());
            return;
        }

        var account = new Account();
        account.setCustomerId(event.customerId());
        accountRepository.save(account);

        log.info("Account opened. accountId={} customerId={}", account.getId(), event.customerId());

        eventPublisher.publishAccountOpened(event.customerId(), account.getId());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_CUSTOMER_CREATED_DLQ)
    public void onCustomerCreatedFailed(CustomerCreatedEvent event) {
        log.error("FAILED to open account after retries. customerId={}", event.customerId());

        eventPublisher.publishAccountFailed(event.customerId());
    }
}