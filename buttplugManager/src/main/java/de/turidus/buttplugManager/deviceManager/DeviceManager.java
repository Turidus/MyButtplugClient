package de.turidus.buttplugManager.deviceManager;

import de.turidus.buttplugClient.devices.DeviceData;
import de.turidus.buttplugClient.enums.MessageType;
import de.turidus.buttplugClient.events.SendListOfMessagesEvent;
import de.turidus.buttplugClient.messages.AbstractMessage;
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.BatteryLevelCmd;
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.RSSILevelCmd;
import de.turidus.buttplugManager.enums.MotorType;
import de.turidus.buttplugManager.events.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DeviceManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    public final EventBus             eventBus;
    public final AtomicInteger        idProvider;
    private final AtomicInteger groupIDProvider = new AtomicInteger();
    public final Map<Integer, Device> mapOfDevices;


    public DeviceManager(@Qualifier("managerEventBus") EventBus eventBus, AtomicInteger idProvider, @Value("#{new java.util.HashMap()}") Map<Integer, Device> mapOfDevices) {
        this.eventBus = eventBus;
        this.idProvider = idProvider;
        this.mapOfDevices = mapOfDevices;
        this.eventBus.register(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onClockEvent(ClockEvent clockEvent) {
        advanceAllMotorsOnAllDevices(clockEvent.deltaTInMS());
        sendDeviceMessages();

    }

    @Subscribe
    public void onNotifyDeviceManagerEvent(NotifyDeviceManagerEvent notifyDeviceManagerEvent) {
        if(notifyDeviceManagerEvent.failedMsg() instanceof BatteryLevelCmd batteryLevelCmd){
            mapOfDevices.get(batteryLevelCmd.DeviceIndex).senseBattery = false;
            logger.warn("Turned off battery sensing for Device " + batteryLevelCmd.DeviceIndex);
        }
        else if(notifyDeviceManagerEvent.failedMsg() instanceof RSSILevelCmd rssiLevelCmd){
            mapOfDevices.get(rssiLevelCmd.DeviceIndex).senseRSSI = false;
            logger.warn("Turned off RSSI sensing for Device " + rssiLevelCmd.DeviceIndex);
        }
    }

    private void sendDeviceMessages() {
        List<AbstractMessage> listOfMessages = collectDeviceMessages();
        if(!listOfMessages.isEmpty()){ this.eventBus.post(new SendListOfMessagesEvent(listOfMessages)); }
    }

    public void addDevice(DeviceData deviceData) {
        Device device = new Device(deviceData, groupIDProvider);
        if(!mapOfDevices.containsKey(device.deviceIndex)){
            mapOfDevices.put(deviceData.DeviceIndex, device);
            eventBus.post(new DeviceAddedEvent(device));
        }
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
