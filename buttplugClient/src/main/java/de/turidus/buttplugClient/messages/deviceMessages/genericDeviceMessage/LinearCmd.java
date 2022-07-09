package de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage;

import de.turidus.buttplugClient.messages.deviceMessages.AbstractDeviceMessage;

import java.util.Arrays;

public class LinearCmd extends AbstractDeviceMessage {

    public Vector[] Vectors;

    private LinearCmd() {
    }

    public LinearCmd(int id, int deviceIndex, Vector[] vectors) {
        super(id, deviceIndex);
        this.Vectors = vectors;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        LinearCmd linearCmd = (LinearCmd) o;
        return Arrays.equals(Vectors, linearCmd.Vectors);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(Vectors);
        return result;
    }

    public record Vector(int Index, int Duration, double Position) {}

}
