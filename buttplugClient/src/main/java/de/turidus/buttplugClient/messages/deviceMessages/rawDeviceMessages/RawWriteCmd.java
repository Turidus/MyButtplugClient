package de.turidus.buttplugClient.messages.deviceMessages.rawDeviceMessages;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.turidus.buttplugClient.json.ByteArraySerializer;
import de.turidus.buttplugClient.messages.deviceMessages.AbstractDeviceMessage;

import java.util.Arrays;
import java.util.Objects;

public class RawWriteCmd extends AbstractDeviceMessage {

    public String  Endpoint;
    @JsonSerialize(using = ByteArraySerializer.class)
    public byte[]  Data;
    public boolean WriteWithResponse;

    private RawWriteCmd() {
        super();
    }

    public RawWriteCmd(int id, int deviceIndex, String endpoint, byte[] data, boolean writeWithResponse) {
        super(id, deviceIndex);
        this.Endpoint = endpoint;
        this.Data = data;
        this.WriteWithResponse = writeWithResponse;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        RawWriteCmd that = (RawWriteCmd) o;
        return WriteWithResponse == that.WriteWithResponse && Objects.equals(Endpoint, that.Endpoint) &&
               Arrays.equals(Data, that.Data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), Endpoint, WriteWithResponse);
        result = 31 * result + Arrays.hashCode(Data);
        return result;
    }

}
