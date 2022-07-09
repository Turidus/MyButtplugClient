package de.turidus.buttplugClient


import de.turidus.buttplugClient.devices.DeviceData
import de.turidus.buttplugClient.enums.ErrorCodeEnum
import de.turidus.buttplugClient.json.MessageDeserializer
import de.turidus.buttplugClient.messages.AbstractMessage
import de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage.*
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.BatteryLevelCmd
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.BatteryLevelReading
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.RSSILevelCmd
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.RSSILevelReading
import de.turidus.buttplugClient.messages.deviceMessages.rawDeviceMessages.*
import de.turidus.buttplugClient.messages.enumerationMessages.*
import de.turidus.buttplugClient.messages.handshakeMessages.RequestServerInfo
import de.turidus.buttplugClient.messages.handshakeMessages.ServerInfo
import de.turidus.buttplugClient.messages.statusMessages.Error
import de.turidus.buttplugClient.messages.statusMessages.Ok
import de.turidus.buttplugClient.messages.statusMessages.Ping
import spock.lang.Shared
import spock.lang.Specification

class AbstractMessageTest extends Specification {
    @Shared
    DeviceData.DeviceMessageAttribute deviceMessageAttribute_vibrateCmd
    @Shared
    DeviceData.DeviceMessageAttribute deviceMessageAttribute_rotateCmd
    @Shared
    DeviceData.DeviceMessageAttribute deviceMessageAttribute_stopCmd
    @Shared
    HashMap<String, DeviceData.DeviceMessageAttribute> devicesMessages
    @Shared
    DeviceData deviceData

    def setupSpec() {
        deviceMessageAttribute_vibrateCmd = new DeviceData.DeviceMessageAttribute(2, new int[]{10, 20})
        deviceMessageAttribute_rotateCmd = new DeviceData.DeviceMessageAttribute(12, new int[]{10})
        deviceMessageAttribute_stopCmd = null
        devicesMessages = new HashMap<>()
        devicesMessages.put("VibrateCmd", deviceMessageAttribute_vibrateCmd)
        devicesMessages.put("RotateCmd", deviceMessageAttribute_rotateCmd)
        devicesMessages.put("StopDeviceCmd", deviceMessageAttribute_stopCmd)
        deviceData = new DeviceData("test", 1, devicesMessages)
    }

    def ok_message_test() {
        Ok msg = new Ok(5)
        Ok msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as Ok
        expect:
        msg.Id == 5
        msg.toJsonString().equals("{\"Ok\":{\"Id\":5}}")
        msg.equals(msg2)
    }

    def error_message_test() {
        Error msg = new Error(5, "test", ErrorCodeEnum.ERROR_MSG)
        Error msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as Error
        expect:
        msg.Id == 5
        msg.ErrorMessage == "test"
        msg.ErrorCode == 3
        msg.getErrorCodeEnum() == ErrorCodeEnum.ERROR_MSG
        msg.toJsonString().equals("{\"Error\":{\"Id\":5,\"ErrorMessage\":\"test\",\"ErrorCode\":3}}")
        msg.equals(msg2)
    }

    def ping_message_test() {
        Ping msg = new Ping(5)
        Ping msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as Ping
        expect:
        msg.Id == 5
        msg.toJsonString().equals("{\"Ping\":{\"Id\":5}}")
        msg.equals(msg2)
    }

    def requestServerInfo_message_test() {
        RequestServerInfo msg = new RequestServerInfo(5, "test", 2)
        RequestServerInfo msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as RequestServerInfo
        expect:
        msg.Id == 5
        msg.ClientName == "test"
        msg.MessageVersion == 2
        msg.toJsonString().equals("{\"RequestServerInfo\":{\"Id\":5,\"ClientName\":\"test\",\"MessageVersion\":2}}")
        msg.equals(msg2)
    }

