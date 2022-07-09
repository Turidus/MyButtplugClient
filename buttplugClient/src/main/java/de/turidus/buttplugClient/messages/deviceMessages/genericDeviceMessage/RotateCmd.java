package de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage;

import de.turidus.buttplugClient.messages.deviceMessages.AbstractDeviceMessage;

import java.util.Arrays;

public class RotateCmd extends AbstractDeviceMessage {

    public Rotation[] Rotations;

    private RotateCmd() {}

    public RotateCmd(int id, int deviceIndex, Rotation[] rotations) {
        super(id, deviceIndex);
        this.Rotations = rotations;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        RotateCmd rotateCmd = (RotateCmd) o;
        return Arrays.equals(Rotations, rotateCmd.Rotations);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(Rotations);
        return result;
    }

    public record Rotation(int Index, double Speed, boolean Clockwise) {}

}
