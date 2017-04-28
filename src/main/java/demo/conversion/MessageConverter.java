package demo.conversion;

import demo.conversion.serialization.JacksonSerializer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeType;

import java.util.Map;

public class MessageConverter extends AbstractMessageConverter {

    private JacksonSerializer jacksonSerializer = new JacksonSerializer();

    public MessageConverter() {
        super(new MimeType("application", "json"));
    }

    @Override
    protected boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    protected Object convertToInternal(Object payload, MessageHeaders headers, Object conversionHint) {
        return jacksonSerializer.serialize(payload);
        //return jacksonSerializer.deserialize((String)payload, Map.class);
        //return super.convertToInternal(payload, headers, conversionHint);
    }

    @Override
    protected Object convertFromInternal(Message<?> message, Class<?> targetClass, Object conversionHint) {
        return jacksonSerializer.deserialize((String)message.getPayload(), Map.class);
    }
}
