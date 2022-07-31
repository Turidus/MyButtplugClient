package de.turidus.buttplugClient;

import de.turidus.buttplugClient.enums.MessageType;
import de.turidus.buttplugClient.events.GotMessageEvent;
import de.turidus.buttplugClient.events.SendListOfMessagesEvent;
import de.turidus.buttplugClient.events.SendMessageEvent;
import de.turidus.buttplugClient.json.MessageDeserializer;
import de.turidus.buttplugClient.messages.AbstractMessage;
import de.turidus.buttplugClient.messages.Message;
import de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage.StopAllDevices;
import de.turidus.buttplugClient.messages.handshakeMessages.RequestServerInfo;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;


public class ButtplugClient extends WebSocketClient {

    private final Logger   logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final EventBus eventBus;

    public ButtplugClient(String ip, int port, EventBus eventBus)
            throws URISyntaxException, InterruptedException {
        super(new URI("ws://" + ip + ":" + port));
        this.eventBus = eventBus;
        connectBlocking();
        this.eventBus.register(this);
    }

    @Subscribe
    public void onSendMessageEvent(SendMessageEvent sendMessageEvent) {
        sendMessage(sendMessageEvent.message());
    }

    @Subscribe
    public void onSendListOfMessagesEvent(SendListOfMessagesEvent sendMessageEvent) {
        sendListOfMessages(sendMessageEvent.messages());
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        sendMessage(new RequestServerInfo(1, "Turis Client", 2));
    }

    @Override
    public void onMessage(String message) {
        logger.debug("Got Message from Server: " + message);

        List<Message> messageList = MessageDeserializer.getListOfMessages(message);
        for(Message msg : messageList) {
            eventBus.post(new GotMessageEvent(msg, MessageType.getTypeFromClass(msg.getClass())));
        }

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        send(new StopAllDevices(1).toJsonString());
    }

    @Override
    public void onError(Exception e) {
        logger.error("An exception occurred in the WebSocket connection", e);
    }

    public void sendMessage(AbstractMessage message) {
        sendListOfMessages(Collections.singletonList(message));
    }

    public void sendListOfMessages(List<AbstractMessage> messages) {
        String jsonToSend = AbstractMessage.listToJsonString(messages);
        logger.debug("Sending message to server: " + jsonToSend);
        send(jsonToSend);
    }

}
