package de.turidus.buttplugClient.enums;

public enum ErrorCodeEnum {

    ERROR_UNKNOWN(0),
    ERROR_INIT(1),
    ERROR_PING(2),
    ERROR_MSG(3),
    ERROR_DEVICE(4);

    public final int code;

    ErrorCodeEnum(int code) {this.code = code;}

    public static ErrorCodeEnum getErrorCodeFromCode(int code) {
        for(ErrorCodeEnum errorCodeEnum : ErrorCodeEnum.values()) {
            if(errorCodeEnum.code == code) {return errorCodeEnum;}
        }
        return ErrorCodeEnum.ERROR_UNKNOWN;
    }
}
