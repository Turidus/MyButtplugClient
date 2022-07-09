package de.turidus.buttplugClient.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.turidus.buttplugClient.devices.DeviceData;

import java.io.IOException;

public class DeviceMessageAttributeDeserializer extends StdDeserializer<DeviceData.DeviceMessageAttribute> {

    protected DeviceMessageAttributeDeserializer() {
        this(null);
    }

    protected DeviceMessageAttributeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public DeviceData.DeviceMessageAttribute deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);

        if(node.isNull() || node.asText().isEmpty() || node.size() == 0) {
            return null;
        }

        int      featureCount  = node.get("featureCount").asInt();
        JsonNode stepCountNode = node.get("stepCount");
        if(stepCountNode.isArray()) {
            int[] stepCount = new int[stepCountNode.size()];
            for(int i = 0; i < stepCountNode.size(); i++) {
                stepCount[i] = stepCountNode.get(i).asInt();
            }
            return new DeviceData.DeviceMessageAttribute(featureCount, new int[] {0});
        }
        else {throw new RuntimeException("There was a malformed JSON node: " + node.asText());}
    }

}
