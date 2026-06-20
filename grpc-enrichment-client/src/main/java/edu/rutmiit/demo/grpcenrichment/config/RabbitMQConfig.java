package edu.rutmiit.demo.grpcenrichment.config;

import edu.rutmiit.demo.events.RoutingKeys;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMQConfig {

    public static final String ENRICHMENT_QUEUE = "q.enrichment.film-created";
    public static final String ENRICHMENT_DLQ = "q.enrichment.film-created.dlq";

    @Bean
    public MessageConverter jsonMessageConverter(JsonMapper jsonMapper) {
        return new JacksonJsonMessageConverter(jsonMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(3);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    @Bean
    public TopicExchange eventsExchange() {
        return ExchangeBuilder
                .topicExchange(RoutingKeys.EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder
                .directExchange(RoutingKeys.EXCHANGE + ".dlx")
                .durable(true)
                .build();
    }

    @Bean
    public Queue enrichmentQueue() {
        return QueueBuilder
                .durable(ENRICHMENT_QUEUE)
                .deadLetterExchange(RoutingKeys.EXCHANGE + ".dlx")
                .deadLetterRoutingKey(ENRICHMENT_DLQ)
                .build();
    }

    @Bean
    public Queue enrichmentDlq() {
        return QueueBuilder.durable(ENRICHMENT_DLQ).build();
    }

    @Bean
    public Binding enrichmentBinding(Queue enrichmentQueue, TopicExchange eventsExchange) {
        return BindingBuilder
                .bind(enrichmentQueue)
                .to(eventsExchange)
                .with(RoutingKeys.FILM_CREATED);
    }

    @Bean
    public Binding enrichmentDlqBinding(Queue enrichmentDlq, DirectExchange deadLetterExchange) {
        return BindingBuilder
                .bind(enrichmentDlq)
                .to(deadLetterExchange)
                .with(ENRICHMENT_DLQ);
    }
}
