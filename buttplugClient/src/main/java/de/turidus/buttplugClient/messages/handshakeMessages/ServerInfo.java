package de.turidus.buttplugClient.messages.handshakeMessages;

import de.turidus.buttplugClient.messages.AbstractMessage;

import java.util.Objects;

public class ServerInfo extends AbstractMessage {

    public String ServerName;

    public int MessageVersion;

    public int MaxPingTime;

    private ServerInfo() {}

    public ServerInfo(int id, String serverName, int messageVersion, int maxPingTime) {
        super(id);
        this.ServerName = serverName;
        this.MessageVersion = messageVersion;
        this.MaxPingTime = maxPingTime;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        ServerInfo that = (ServerInfo) o;
        return MessageVersion == that.MessageVersion && MaxPingTime == that.MaxPingTime && Objects.equals(ServerName, that.ServerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ServerName, MessageVersion, MaxPingTime);
    }

}
