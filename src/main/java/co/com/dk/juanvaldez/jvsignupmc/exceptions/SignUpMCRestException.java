package co.com.dk.juanvaldez.jvsignupmc.exceptions;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpMCRestException extends RuntimeException {

    private int httpStatusCode;

    private List<String> errors;

    private Object data;

    public SignUpMCRestException(String message) {
        super(message);
    }

    public SignUpMCRestException(String message, int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public SignUpMCRestException(String message, int httpStatusCode, List<String> errors,
        Object data) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.errors = errors;
        this.data = data;
    }

}
