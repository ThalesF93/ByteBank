package br.com.bytebank.accounts.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_ACCOUNT_OPENED      = "account.opened";
    public static final String EXCHANGE_ACCOUNT          = "account.exchange";
    public static final String ROUTING_KEY_ACCOUNT_OPENED = "account.opened";

    public static final String QUEUE_CUSTOMER_CREATED = "customer.created";

    @Bean
    public Queue accountOpenedQueue() {
        return new Queue(QUEUE_ACCOUNT_OPENED, true);
    }

    @Bean
    public DirectExchange accountExchange() {
        return new DirectExchange(EXCHANGE_ACCOUNT);
    }

    @Bean
    public Binding accountOpenedBinding(Queue accountOpenedQueue,
                                        DirectExchange accountExchange) {
        return BindingBuilder
                .bind(accountOpenedQueue)
                .to(accountExchange)
                .with(ROUTING_KEY_ACCOUNT_OPENED);
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