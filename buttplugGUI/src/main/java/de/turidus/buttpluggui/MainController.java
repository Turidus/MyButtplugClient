package de.turidus.buttpluggui;

import de.turidus.buttplugClient.enums.MessageType;
import de.turidus.buttplugManager.events.ConnectToServerEvent;
import de.turidus.buttplugManager.events.DeviceAddedEvent;
import de.turidus.buttplugManager.events.SimpleMessageRequest;
import de.turidus.buttpluggui.deviceControllers.DeviceController;
import de.turidus.buttpluggui.util.FXMLHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainController {

    private final EventBus  defaultBus = EventBus.getDefault();
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final Map<Integer, DeviceController> deviceControllerMap = new HashMap<>();
    @FXML
    private       TextField                      ipTextField;
    @FXML
    private       TextField                      portTextField;
    @FXML
    private       Button    connectButton;
    @FXML
    private       Button    starScanningButton;
    @FXML
    private       Button    stopScanningButton;
    @FXML
    private       Button    stopAllDevicesButton;
    @FXML
    private       HBox      hBox;

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onDeviceAddedEvent(DeviceAddedEvent dae) {
        FXMLLoader loader = FXMLHelper.getFXMLLoader("de/turidus/buttpluggui/deviceView");
        try {
            Parent parent = loader.load();
            DeviceController deviceController = loader.getController();
            deviceControllerMap.put(dae.device().deviceIndex, deviceController);
            deviceController.addDevice(dae.device());
            Platform.runLater(() -> hBox.getChildren().add(parent));
        } catch(IOException e) {
            logger.error("Device Controller could not be loaded.", e);
            throw new RuntimeException(e);
        }
    }

    public void connectButtonPressed() {
        String ip   = ipTextField.getText();
        int    port = Integer.parseInt(portTextField.getText());
        defaultBus.post(new ConnectToServerEvent(ip, port));
    }

    public void starScanningButtonPressed() {
        defaultBus.post(new SimpleMessageRequest(MessageType.START_SCANNING));
        defaultBus.post(new SimpleMessageRequest(MessageType.REQUEST_DEVICE_LIST));
    }

    public void stopScanningButtonPressed() {defaultBus.post(new SimpleMessageRequest(MessageType.STOP_SCANNING));}

    public void stopAllDevicesPressed() {defaultBus.post(new SimpleMessageRequest(MessageType.STOP_ALL_DEVICE));}

    public void initialize() {
        defaultBus.register(this);
    }


}