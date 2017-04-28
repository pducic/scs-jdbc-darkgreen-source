package demo.conversion.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.apache.commons.io.IOUtils;
import org.infobip.util.HexStringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.sql.SQLException;

public class BlobJsonSerializer extends StdScalarSerializer<Blob> {
    public BlobJsonSerializer() {
        super(Blob.class);
    }

    public void serialize(Blob value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        try {
            jgen.writeString(HexStringUtils.bytesToHexString(IOUtils.toByteArray(value.getBinaryStream())));
        } catch (SQLException e) {
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
