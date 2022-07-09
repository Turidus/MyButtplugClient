package de.turidus.buttplugClient.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ByteArraySerializer extends StdSerializer<byte[]> {

    protected ByteArraySerializer() {
        super(byte[].class);
    }

    private static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }

    @Override
    public void serialize(byte[] bytes, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartArray();
        for(byte b : bytes) {
            jgen.writeNumber(unsignedToBytes(b));
        }
        jgen.writeEndArray();
    }

}
