package de.turidus.buttplugManager.deviceManager;

import de.turidus.buttplugClient.devices.DeviceData;
import de.turidus.buttplugClient.messages.AbstractMessage;
import de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage.LinearCmd;
import de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage.RotateCmd;
import de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage.VibrateCmd;
import de.turidus.buttplugManager.deviceManager.motors.LinearMotor;
import de.turidus.buttplugManager.deviceManager.motors.Motor;
import de.turidus.buttplugManager.deviceManager.motors.RotationMotor;
import de.turidus.buttplugManager.deviceManager.motors.VibrationMotor;
import de.turidus.buttplugManager.enums.MotorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Device {

    public final  String                           name;
    public final  int                              deviceIndex;
    public final  HashMap<Integer, VibrationMotor> mapOfVibrationMotors = new HashMap<>();
    public final  HashMap<Integer, RotationMotor>  mapOfRotationMotors  = new HashMap<>();
    public final  HashMap<Integer, LinearMotor>    mapOfLinearMotors    = new HashMap<>();
    private final Logger                           logger               = LoggerFactory.getLogger(this.getClass().getSimpleName());
    public        double                           batteryLevel         = 1;
    public        int                              rssiLevel            = 0;
    public        boolean                          senseBattery         = true;
    public        boolean                          senseRSSI            = true;
    private       boolean                          stoppable            = false;

    public Device(DeviceData deviceData, AtomicInteger groupIDProvider) {
        name = deviceData.DeviceName;
        deviceIndex = deviceData.DeviceIndex;
        addMotors(deviceData.DeviceMessages, groupIDProvider);
        if(!stoppable) {logger.warn("Device " + deviceIndex + " had no stop command.");}
    }


    public void setTargetStepOnAllMotorsByType(MotorType motorType, double targetStep) {
        switch(motorType) {
            case VIBRATION -> mapOfVibrationMotors.values().forEach(motor -> motor.setNextTarget(targetStep));
            case ROTATION -> mapOfRotationMotors.values().forEach(motor -> motor.setNextTarget(targetStep));
            case LINEAR -> mapOfLinearMotors.values().forEach(motor -> motor.setNextTargetAndDurationOfMovement(targetStep, Double.MIN_VALUE));
        }
    }

    public void advanceAllMotors(long deltaTInMS) {
        for(Motor motor : getAllMotors()) {
            motor.calculateNextStep(deltaTInMS);
        }
    }

    public List<Motor> getAllMotors() {
        List<Motor> deviceList = new ArrayList<>();
        deviceList.addAll(mapOfLinearMotors.values());
        deviceList.addAll(mapOfRotationMotors.values());
        deviceList.addAll(mapOfVibrationMotors.values());
        return deviceList;
    }

    public List<AbstractMessage> collectMotorMessages(AtomicInteger idProvider) {
        List<AbstractMessage> messageList = new ArrayList<>();
        if(mapOfLinearMotors.size() > 0) {messageList.add(getLinearCmd(idProvider));}
        if(mapOfRotationMotors.size() > 0) {messageList.add(getRotationCmd(idProvider));}
        if(mapOfVibrationMotors.size() > 0) {messageList.add(getVibrationCmd(idProvider));}
        return messageList;
    }

    private AbstractMessage getVibrationCmd(AtomicInteger idProvider) {
        VibrateCmd.Speed[] speeds = new VibrateCmd.Speed[mapOfVibrationMotors.size()];
        for(Map.Entry<Integer, VibrationMotor> entry : mapOfVibrationMotors.entrySet()) {
            speeds[entry.getKey()] = entry.getValue().getSpeed();
        }
        return new VibrateCmd(idProvider.getAndIncrement(), deviceIndex, speeds);
    }

    private AbstractMessage getRotationCmd(AtomicInteger idProvider) {
        RotateCmd.Rotation[] rotations = new RotateCmd.Rotation[mapOfRotationMotors.size()];
        for(Map.Entry<Integer, RotationMotor> entry : mapOfRotationMotors.entrySet()) {
            rotations[entry.getKey()] = entry.getValue().getRotation();
        }
        return new RotateCmd(idProvider.getAndIncrement(), deviceIndex, rotations);
    }

    private AbstractMessage getLinearCmd(AtomicInteger idProvider) {
        LinearCmd.Vector[] vectors = new LinearCmd.Vector[mapOfLinearMotors.size()];
        for(Map.Entry<Integer, LinearMotor> entry : mapOfLinearMotors.entrySet()) {
            vectors[entry.getKey()] = entry.getValue().getVector();
        }
        return new LinearCmd(idProvider.getAndIncrement(), deviceIndex, vectors);
    }

    public boolean isStoppable() {
        return stoppable;
    }

    private void addMotors(HashMap<String, DeviceData.DeviceMessageAttribute> deviceMessages, AtomicInteger groupIDProvider) {
        for(Map.Entry<String, DeviceData.DeviceMessageAttribute> entry : deviceMessages.entrySet()) {
            switch(entry.getKey()) {
                case "VibrateCmd" -> addVibrationMotor(entry.getValue(), groupIDProvider);
                case "RotateCmd" -> addRotationMotor(entry.getValue(), groupIDProvider);
                case "LinearCmd" -> addLinearMotor(entry.getValue(), groupIDProvider);
                case "StopDeviceCmd" -> stoppable = true;
                default -> logger.warn("Device " + deviceIndex + " had an unsupported command: " + entry.getKey());
            }
        }
    }

    private void addLinearMotor(DeviceData.DeviceMessageAttribute data, AtomicInteger groupIDProvider) {
        for(int i = 0; i < data.FeatureCount(); i++) {
            mapOfLinearMotors.put(i, new LinearMotor(i, data.StepCount()[i], groupIDProvider.getAndIncrement()));
        }
    }

    private void addRotationMotor(DeviceData.DeviceMessageAttribute data, AtomicInteger groupIDProvider) {
        for(int i = 0; i < data.FeatureCount(); i++) {
            mapOfRotationMotors.put(i, new RotationMotor(i, data.StepCount()[i], groupIDProvider.getAndIncrement()));
        }
    }

    private void addVibrationMotor(DeviceData.DeviceMessageAttribute data, AtomicInteger groupIDProvider) {
        for(int i = 0; i < data.FeatureCount(); i++) {
            mapOfVibrationMotors.put(i, new VibrationMotor(i, data.StepCount()[i], groupIDProvider.getAndIncrement()));
        }
    }

    public void stopAllMotors() {
        for(Motor motor : getAllMotors()) {
            motor.stop();
        }
    }

}
