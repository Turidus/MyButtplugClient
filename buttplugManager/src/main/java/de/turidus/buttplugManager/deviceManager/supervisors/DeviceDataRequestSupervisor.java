package de.turidus.buttplugManager.deviceManager.supervisors;

import de.turidus.buttplugClient.messages.AbstractMessage;
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.BatteryLevelCmd;
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.RSSILevelCmd;
import de.turidus.buttplugClient.messages.enumerationMessages.RequestDeviceList;
import de.turidus.buttplugManager.deviceManager.DeviceManager;
import de.turidus.buttplugManager.events.ClockEvent;
import de.turidus.buttplugManager.events.ConnectedEvent;
import de.turidus.buttplugManager.events.NewMessageListEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class DeviceDataRequestSupervisor {

    private final EventBus      eventBus;
    private final double        requestTimeInMS;
    private final DeviceManager deviceManager;
    private final AtomicInteger idProvider;
    private       long          deltaTInMS;
    private       boolean       connected = false;

    public DeviceDataRequestSupervisor(@Qualifier("managerEventBus") EventBus eventBus,
                                       @Value("${manager.deviceRequestTimeInMS}") double requestTimeInMS,
                                       DeviceManager deviceManager,
                                       AtomicInteger idProvider) {
        this.eventBus = eventBus;
        this.requestTimeInMS = requestTimeInMS;
        this.deviceManager = deviceManager;
        this.idProvider = idProvider;
        this.eventBus.register(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onClockEvent(ClockEvent clockEvent) {
        if(!connected) {return;}
        deltaTInMS += clockEvent.deltaTInMS();
        if(deltaTInMS >= requestTimeInMS) {
            collectMessagesAndFireEvent();
            deltaTInMS -= requestTimeInMS;
        }
    }

    private void collectMessagesAndFireEvent() {
        List<AbstractMessage> msgList = new ArrayList<>();
        msgList.add(new RequestDeviceList(idProvider.getAndIncrement()));
        msgList.addAll(requestBatteryLevel());
        msgList.addAll(requestRSSILevel());
        eventBus.post(new NewMessageListEvent(msgList));
    }

    @Subscribe
    public void onConnectedEvent(ConnectedEvent event) {
        connected = true;
    }

    private List<BatteryLevelCmd> requestBatteryLevel() {
        return deviceManager.mapOfDevices.values().stream().filter(device -> device.senseBattery)
                                         .map(device -> new BatteryLevelCmd(idProvider.getAndIncrement(), device.deviceIndex))
                                         .collect(Collectors.toList());
    }

    private List<RSSILevelCmd> requestRSSILevel() {
        return deviceManager.mapOfDevices.values().stream().filter(device -> device.senseRSSI)
                                         .map(device -> new RSSILevelCmd(idProvider.getAndIncrement(), device.deviceIndex))
                                         .collect(Collectors.toList());
    }

}
