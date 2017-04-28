package demo.conversion.serialization;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.infobip.common.io.SerializationException;
import org.infobip.common.io.Serializer;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

public class JacksonSerializer implements Serializer {
    private ObjectMapper mapper;

    public JacksonSerializer() {
    }

    private ObjectMapper getObjectMapper() {
        if (null != this.mapper) {
            return this.mapper;
        } else {
            this.mapper = new ObjectMapper();

            SimpleModule testModule = new SimpleModule("DarkGreenSync", new Version(1, 0, 0, null, "org.infobip.sync", "infobip-darkgreen-sync"));
            testModule.addSerializer(new ClobJsonSerializer());
            testModule.addSerializer(new BlobJsonSerializer());
            this.mapper.registerModule(testModule);
            this.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            this.mapper.setDateFormat(new ISO8601DateFormat());

            this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return this.mapper;
        }
    }

    public <T> T deserialize(String s, Class<T> aClass) {
        try {
            return this.getObjectMapper().readValue(s, aClass);
        } catch (IOException var4) {
            throw new SerializationException(var4);
        }
    }

    public <T> void deserializeInto(String s, T t) {
        try {
            this.getObjectMapper().readerForUpdating(t).readValue(s);
        } catch (IOException var4) {
            throw new SerializationException(var4);
        }
    }

    public <T> String serialize(T t) {
        try {
            StringWriter e = new StringWriter();
            this.getObjectMapper().writeValue(e, t);
            return e.toString();
        } catch (IOException var3) {
            throw new SerializationException(var3);
        }
    }

    public void setDateFormat(String dateFormat) {
        this.getObjectMapper().setDateFormat(new SimpleDateFormat(dateFormat));
    }

    public void setDateFormat(SimpleDateFormat simpleDateFormat) {
        this.getObjectMapper().setDateFormat(simpleDateFormat);
    }
}
