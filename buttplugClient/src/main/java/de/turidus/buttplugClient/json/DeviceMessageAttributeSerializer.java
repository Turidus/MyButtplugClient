package de.turidus.buttplugClient.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.turidus.buttplugClient.devices.DeviceData;

import java.io.IOException;

public class DeviceMessageAttributeSerializer extends StdSerializer<DeviceData.DeviceMessageAttribute> {

    protected DeviceMessageAttributeSerializer() {
        this(null);
    }

    protected DeviceMessageAttributeSerializer(Class<DeviceData.DeviceMessageAttribute> t) {
        super(t);
    }

    @Override
    public void serialize(DeviceData.DeviceMessageAttribute data, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        if(data != null) {
            gen.writeFieldName("featureCount");
            gen.writeNumber(data.FeatureCount());
            gen.writeFieldName("stepCount");
            gen.writeArray(data.StepCount(), 0, data.StepCount().length);
        }
        gen.writeEndObject();
    }

}
