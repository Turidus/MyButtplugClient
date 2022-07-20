package de.turidus.buttplugManager.deviceManager;

import de.turidus.buttplugClient.devices.DeviceData;
import de.turidus.buttplugClient.enums.MessageType;
import de.turidus.buttplugClient.events.SendListOfMessagesEvent;
import de.turidus.buttplugClient.messages.AbstractMessage;
import de.turidus.buttplugManager.enums.MotorType;
import de.turidus.buttplugManager.events.ClockEvent;
import de.turidus.buttplugManager.events.DeviceAddedEvent;
import de.turidus.buttplugManager.events.DeviceRemovedEvent;
import de.turidus.buttplugManager.events.SimpleMessageRequest;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DeviceManager {

    public final EventBus             eventBus;
    public final AtomicInteger        idProvider;
    public final Map<Integer, Device> mapOfDevices;


    public DeviceManager(EventBus eventBus, AtomicInteger idProvider, @Value("#{new java.util.HashMap()}") Map<Integer, Device> mapOfDevices) {
        this.eventBus = eventBus;
        this.idProvider = idProvider;
        this.mapOfDevices = mapOfDevices;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onClockEvent(ClockEvent clockEvent) {
        advanceAllMotorsOnAllDevices(clockEvent.deltaTInMS());
        this.eventBus.post(new SendListOfMessagesEvent(collectDeviceMessages()));
    }

    public void addDevice(DeviceData deviceData) {
        Device device = new Device(deviceData);
        mapOfDevices.put(deviceData.DeviceIndex, device);
        eventBus.post(new DeviceAddedEvent(device));
    }

    public void addDevices(List<DeviceData> deviceDataList) {
        deviceDataList.forEach(this::addDevice);
    }

    public void removeDevice(int deviceIndex) {
        Device device = mapOfDevices.remove(deviceIndex);
        eventBus.post(new DeviceRemovedEvent(device));
    }

    public void stopAllDevices() {
        mapOfDevices.values().forEach(Device::stopAllMotors);
    }

    public void advanceAllMotorsOnAllDevices(long deltaTInMS) {
        mapOfDevices.values().forEach(device -> device.advanceAllMotors(deltaTInMS));
    }

    public void setTargetStepOnDeviceByType(int deviceIndex, MotorType motorType, Double targetStep) {
        mapOfDevices.get(deviceIndex).setTargetStepOnAllMotorsByType(motorType, targetStep);
    }

    private List<AbstractMessage> collectDeviceMessages() {
        List<AbstractMessage> messageList = new ArrayList<>();
        for(Device device : mapOfDevices.values()) {
            messageList.addAll(device.collectMotorMessages(idProvider));
        }
        return messageList;
    }

}
