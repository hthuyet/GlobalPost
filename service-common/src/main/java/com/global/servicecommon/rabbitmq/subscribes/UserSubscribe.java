package com.global.servicecommon.rabbitmq.subscribes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import vn.vnpt.ssdc.event.AMQPSubscribes;
import vn.vnpt.ssdc.event.Event;

/**
 * Created by ThuyetLV
 */
@Component
public class UserSubscribe {

    private static final Logger logger = LoggerFactory.getLogger(UserSubscribe.class);

    @AMQPSubscribes(queue = "user-create")
    public void processUserCreate(Event event) {
        logger.info("Create user #{}", event.message.get("username"));
    }
}
