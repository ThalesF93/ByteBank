package br.com.bytebank.customers.infrastructure.messaging;

import br.com.bytebank.customers.domain.enums.AccountStatus;
import br.com.bytebank.customers.infrastructure.config.RabbitMQConfig;
import br.com.bytebank.customers.infrastructure.messaging.event.AccountFailedEvent;
import br.com.bytebank.customers.infrastructure.messaging.event.AccountOpenedEvent;
import br.com.bytebank.customers.infrastructure.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEventListener {

    private final CustomerRepository customerRepository;

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.QUEUE_ACCOUNT_OPENED)
    public void onAccountOpened(AccountOpenedEvent event) {
        log.info("Event received: AccountOpenedEvent customerId={} accountId={}",
                event.customerId(), event.accountId());

        customerRepository.findById(event.customerId()).ifPresentOrElse(
                customer -> {
                    customer.setAccountStatus(AccountStatus.CREATED);
                    customerRepository.save(customer);
                    log.info("Customer status updated to CREATED. customerId={}", event.customerId());
                },
                () -> log.warn("Customer not found for customerId={}", event.customerId())
        );
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ACCOUNT_FAILED)
    public void onAccountFailed(AccountFailedEvent event) {
        customerRepository.findById(event.customerId()).ifPresent(customer -> {
            customer.setAccountStatus(AccountStatus.FAILED);
            customerRepository.save(customer);
            log.error("Account creation failed. Customer marked as FAILED. customerId={}",
                    event.customerId());
        });
}}