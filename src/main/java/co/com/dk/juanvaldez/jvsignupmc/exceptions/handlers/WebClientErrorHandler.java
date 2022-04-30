package co.com.dk.juanvaldez.jvsignupmc.exceptions.handlers;

import co.com.dk.juanvaldez.jvsignupmc.exceptions.SignUpMCRestException;
import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class WebClientErrorHandler {

    private static Loggin logger = new Loggin();

    private WebClientErrorHandler() {
    }

    public static Mono<SignUpMCRestException> manageError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(Map.class).flatMap(WebClientErrorHandler::handleResponse);
    }

    private static Mono<SignUpMCRestException> handleResponse(Map<String, Object> response) {
        logger.log(String.format("Response received on Web Client Error Handler: {}", response));

        List<String> errors = new ArrayList<>();
        StringBuilder message = new StringBuilder();
        Object data = null;
        int code = HttpStatus.INTERNAL_SERVER_ERROR.value();

        if (response.get("message") != null) {
            message.append(response.get("message"));
        } else {
            message.append("Error without definition.");
        }

        if (response.get("error") != null) {
            errors.add(response.get("error").toString());
        }

        if (response.get("data") != null) {
            data = response.get("data");
        }

        if (response.get("code") != null) {
            code = Integer.parseInt(response.get("code").toString());
        } else if (response.get("status") != null) {
            code = Integer.parseInt(response.get("status").toString());
        }

        return Mono.error(new SignUpMCRestException(message.toString(), code, errors, data));
    }

}
