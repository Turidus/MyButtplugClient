package de.turidus.buttplugManager.events;

import de.turidus.buttplugClient.messages.AbstractMessage;

public record NotifyDeviceManagerEvent(AbstractMessage failedMsg) {}
