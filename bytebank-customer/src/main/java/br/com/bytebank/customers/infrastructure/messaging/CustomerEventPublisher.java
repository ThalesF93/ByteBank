
package br.com.bytebank.customers.infrastructure.messaging;

import br.com.bytebank.customers.infrastructure.config.RabbitMQConfig;
import br.com.bytebank.customers.infrastructure.messaging.event.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishCustomerCreated(UUID customerId) {
        var event = new CustomerCreatedEvent(customerId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_CUSTOMER,
                RabbitMQConfig.ROUTING_KEY_CUSTOMER_CREATED,
                event
        );
        log.info("Event published: CustomerCreatedEvent customerId={}", customerId);
    }
}