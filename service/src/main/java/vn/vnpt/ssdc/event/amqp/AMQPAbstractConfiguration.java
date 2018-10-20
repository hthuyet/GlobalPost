package vn.vnpt.ssdc.event.amqp;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * Created by vietnq on 11/29/16.
 */
public abstract class AMQPAbstractConfiguration {
    public static final String TOPIC_EXCHANGE_NAME = "ssdc.ump.event";

    protected static CachingConnectionFactory CONNECTION_FACTORY;

    public synchronized ConnectionFactory connectionFactory() {
        return CONNECTION_FACTORY;
    }

    public abstract void configureRabbitTemplate(RabbitTemplate template);

    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(jsonMessageConverter());
        configureRabbitTemplate(template);
        return template;
    }

    public MessageConverter jsonMessageConverter() {
        return new UmpJsonMessageConverter();
    }

    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }
}
