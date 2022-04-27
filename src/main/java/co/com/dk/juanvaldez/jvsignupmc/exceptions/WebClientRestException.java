package co.com.dk.juanvaldez.jvsignupmc.exceptions;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebClientRestException extends RuntimeException {

    private int httpStatusCode;

    private List<String> errors;

    private Object data;

    public WebClientRestException(String message) {
        super(message);
    }

    public WebClientRestException(String message, int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public WebClientRestException(String message, int httpStatusCode, List<String> errors,
        Object data) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.errors = errors;
        this.data = data;
    }

}