    def serverInfo_message_test() {
        ServerInfo msg = new ServerInfo(5, "test", 2, 2000)
        ServerInfo msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as ServerInfo
        expect:
        msg.Id == 5
        msg.ServerName == "test"
        msg.MessageVersion == 2
        msg.MaxPingTime == 2000
        msg.toJsonString().equals("{\"ServerInfo\":{\"Id\":5,\"ServerName\":\"test\",\"MessageVersion\":2,\"MaxPingTime\":2000}}")
        msg.equals(msg2)
    }

    def startScanning_message_test() {
        StartScanning msg = new StartScanning(5)
        StartScanning msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as StartScanning
        expect:
        msg.Id == 5
        msg.toJsonString().equals("{\"StartScanning\":{\"Id\":5}}")
        msg.equals(msg2)
    }

    def stopScanning_message_test() {
        StopScanning msg = new StopScanning(5)
        StopScanning msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as StopScanning
        expect:
        msg.Id == 5
        msg.toJsonString().equals("{\"StopScanning\":{\"Id\":5}}")
        msg.equals(msg2)
    }

    def scanningFinished_message_test() {
        ScanningFinished msg = new ScanningFinished(5)
        ScanningFinished msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as ScanningFinished
        expect:
        msg.Id == 5
        msg.toJsonString().equals("{\"ScanningFinished\":{\"Id\":5}}")
        msg.equals(msg2)
    }

    def requestDeviceList_message_test() {
        RequestDeviceList msg = new RequestDeviceList(5)
        RequestDeviceList msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as RequestDeviceList
        expect:
        msg.Id == 5
        msg.toJsonString().equals("{\"RequestDeviceList\":{\"Id\":5}}")
        msg.equals(msg2)
    }

    def deviceList_message_test() {
        List<DeviceData> deviceList = new ArrayList<>();
        deviceList.add(deviceData)
        DeviceList msg = new DeviceList(5, deviceList)
        DeviceList msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as DeviceList
        expect:
        msg.Id == 5
        msg.Devices.size() == 1
        msg.toJsonString().equals("{\"DeviceList\":{\"Id\":5," +
                "\"Devices\":[{\"DeviceName\":\"test\",\"DeviceIndex\":1," +
                "\"DeviceMessages\":{\"VibrateCmd\":{\"FeatureCount\":2,\"StepCount\":[10,20]},\"StopDeviceCmd\":{}," +
                "\"RotateCmd\":{\"FeatureCount\":12,\"StepCount\":[10]}}}]}}")
        msg.equals(msg2)
    }

    def deviceAdded_message_test() {
        DeviceAdded msg = new DeviceAdded(5, "test", 0, devicesMessages)
        DeviceAdded msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as DeviceAdded
        expect:
        msg.Id == 5
        msg.DeviceName == "test"
        msg.DeviceIndex == 0
        msg.DeviceMessages.get("VibrateCmd") == deviceMessageAttribute_vibrateCmd
        msg.toJsonString().equals("{\"DeviceAdded\":{\"Id\":5,\"DeviceName\":\"test\",\"DeviceIndex\":0," +
                "\"DeviceMessages\":{\"VibrateCmd\":{\"FeatureCount\":2,\"StepCount\":[10,20]},\"StopDeviceCmd\":{}," +
                "\"RotateCmd\":{\"FeatureCount\":12,\"StepCount\":[10]}}}}")
        msg.equals(msg2)
    }

    def deviceRemoved_message_test() {
        DeviceRemoved msg = new DeviceRemoved(5, 3)
        DeviceRemoved msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as DeviceRemoved
        expect:
        msg.Id == 5
        msg.DeviceIndex == 3
        msg.toJsonString().equals("{\"DeviceRemoved\":{\"Id\":5,\"DeviceIndex\":3}}")
        msg.equals(msg2)
    }

    def rawWriteCmd_message_test() {
        RawWriteCmd msg = new RawWriteCmd(5, 0, "tx", new byte[]{0, 1, 0}, true)
        RawWriteCmd msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as RawWriteCmd
        expect:
        msg.Id == 5
        msg.DeviceIndex == 0
        msg.Endpoint == "tx"
        msg.Data[1] == (byte) 1
        msg.WriteWithResponse
        msg.toJsonString().equals("{\"RawWriteCmd\":{\"Id\":5,\"DeviceIndex\":0,\"Endpoint\":\"tx\",\"Data\":[0,1,0],\"WriteWithResponse\":true}}")
        msg.equals(msg2)


    }

