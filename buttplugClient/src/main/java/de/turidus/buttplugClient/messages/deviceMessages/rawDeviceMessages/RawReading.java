package de.turidus.buttplugClient.messages.deviceMessages.rawDeviceMessages;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.turidus.buttplugClient.json.ByteArraySerializer;
import de.turidus.buttplugClient.messages.deviceMessages.AbstractDeviceMessage;

import java.util.Arrays;
import java.util.Objects;

public class RawReading extends AbstractDeviceMessage {

    public String Endpoint;
    @JsonSerialize(using = ByteArraySerializer.class)
    public byte[] Data;

    private RawReading() {
    }

    public RawReading(int id, int deviceIndex, String endpoint, byte[] data) {
        super(id, deviceIndex);
        this.Endpoint = endpoint;
        this.Data = data;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        RawReading that = (RawReading) o;
        return Objects.equals(Endpoint, that.Endpoint) && Arrays.equals(Data, that.Data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), Endpoint);
        result = 31 * result + Arrays.hashCode(Data);
        return result;
    }

}
