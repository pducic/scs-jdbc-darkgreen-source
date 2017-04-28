package demo.conversion.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Clob;

public class ClobJsonSerializer extends StdScalarSerializer<Clob> {
    public ClobJsonSerializer() {
        super(Clob.class);
    }

    public void serialize(Clob value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        try {
            jgen.writeString(IOUtils.toString(value.getAsciiStream(), "UTF-8"));
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        return this.createSchemaNode("string", true);
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        visitor.expectStringFormat(typeHint);
    }
}
