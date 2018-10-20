package vn.vnpt.ssdc.event.amqp;


import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import vn.vnpt.ssdc.event.Event;

/**
 * Created by vietnq on 11/29/16.
 */
public class UmpJsonMessageConverter implements MessageConverter {

    private static final Logger logger = LoggerFactory.getLogger(UmpJsonMessageConverter.class);

    private Gson gson;
    private Class<? extends Event> clazz;

    public UmpJsonMessageConverter() {
        this.gson = new Gson();
    }

    public UmpJsonMessageConverter(Class<? extends Event> clazz) {
        this.clazz = clazz;
        this.gson = new Gson();
    }

    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        Message message = new Message(this.gson.toJson(o).getBytes(),messageProperties);
        return message;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        if(clazz == null) {
            throw new UnsupportedOperationException("Class must be set");
        }
        Object object = this.gson.fromJson(new String(message.getBody()),clazz);

        return object;
    }
}
