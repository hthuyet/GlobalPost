package vn.vnpt.ssdc.event.amqp;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vnpt.ssdc.event.AMQPSubscriber;
import vn.vnpt.ssdc.event.AMQPSubscribes;
import vn.vnpt.ssdc.event.Event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by vietnq on 12/5/16.
 */
public class AnnotationProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationProcessor.class);
    public static List<AMQPSubscriber> findSubscribers() {

        List<AMQPSubscriber> subscribers = new ArrayList<AMQPSubscriber>();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("vn.vnpt.ssdc"))
                .setScanners(new MethodAnnotationsScanner()));

        Set<Method> annontated = reflections.getMethodsAnnotatedWith(AMQPSubscribes.class);

        for (Method m : annontated) {
            // Analyze each consumer method
            AMQPSubscribes subscribes = m.getAnnotation(AMQPSubscribes.class);
            if (subscribes != null) {
                Class<? extends Event> eventType = analyzeMethod(m);

                subscribers.add(new AMQPSubscriber(subscribes.queue(), subscribes.routingKey(), m.getDeclaringClass(), m, eventType, subscribes.concurrency()));

                logger.debug("Found consumer " + m.getName() + " in class " + m.getDeclaringClass().getName() +
                        ", queue: " + subscribes.queue() + ", routing key: " + subscribes.routingKey() + ", event type: " + eventType);
            }

        }
        return subscribers;
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Event> analyzeMethod(Method m) {

        if (!m.getReturnType().equals(void.class) && !m.getReturnType().equals(Void.class)) {
            logger.error("Method " + m.getName() + " must have void as return type. Type is " + m.getReturnType().getName());
            return null;
        }

        if (m.getParameterTypes().length != 1) {
            logger.error("Method " + m.getName() + " must take ONE argument.");
            return null;
        }

        // Find type of event
        Class<?> argType = m.getParameterTypes()[0];

        // Check that the declared type is a superclass of the argument type
        if (!Event.class.isAssignableFrom(argType)) {
            logger.error("Method " + m.getName() + " argument type must extend Event.");
            return null;
        }

        return (Class<? extends Event>) argType;
    }
}
