package de.turidus.buttplugClient.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public abstract class AbstractMessage implements Message {

    private final Logger       logger       = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    public        int          Id;

    public AbstractMessage() {}

    public AbstractMessage(int id) {
        this.Id = id;
    }

    public static String listToJsonString(List<AbstractMessage> list) {
        StringBuilder             sb   = new StringBuilder("[");
        Iterator<AbstractMessage> iter = list.iterator();
        while(iter.hasNext()) {
            sb.append(iter.next().toJsonString());
            if(iter.hasNext()) {sb.append(",");}
        }
        sb.append("]");
        return sb.toString();
    }

    public String toJsonString() {
        return toJsonString(objectMapper, logger);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        AbstractMessage that = (AbstractMessage) o;
        return Id == that.Id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

}