    def rawReadCmd_message_test() {
        RawReadCmd msg = new RawReadCmd(5, 3, "rx", 10, true)
        RawReadCmd msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as RawReadCmd
        expect:
        msg.Id == 5
        msg.DeviceIndex == 3
        msg.Endpoint == "rx"
        msg.ExpectedLength == 10
        msg.WaitForData
        msg.toJsonString().equals("{\"RawReadCmd\":{\"Id\":5,\"DeviceIndex\":3,\"Endpoint\":\"rx\",\"ExpectedLength\":10,\"WaitForData\":true}}")
        msg.equals(msg2)
    }

    def rawReading_message_test() {
        RawReading msg = new RawReading(5, 3, "rx", new byte[]{10, 1})
        RawReading msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as RawReading
        expect:
        msg.Id == 5
        msg.DeviceIndex == 3
        msg.Endpoint == "rx"
        msg.Data[1] == (byte) 1
        msg.toJsonString().equals("{\"RawReading\":{\"Id\":5,\"DeviceIndex\":3,\"Endpoint\":\"rx\",\"Data\":[10,1]}}")
        msg.equals(msg2)
    }

    def rawSubscribeCmd_message_test() {
        RawSubscribeCmd msg = new RawSubscribeCmd(5, 3, "rx")
        RawSubscribeCmd msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as RawSubscribeCmd
        expect:
        msg.Id == 5
        msg.DeviceIndex == 3
        msg.Endpoint == "rx"
        msg.toJsonString().equals("{\"RawSubscribeCmd\":{\"Id\":5,\"DeviceIndex\":3,\"Endpoint\":\"rx\"}}")
        msg.equals(msg2)
    }

    def rawUnsubscribeCmd_message_test() {
        RawUnsubscribeCmd msg = new RawUnsubscribeCmd(5, 3, "rx")
        RawUnsubscribeCmd msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as RawUnsubscribeCmd
        expect:
        msg.Id == 5
        msg.DeviceIndex == 3
        msg.Endpoint == "rx"
        msg.toJsonString().equals("{\"RawUnsubscribeCmd\":{\"Id\":5,\"DeviceIndex\":3,\"Endpoint\":\"rx\"}}")
        msg.equals(msg2)
    }

    def stopDeviceCmd_message_test() {
        StopDeviceCmd msg = new StopDeviceCmd(5, 3)
        StopDeviceCmd msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as StopDeviceCmd
        expect:
        msg.Id == 5
        msg.DeviceIndex == 3
        msg.toJsonString().equals("{\"StopDeviceCmd\":{\"Id\":5,\"DeviceIndex\":3}}")
        msg.equals(msg2)
    }

    def stopAllDevices_message_test() {
        StopAllDevices msg = new StopAllDevices(5)
        StopAllDevices msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as StopAllDevices
        expect:
        msg.Id == 5
        msg.toJsonString().equals("{\"StopAllDevices\":{\"Id\":5}}")
        msg.equals(msg2)
    }

    def vibrateCmd_message_test() {
        VibrateCmd.Speed[] speeds = new VibrateCmd.Speed[]{new VibrateCmd.Speed(0, 0.3), new VibrateCmd.Speed(1, 0.5)}
        VibrateCmd msg = new VibrateCmd(5, 3, speeds)
        VibrateCmd msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as VibrateCmd
        expect:
        msg.Id == 5
        msg.DeviceIndex == 3
        msg.Speeds[0].Index() == 0
        msg.Speeds[0].Speed() == (double) 0.3
        msg.toJsonString().equals("{\"VibrateCmd\":{\"Id\":5," +
                "\"DeviceIndex\":3,\"Speeds\":[{\"Index\":0,\"Speed\":0.3},{\"Index\":1,\"Speed\":0.5}]}}")
        msg.equals(msg2)
    }

