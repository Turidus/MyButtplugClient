package de.turidus.buttpluggui;

import de.turidus.buttplugClient.enums.MessageType;
import de.turidus.buttplugManager.events.ConnectToServerEvent;
import de.turidus.buttplugManager.events.SimpleMessageRequest;
import de.turidus.buttpluggui.events.StopDevicesEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {

    private final Logger     logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private       EventBus   guiEventBus;
    @FXML
    private       TextField  ipTextField;
    @FXML
    private       TextField  portTextField;
    @FXML
    private       Button     connectButton;
    @FXML
    private       Button     starScanningButton;
    @FXML
    private       Button     stopScanningButton;
    @FXML
    private       Button     stopAllDevicesButton;
    @FXML
    private       HBox       hBox;
    @FXML
    private       ScrollPane scrollPane;


    public void connectButtonPressed() {
        String ip   = ipTextField.getText();
        int    port = Integer.parseInt(portTextField.getText());
        guiEventBus.post(new ConnectToServerEvent(ip, port));
    }

    public void starScanningButtonPressed() {
        guiEventBus.post(new SimpleMessageRequest(MessageType.START_SCANNING));
        guiEventBus.post(new SimpleMessageRequest(MessageType.REQUEST_DEVICE_LIST));
    }

    public void stopScanningButtonPressed() {guiEventBus.post(new SimpleMessageRequest(MessageType.STOP_SCANNING));}

    public void stopAllDevicesPressed() {
        guiEventBus.post(new StopDevicesEvent());
        guiEventBus.post(new SimpleMessageRequest(MessageType.STOP_ALL_DEVICE));
    }

    public void addDevice(Parent parent) {
        Platform.runLater(() -> hBox.getChildren().add(parent));
    }

    public void initialize() {}

    public void setEventBus(EventBus guiEventBus) {
        this.guiEventBus = guiEventBus;
    }

    public void setConnected(boolean b) {
        connectButton.setDisable(b);
    }

}