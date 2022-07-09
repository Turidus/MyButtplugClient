package de.turidus.buttplugClient.messages.handshakeMessages;

import de.turidus.buttplugClient.messages.AbstractMessage;

import java.util.Objects;

public class RequestServerInfo extends AbstractMessage {

    public String ClientName;

    public int MessageVersion;

    private RequestServerInfo() {}

    public RequestServerInfo(int id, String clientName, int messageVersion) {
        super(id);
        this.ClientName = clientName;
        this.MessageVersion = messageVersion;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        RequestServerInfo that = (RequestServerInfo) o;
        return MessageVersion == that.MessageVersion && Objects.equals(ClientName, that.ClientName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ClientName, MessageVersion);
    }

}
