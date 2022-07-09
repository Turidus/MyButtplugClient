package de.turidus.buttplugClient.messages.deviceMessages.rawDeviceMessages;

import de.turidus.buttplugClient.messages.deviceMessages.AbstractDeviceMessage;

import java.util.Objects;

public class RawUnsubscribeCmd extends AbstractDeviceMessage {

    public String Endpoint;

    private RawUnsubscribeCmd() {
    }

    public RawUnsubscribeCmd(int id, int deviceIndex, String endpoint) {
        super(id, deviceIndex);
        this.Endpoint = endpoint;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        RawUnsubscribeCmd that = (RawUnsubscribeCmd) o;
        return Objects.equals(Endpoint, that.Endpoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), Endpoint);
    }

}
