package de.turidus.buttpluggui;

import de.turidus.buttplugManager.deviceManager.Device;
import de.turidus.buttplugManager.deviceManager.VibrationMotor;
import de.turidus.buttplugManager.events.DeviceAddedEvent;
import de.turidus.buttpluggui.deviceControllers.DeviceController;
import de.turidus.buttpluggui.events.StopDevicesEvent;
import de.turidus.buttpluggui.motorControllers.VibrationMotorController;
import de.turidus.buttpluggui.util.FXMLHelper;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GUISupervisor extends AnimationTimer {

    private final Logger                    logger              = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final EventBus                  eventBus;
    private final Map<Integer, Controllers> deviceControllerMap = new HashMap<>();
    private       MainController            mainController      = null;

    public GUISupervisor(@Qualifier("guiEventBus") EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    @Override
    public void handle(long now) {
        if(mainController == null) {return;}
    }

    @Subscribe
    public void onMainControllerEvent(MainController mainController) {
        this.mainController = mainController;
    }

    @Subscribe
    public void onStopDevicesEvent(StopDevicesEvent stopDevicesEvent) {
        deviceControllerMap.values().forEach(controllers -> controllers.vibrationMap.values().forEach(VibrationMotorController::stopMotor));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onDeviceAddedEvent(DeviceAddedEvent dae) {
        generateDeviceController(dae, getMotors(dae.device()));
    }

    private DeviceData getMotors(Device device) {
        List<Parent>                           parentList   = new ArrayList<>();
        Map<Integer, VibrationMotorController> vibrationMap = getVibrationMapAndAddToParentList(device.mapOfVibrationMotors, parentList);
        return new DeviceData(parentList, vibrationMap);
    }

    private Map<Integer, VibrationMotorController> getVibrationMapAndAddToParentList(HashMap<Integer, VibrationMotor> mapOfVibrationMotors,
                                                                                     List<Parent> parentList) {
        return addVibrationMotors(mapOfVibrationMotors, parentList);
    }

    private void generateDeviceController(DeviceAddedEvent dae, DeviceData deviceData) {
        FXMLLoader loader = FXMLHelper.getFXMLLoader("de/turidus/buttpluggui/deviceView");
        try {
            Parent           parent           = loader.load();
            DeviceController deviceController = loader.getController();
            deviceControllerMap.put(dae.device().deviceIndex, new Controllers(deviceController, deviceData.vibrationMap));
            deviceController.addDevice(dae.device());
            deviceController.addMotors(deviceData.parentList);
            mainController.addDevice(parent);
        } catch(IOException e) {
            logger.error("Device Controller could not be loaded.", e);
            throw new RuntimeException(e);
        }
    }

    private Map<Integer, VibrationMotorController> addVibrationMotors(HashMap<Integer, VibrationMotor> mapOfVibrationMotors,
                                                                      List<Parent> parentList) {
        Map<Integer, VibrationMotorController> vibrationMap = new HashMap<>();
        for(Map.Entry<Integer, VibrationMotor> entry : mapOfVibrationMotors.entrySet()) {
            vibrationMap.put(entry.getKey(), addVibrationMotor(entry.getValue(), parentList));
        }
        return vibrationMap;
    }

    private VibrationMotorController addVibrationMotor(VibrationMotor vibrationMotor, List<Parent> parentList) {
        FXMLLoader               loader = FXMLHelper.getFXMLLoader("de/turidus/buttpluggui/vibrationMotorView");
        VibrationMotorController vibrationController;
        try {
            Parent parent = loader.load();
            parentList.add(parent);
            vibrationController = loader.getController();
            vibrationController.addMotor(vibrationMotor);
        } catch(IOException e) {
            logger.error("Device Controller could not be loaded.", e);
            throw new RuntimeException(e);
        }
        return vibrationController;
    }

    private record DeviceData(List<Parent> parentList, Map<Integer, VibrationMotorController> vibrationMap) {}

    private record Controllers(DeviceController deviceController, Map<Integer, VibrationMotorController> vibrationMap) {}

}
