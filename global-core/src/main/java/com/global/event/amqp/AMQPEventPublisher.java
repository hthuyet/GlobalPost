package com.global.event.amqp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.global.event.Event;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
* Created by ThuyetLV
 */
public class AMQPEventPublisher extends AMQPAbstractConfiguration {

    private ExecutorService pool;
    private LinkedBlockingQueue<Runnable> publishQueue = new LinkedBlockingQueue<Runnable>();

    public AMQPEventPublisher() {
        pool = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, publishQueue);
    }

    @Override
    public void configureRabbitTemplate(RabbitTemplate template) {
        template.setExchange(TOPIC_EXCHANGE_NAME);
    }

    public String publish(String routingKey, Event event) {
        event.id = UUID.randomUUID().toString();
        pool.submit(new PublishEventTask(routingKey, event));
        return event.id;
    }

    private class PublishEventTask implements Runnable {

        private Event event;
        private String routingKey;

        public PublishEventTask(String routingKey, Event event) {
            this.routingKey = routingKey;
            this.event = event;
        }

        @Override
        public void run() {
            rabbitTemplate().convertAndSend(routingKey, event);
        }

    }
}
