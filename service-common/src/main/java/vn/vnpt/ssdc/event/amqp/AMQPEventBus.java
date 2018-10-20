package vn.vnpt.ssdc.event.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.context.ApplicationContext;
import vn.vnpt.ssdc.event.AMQPSubscriber;
import vn.vnpt.ssdc.event.Event;
import vn.vnpt.ssdc.event.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ThuyetLV
 */
public class AMQPEventBus implements EventBus {

    private static final Logger logger = LoggerFactory.getLogger(AMQPEventBus.class);
    private final Object lock = new Object();

    private AMQPEventPublisher publisher;
    private Map<String, AMQPEventListener> subscribers;
    private ApplicationContext ctx;
    private CachingConnectionFactory connectionFactory;

    public AMQPEventBus(CachingConnectionFactory connectionFactory, ApplicationContext ctx) {
        this.connectionFactory = connectionFactory;
        this.ctx = ctx;
        publisher = new AMQPEventPublisher();
        subscribers = new HashMap<String, AMQPEventListener>();
    }

    @Override
    public void registerSubscriber(AMQPSubscriber subscriber) {
        AMQPEventListener listener = new AMQPEventListener(connectionFactory, ctx, subscriber);
        String key = subscriber.getRoutingKey() + "." + subscriber.getQueue();
        logger.info("Registering amqp listener with key {}", key);
        synchronized (lock) {
            subscribers.put(key, listener);
        }
    }

    @Override
    public String publish(String routingKey, Event event) {
        return publisher.publish(routingKey, event);
    }
}
