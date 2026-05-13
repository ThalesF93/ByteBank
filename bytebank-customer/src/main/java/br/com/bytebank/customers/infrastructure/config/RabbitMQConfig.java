package br.com.bytebank.customers.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_CUSTOMER_CREATED = "customer.created";
    public static final String EXCHANGE_CUSTOMERS     = "customers.exchange";
    public static final String ROUTING_KEY_CUSTOMER_CREATED = "customer.created";

    public static final String QUEUE_ACCOUNT_OPENED = "account.opened";

    @Bean
    public Queue customerCreatedQueue() {
        return new Queue(QUEUE_CUSTOMER_CREATED, true);
    }

    @Bean
    public DirectExchange customersExchange() {
        return new DirectExchange(EXCHANGE_CUSTOMERS);
    }

    @Bean
    public Binding customerCreatedBinding(Queue customerCreatedQueue,
                                          DirectExchange customersExchange) {
        return BindingBuilder
                .bind(customerCreatedQueue)
                .to(customersExchange)
                .with(ROUTING_KEY_CUSTOMER_CREATED);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}