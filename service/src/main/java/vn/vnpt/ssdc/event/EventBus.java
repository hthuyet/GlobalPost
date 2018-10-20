package vn.vnpt.ssdc.event;

/**
 * Created by vietnq on 11/29/16.
 */
public interface EventBus {
    public void registerSubscriber(AMQPSubscriber subscriber);
    public String publish(String routingKey, Event event);
}
