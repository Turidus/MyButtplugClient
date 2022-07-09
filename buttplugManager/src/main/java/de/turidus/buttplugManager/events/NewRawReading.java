package de.turidus.buttplugManager.events;

import de.turidus.buttplugClient.messages.deviceMessages.rawDeviceMessages.RawReading;

public record NewRawReading(RawReading rawReading) {}
