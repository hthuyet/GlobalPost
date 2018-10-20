package vn.vnpt.ssdc.event.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import vn.vnpt.ssdc.event.AMQPSubscriber;
import vn.vnpt.ssdc.event.Event;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by ThuyetLV
 */
public class MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    private ApplicationContext ctx;
    private AMQPSubscriber subscriber;

    public MessageHandler(ApplicationContext ctx, AMQPSubscriber subscriber) {
        this.ctx = ctx;
        this.subscriber = subscriber;
    }

    public void handleMessage(Event event) {
        Object instance = ctx.getBean(subscriber.getInstanceClass());
        try {
            subscriber.getConsumeMethod().invoke(instance, event);
        } catch (IllegalAccessException e) {
            logger.error("Error handling event", e);
        } catch (InvocationTargetException e) {
            logger.error("Error handling event", e);
        }
    }
}
