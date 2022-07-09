package de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage;

import de.turidus.buttplugClient.messages.deviceMessages.AbstractDeviceMessage;

import java.util.Arrays;

public class VibrateCmd extends AbstractDeviceMessage {

    public Speed[] Speeds;

    private VibrateCmd() {
    }

    public VibrateCmd(int id, int deviceIndex, Speed[] speeds) {
        super(id, deviceIndex);
        this.Speeds = speeds;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        VibrateCmd that = (VibrateCmd) o;
        return Arrays.equals(Speeds, that.Speeds);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(Speeds);
        return result;
    }

    public record Speed(int Index, double Speed) {}

}
