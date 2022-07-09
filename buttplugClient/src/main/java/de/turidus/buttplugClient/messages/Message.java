package de.turidus.buttplugClient.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

public interface Message {

    default String toJsonString(ObjectMapper objectMapper, Logger logger) {
        try {
            return ("{\"" + this.getClass().getSimpleName() + "\":" + objectMapper.writeValueAsString(this) + "}").replace("null", "{}");
        } catch(JsonProcessingException e) {
            logger.error("Could not parse to Json", e);
            return "";
        }
    }

}