    def linearCmd_message_test() {
        LinearCmd.Vector[] vectors = new LinearCmd.Vector[]{new LinearCmd.Vector(0, 500, 0.5)}
        LinearCmd msg = new LinearCmd(5, 3, vectors)
        LinearCmd msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as LinearCmd
        expect:
        msg.Id == 5
        msg.DeviceIndex == 3
        msg.Vectors[0].Index() == 0
        msg.Vectors[0].Duration() == 500
        msg.Vectors[0].Position() == (double) 0.5
        msg.toJsonString().equals("{\"LinearCmd\":{\"Id\":5,\"DeviceIndex\":3,\"Vectors\":[{\"Index\":0,\"Duration\":500,\"Position\":0.5}]}}")
        msg.equals(msg2)
    }

    def rotateCmd_message_test() {
        RotateCmd.Rotation[] rotations = new RotateCmd.Rotation[]{new RotateCmd.Rotation(0, 0.5, true)}
        RotateCmd msg = new RotateCmd(5, 3, rotations)
        RotateCmd msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as RotateCmd
        expect:
        msg.Id == 5
        msg.DeviceIndex == 3
        msg.Rotations[0].Index() == 0
        msg.Rotations[0].Speed() == (double) 0.5
        msg.Rotations[0].Clockwise()
        msg.toJsonString().equals("{\"RotateCmd\":{\"Id\":5,\"DeviceIndex\":3,\"Rotations\":[{\"Index\":0,\"Speed\":0.5,\"Clockwise\":true}]}}")
        msg.equals(msg2)
    }

    def batteryLevelCmd_message_test() {
        BatteryLevelCmd msg = new BatteryLevelCmd(5, 3)
        BatteryLevelCmd msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as BatteryLevelCmd
        expect:
        msg.Id == 5
        msg.DeviceIndex == 3
        msg.toJsonString().equals("{\"BatteryLevelCmd\":{\"Id\":5,\"DeviceIndex\":3}}")
        msg.equals(msg2)
    }

    def batteryLevelReading_message_test() {
        BatteryLevelReading msg = new BatteryLevelReading(5, 3, 0.3)
        BatteryLevelReading msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as BatteryLevelReading
        expect:
        msg.Id == 5
        msg.DeviceIndex == 3
        msg.BatteryLevel == (double) 0.3
        msg.toJsonString().equals("{\"BatteryLevelReading\":{\"Id\":5,\"DeviceIndex\":3,\"BatteryLevel\":0.3}}")
        msg.equals(msg2)
    }

    def RSSILevelCmd_message_test() {
        RSSILevelCmd msg = new RSSILevelCmd(5, 3)
        RSSILevelCmd msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as RSSILevelCmd
        expect:
        msg.Id == 5
        msg.DeviceIndex == 3
        msg.toJsonString().equals("{\"RSSILevelCmd\":{\"Id\":5,\"DeviceIndex\":3}}")
        msg.equals(msg2)
    }

    def RSSILevelReading_message_test() {
        RSSILevelReading msg = new RSSILevelReading(5, 3, 3)
        RSSILevelReading msg2 = MessageDeserializer.getSingleMessageFromJSON(msg.toJsonString()) as RSSILevelReading
        expect:
        msg.Id == 5
        msg.DeviceIndex == 3
        msg.RSSILevel == 3
        msg.toJsonString().equals("{\"RSSILevelReading\":{\"Id\":5,\"DeviceIndex\":3,\"RSSILevel\":3}}")
        msg.equals(msg2)
    }

    def "Test serializing of lists"() {
        Ok msg = new Ok(5)
        Ok msg2 = new Ok(5)
        List<AbstractMessage> list = new ArrayList<>()
        list.add(msg)
        list.add(msg2)
        expect:
        AbstractMessage.listToJsonString(list).equals("[{\"Ok\":{\"Id\":5}},{\"Ok\":{\"Id\":5}}]")
        list.equals(MessageDeserializer.getListOfMessages("[{\"Ok\":{\"Id\":5}},{\"Ok\":{\"Id\":5}}]"))
    }

}
