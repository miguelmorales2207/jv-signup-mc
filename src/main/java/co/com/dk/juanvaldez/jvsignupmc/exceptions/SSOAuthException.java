package co.com.dk.juanvaldez.jvsignupmc.exceptions;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SSOAuthException extends SignUpMCRestException {

    private int httpStatusCode;

    public SSOAuthException(int code, @NotNull final String message) {
        super(message);
        this.httpStatusCode = code;
    }

}
