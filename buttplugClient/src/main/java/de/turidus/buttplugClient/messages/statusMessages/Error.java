package de.turidus.buttplugClient.messages.statusMessages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.turidus.buttplugClient.enums.ErrorCodeEnum;
import de.turidus.buttplugClient.messages.AbstractMessage;

import java.util.Objects;

public class Error extends AbstractMessage {

    public String ErrorMessage;
    public int    ErrorCode;

    private Error() {}

    public Error(int id, String errorMessage, ErrorCodeEnum errorCodeEnum) {
        super(id);
        this.ErrorMessage = errorMessage;
        this.ErrorCode = errorCodeEnum.code;
    }

    @JsonIgnore
    public ErrorCodeEnum getErrorCodeEnum() {
        return ErrorCodeEnum.getErrorCodeFromCode(this.ErrorCode);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        Error error = (Error) o;
        return ErrorCode == error.ErrorCode && Objects.equals(ErrorMessage, error.ErrorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ErrorMessage, ErrorCode);
    }

    @Override
    public String toString() {
        return "Error{" +
               "ErrorMessage='" + ErrorMessage + '\'' +
               ", ErrorCodeEnum=" + ErrorCodeEnum.getErrorCodeFromCode(ErrorCode) +
               ", Id=" + Id +
               '}';
    }

}
