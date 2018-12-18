package com.global.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.global.event.amqp.AnnotationProcessor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ThuyetLV
 */
@Component
public class AMQPService {

    private static final Logger logger = LoggerFactory.getLogger(AMQPService.class);
    private Set<String> listeners = new HashSet<String>();
    private ApplicationContext ctx;

    @Autowired
    public AMQPService(ApplicationContext ctx) {
        this.ctx = ctx;
        doInitialize();
    }

    public static void initialize(ApplicationContext ctx) {
        AMQPService service = ctx.getBean(AMQPService.class);
        service.doInitialize();
    }

    private synchronized void doInitialize() {
        logger.info("Initializing ampq listeners ....");
        List<AMQPSubscriber> subscribers = AnnotationProcessor.findSubscribers();
        EventBus eventBus = ctx.getBean(EventBus.class);
        for (AMQPSubscriber subscriber : subscribers) {
            if (!listeners.contains(subscriber.getConsumeMethod().getName())) {
                logger.info("Registering handler for routing key: {}, queue: {}", subscriber.getRoutingKey(), subscriber.getQueue());
                eventBus.registerSubscriber(subscriber);
                listeners.add(subscriber.getConsumeMethod().getName());
            }
        }
        logger.info("Registered {} amqp listeners", listeners.size());
    }
}
