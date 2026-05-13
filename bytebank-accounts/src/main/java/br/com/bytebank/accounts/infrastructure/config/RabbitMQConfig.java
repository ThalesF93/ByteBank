package br.com.bytebank.accounts.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_ACCOUNT_OPENED       = "account.opened";
    public static final String QUEUE_ACCOUNT_FAILED       = "account.failed";
    public static final String EXCHANGE_ACCOUNT           = "account.exchange";
    public static final String ROUTING_KEY_ACCOUNT_OPENED = "account.opened";
    public static final String ROUTING_KEY_ACCOUNT_FAILED = "account.failed";


    public static final String QUEUE_CUSTOMER_CREATED    = "customer.created";
    public static final String QUEUE_CUSTOMER_CREATED_DLQ = "customer.created.dlq";

    @Bean
    public Queue accountOpenedQueue() {
        return QueueBuilder.durable(QUEUE_ACCOUNT_OPENED).build();
    }

    @Bean
    public Queue accountFailedQueue() {
        return QueueBuilder.durable(QUEUE_ACCOUNT_FAILED).build();
    }

    @Bean
    public DirectExchange accountExchange() {
        return new DirectExchange(EXCHANGE_ACCOUNT);
    }

    @Bean
    public Binding accountOpenedBinding(Queue accountOpenedQueue,
                                        DirectExchange accountExchange) {
        return BindingBuilder.bind(accountOpenedQueue)
                .to(accountExchange)
                .with(ROUTING_KEY_ACCOUNT_OPENED);
    }

    @Bean
    public Binding accountFailedBinding(Queue accountFailedQueue,
                                        DirectExchange accountExchange) {
        return BindingBuilder.bind(accountFailedQueue)
                .to(accountExchange)
                .with(ROUTING_KEY_ACCOUNT_FAILED);
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