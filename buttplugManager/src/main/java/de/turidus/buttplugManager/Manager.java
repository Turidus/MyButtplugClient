package de.turidus.buttplugManager;

import de.turidus.buttplugClient.devices.DeviceData;
import de.turidus.buttplugClient.events.GotMessageEvent;
import de.turidus.buttplugClient.events.SendMessageEvent;
import de.turidus.buttplugClient.messages.AbstractMessage;
import de.turidus.buttplugClient.messages.Message;
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.BatteryLevelReading;
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.RSSILevelReading;
import de.turidus.buttplugClient.messages.deviceMessages.rawDeviceMessages.RawReading;
import de.turidus.buttplugClient.messages.enumerationMessages.DeviceAdded;
import de.turidus.buttplugClient.messages.enumerationMessages.DeviceList;
import de.turidus.buttplugClient.messages.enumerationMessages.DeviceRemoved;
import de.turidus.buttplugClient.messages.handshakeMessages.ServerInfo;
import de.turidus.buttplugClient.messages.statusMessages.Error;
import de.turidus.buttplugClient.messages.statusMessages.Ok;
import de.turidus.buttplugManager.deviceManager.DeviceManager;
import de.turidus.buttplugManager.events.NewMessageEvent;
import de.turidus.buttplugManager.events.NewRawReading;
import de.turidus.buttplugManager.utils.PingManager;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Manager {

    private final Logger                        logger     = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final EventBus                      eventBus;
    private final DeviceManager                 deviceManager;
    private final PingManager                   pingManager;
    private final Map<Integer, AbstractMessage> messageMap = new HashMap<>();

    public Manager(EventBus eventBus, DeviceManager deviceManager, PingManager pingManager) {
        this.eventBus = eventBus;
        this.deviceManager = deviceManager;
        this.pingManager = pingManager;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onGotMessageEvent(GotMessageEvent gme) {
        switch(gme.messageType) {
            case OK -> handelOk(gme.msg);
            case ERROR -> handelError(gme.msg);
            case SERVER_INFO -> handelServerInfo((ServerInfo) gme.msg);
            case SCANNING_FINISHED -> logger.info("Scanning was finished.");
            case DEVICE_LIST -> handelDeviceList(gme.msg);
            case DEVICE_ADDED -> handelDeviceAdded(gme.msg);
            case DEVICE_REMOVED -> handelDeviceRemoved(gme.msg);
            case RAW_READING -> handelRawReading(gme.msg);
            case BATTERY_LEVEL_READING -> handelBatteryLevelReading(gme.msg);
            case RSSI_LEVEL_READING -> handelRSSILevelReading(gme.msg);
            default -> logger.warn("Received an unexpected message: " + gme.messageType);
        }
    }


    @Subscribe
    public void onNewMessageEvent(NewMessageEvent nme) {
        messageMap.put(nme.messageId(), nme.message());
        eventBus.post(new SendMessageEvent(nme.message()));
    }

    public int sizeOfMessageMap() {
        return messageMap.size();
    }

    private void handelOk(Message msg) {
        messageMap.remove(((Ok) msg).Id);
    }

    private void handelError(Message msg) {
        Error           error     = (Error) msg;
        AbstractMessage failedMsg = messageMap.remove(error.Id);
        logger.warn("Message " + failedMsg.toString() + " failed: " + error);
    }

    private void handelServerInfo(ServerInfo msg) {
        pingManager.setMaxPingTime(msg.MaxPingTime);
    }

    private void handelDeviceList(Message msg) {
        DeviceList deviceList = (DeviceList) msg;
        deviceManager.addDevices(deviceList.Devices);
    }

    private void handelDeviceAdded(Message msg) {
        DeviceAdded deviceAdded = (DeviceAdded) msg;
        deviceManager.addDevice(new DeviceData(deviceAdded.DeviceName, deviceAdded.DeviceIndex, deviceAdded.DeviceMessages));
    }

    private void handelDeviceRemoved(Message msg) {
        deviceManager.removeDevice(((DeviceRemoved) msg).Id);
    }

    private void handelRawReading(Message msg) {
        eventBus.post(new NewRawReading((RawReading) msg));
    }

    private void handelBatteryLevelReading(Message msg) {
        BatteryLevelReading batteryLevelReading = (BatteryLevelReading) msg;
        deviceManager.mapOfDevices.get(batteryLevelReading.DeviceIndex).batteryLevel = batteryLevelReading.BatteryLevel;
    }

    private void handelRSSILevelReading(Message msg) {
        RSSILevelReading rssiLevelReading = (RSSILevelReading) msg;
        deviceManager.mapOfDevices.get(rssiLevelReading.DeviceIndex).rssiLevel = rssiLevelReading.RSSILevel;
    }

}