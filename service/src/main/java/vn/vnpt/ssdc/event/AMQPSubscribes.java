package vn.vnpt.ssdc.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by vietnq on 12/5/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AMQPSubscribes {
    public String queue() default "";
    public String routingKey() default "";
    public int concurrency() default 1;
}
