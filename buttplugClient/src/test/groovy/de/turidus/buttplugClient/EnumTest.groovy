package de.turidus.buttplugClient

import de.turidus.buttplugClient.enums.ErrorCodeEnum
import spock.lang.Specification

class EnumTest extends Specification {

    def errorCode_test() {
        expect:
        ErrorCodeEnum.ERROR_UNKNOWN.code == 0
        ErrorCodeEnum.ERROR_INIT.code == 1
        ErrorCodeEnum.ERROR_PING.code == 2
        ErrorCodeEnum.ERROR_MSG.code == 3
        ErrorCodeEnum.ERROR_DEVICE.code == 4
    }
}
