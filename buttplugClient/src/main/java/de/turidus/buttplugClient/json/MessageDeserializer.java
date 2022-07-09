package de.turidus.buttplugClient.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.turidus.buttplugClient.enums.ErrorCodeEnum;
import de.turidus.buttplugClient.enums.MessageType;
import de.turidus.buttplugClient.messages.Message;
import de.turidus.buttplugClient.messages.statusMessages.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageDeserializer {

    private static final Logger       logger       = LoggerFactory.getLogger(MessageDeserializer.class.getSimpleName());
    public static        ObjectMapper objectMapper = new ObjectMapper();

    public static List<Message> getListOfMessages(String json) {
        json = json.replace("{}", "null");
        List<Message> listOfMessages = new ArrayList<>();
        try {
            List<Map<String, JsonNode>> mapList = objectMapper.readValue(json, new TypeReference<>() {});
            for(Map<String, JsonNode> map : mapList) {
                listOfMessages.add(getMessageFromMap(map));
            }
        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return listOfMessages;
    }

    public static Message getSingleMessageFromJSON(String json) {
        json = json.replace("{}", "null");
        try {
            return getMessageFromMap(objectMapper.readValue(json, new TypeReference<>() {}));
        } catch(JsonProcessingException e) {
            logger.error("Was unable to deserialize message " + json, e);
        }
        return new Error(0, "No message could be deserialized.", ErrorCodeEnum.ERROR_UNKNOWN);
    }

    private static Message getMessageFromMap(Map<String, JsonNode> map) {
        Map.Entry<String, JsonNode> entry = map.entrySet().stream().findFirst().orElseThrow();
        for(MessageType messageType : MessageType.values()) {
            if(messageType.messageClass.getSimpleName().equals(entry.getKey())) {
                try {
                    return objectMapper.treeToValue(entry.getValue(), messageType.messageClass);
                } catch(JsonProcessingException e) {
                    logger.error("Was unable to deserialize message " + entry.getValue().textValue() + " with type " +
                                 messageType.messageClass.getSimpleName(), e);
                }
            }
        }
        return new Error(0, "No message could be deserialized.", ErrorCodeEnum.ERROR_UNKNOWN);
    }

}
