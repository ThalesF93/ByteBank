package br.com.bytebank.customers.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_CUSTOMER_CREATED       = "customer.created";
    public static final String QUEUE_CUSTOMER_CREATED_DLQ  = "customer.created.dlq";
    public static final String EXCHANGE_CUSTOMER            = "customer.exchange";
    public static final String ROUTING_KEY_CUSTOMER_CREATED = "customer.created";

    // filas que ele CONSOME (só os nomes, sem declarar)
    public static final String QUEUE_ACCOUNT_OPENED = "account.opened";
    public static final String QUEUE_ACCOUNT_FAILED = "account.failed";

    @Bean
    public Queue customerCreatedQueue() {
        return QueueBuilder.durable(QUEUE_CUSTOMER_CREATED)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", QUEUE_CUSTOMER_CREATED_DLQ)
                .build();
    }

    @Bean
    public Queue customerCreatedDlq() {
        return QueueBuilder.durable(QUEUE_CUSTOMER_CREATED_DLQ).build();
    }

    @Bean
    public DirectExchange customerExchange() {
        return new DirectExchange(EXCHANGE_CUSTOMER);
    }

    @Bean
    public Binding customerCreatedBinding(Queue customerCreatedQueue,
                                          DirectExchange customerExchange) {
        return BindingBuilder.bind(customerCreatedQueue)
                .to(customerExchange)
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