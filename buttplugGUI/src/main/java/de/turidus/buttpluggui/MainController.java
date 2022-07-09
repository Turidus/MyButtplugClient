package de.turidus.buttpluggui;

import de.turidus.buttplugClient.enums.MessageType;
import de.turidus.buttplugManager.events.ConnectToServerEvent;
import de.turidus.buttplugManager.events.SimpleMessageRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.greenrobot.eventbus.EventBus;

public class MainController {

    private final EventBus  defaultBus = EventBus.getDefault();
    @FXML
    private       TextField ipTextField;
    @FXML
    private       TextField portTextField;
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

    public void connectButtonPressed() {
        String ip   = ipTextField.getText();
        int    port = Integer.parseInt(portTextField.getText());
        defaultBus.post(new ConnectToServerEvent(ip, port));
    }

    public void starScanningButtonPressed() {defaultBus.post(new SimpleMessageRequest(MessageType.START_SCANNING));}

    public void stopScanningButtonPressed() {defaultBus.post(new SimpleMessageRequest(MessageType.STOP_SCANNING));}

    public void stopAllDevicesPressed() {defaultBus.post(new SimpleMessageRequest(MessageType.STOP_ALL_DEVICE));}

    public void initialize() {
        //hBox.getChildren().add(loadFXML("vibrationMotorView"));
    }


}