package de.turidus.buttplugClient.messages.deviceMessages.rawDeviceMessages;

import de.turidus.buttplugClient.messages.deviceMessages.AbstractDeviceMessage;

import java.util.Objects;

public class RawReadCmd extends AbstractDeviceMessage {

    public String  Endpoint;
    public int     ExpectedLength;
    public boolean WaitForData;

    private RawReadCmd() {
    }

    public RawReadCmd(int id, int deviceIndex, String endpoint, int expectedLength, boolean waitForData) {
        super(id, deviceIndex);
        this.Endpoint = endpoint;
        this.ExpectedLength = expectedLength;
        this.WaitForData = waitForData;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        RawReadCmd that = (RawReadCmd) o;
        return ExpectedLength == that.ExpectedLength && WaitForData == that.WaitForData && Objects.equals(Endpoint, that.Endpoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), Endpoint, ExpectedLength, WaitForData);
    }

}
