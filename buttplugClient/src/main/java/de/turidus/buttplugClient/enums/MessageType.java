package de.turidus.buttplugClient.enums;

import de.turidus.buttplugClient.messages.AbstractMessage;
import de.turidus.buttplugClient.messages.Message;
import de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage.*;
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.BatteryLevelCmd;
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.BatteryLevelReading;
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.RSSILevelCmd;
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.RSSILevelReading;
import de.turidus.buttplugClient.messages.deviceMessages.rawDeviceMessages.*;
import de.turidus.buttplugClient.messages.enumerationMessages.*;
import de.turidus.buttplugClient.messages.handshakeMessages.RequestServerInfo;
import de.turidus.buttplugClient.messages.handshakeMessages.ServerInfo;
import de.turidus.buttplugClient.messages.statusMessages.Error;
import de.turidus.buttplugClient.messages.statusMessages.Ok;
import de.turidus.buttplugClient.messages.statusMessages.Ping;

import java.util.Arrays;

public enum MessageType {

    OK(Ok.class),
    ERROR(Error.class),
    PING(Ping.class),
    REQUEST_SERVER_INFO(RequestServerInfo.class),
    SERVER_INFO(ServerInfo.class),
    START_SCANNING(StartScanning.class),
    STOP_SCANNING(StopScanning.class),
    SCANNING_FINISHED(ScanningFinished.class),
    REQUEST_DEVICE_LIST(RequestDeviceList.class),
    DEVICE_LIST(DeviceList.class),
    DEVICE_ADDED(DeviceAdded.class),
    DEVICE_REMOVED(DeviceRemoved.class),
    RAW_WRITE_CMD(RawWriteCmd.class),
    RAW_READ_CMD(RawReadCmd.class),
    RAW_READING(RawReading.class),
    RAW_SUBSCRIBE_CMD(RawSubscribeCmd.class),
    RAW_UNSUBSCRIBE_CMD(RawUnsubscribeCmd.class),
    STOP_DEVICE_CMD(StopDeviceCmd.class),
    STOP_ALL_DEVICE(StopAllDevices.class),
    VIBRATE_CMD(VibrateCmd.class),
    LINEAR_CMD(LinearCmd.class),
    ROTATE_CMD(RotateCmd.class),
    BATTERY_LEVEL_CMD(BatteryLevelCmd.class),
    BATTERY_LEVEL_READING(BatteryLevelReading.class),
    RSSI_LEVEL_CMD(RSSILevelCmd.class),
    RSSI_LEVEL_READING(RSSILevelReading.class);

    public final Class<? extends AbstractMessage> messageClass;

    MessageType(Class<? extends AbstractMessage> messageClass) {
        this.messageClass = messageClass;
    }

    public static MessageType getTypeFromSimpleName(String simpleName) {
        return Arrays.stream(MessageType.values()).
                     filter(messageType -> messageType.messageClass.getSimpleName().equalsIgnoreCase(simpleName)).
                     findFirst().orElseThrow();
    }

    public static MessageType getTypeFromClass(Class<? extends Message> clazz) {
        return Arrays.stream(MessageType.values()).
                     filter(messageType -> messageType.messageClass.equals(clazz)).
                     findFirst().orElseThrow();
    }
}
