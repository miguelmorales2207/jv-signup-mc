package co.com.dk.juanvaldez.jvsignupmc.exceptions.handlers;

import co.com.dk.juanvaldez.jvsignupmc.exceptions.SSOAuthException;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.SignUpMCRestException;
import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public final class SSOWebClientErrorHandler {

    private static Loggin logger = new Loggin();

    private SSOWebClientErrorHandler() {
    }

    public static Mono<SignUpMCRestException> manageError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(Map.class)
            .flatMap(SSOWebClientErrorHandler::handleResponse);
    }

    private static Mono<SignUpMCRestException> handleResponse(Map<String, Object> response) {
        logger.log(String.format("Response received on Web Client Error Handler: {}", response));

        StringBuilder message = new StringBuilder();
        int code = HttpStatus.INTERNAL_SERVER_ERROR.value();

        if (response.get("message") != null) {
            message.append(response.get("message"));
        } else {
            message.append("Security problem.");
        }

        if (response.get("messages") != null) {
            message.append(response.get("messages"));
        }

        if (response.get("code") != null) {
            code = Integer.parseInt(response.get("code").toString());
        }

        return Mono.error(new SSOAuthException(code, message.toString()));
    }

}
