package com.global.event;

/**
 * Created by ThuyetLV
 */
public interface EventBus {

    public void registerSubscriber(AMQPSubscriber subscriber);

    public String publish(String routingKey, Event event);
}
