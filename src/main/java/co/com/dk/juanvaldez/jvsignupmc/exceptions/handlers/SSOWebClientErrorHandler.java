package co.com.dk.juanvaldez.jvsignupmc.exceptions.handlers;

import co.com.dk.juanvaldez.jvsignupmc.exceptions.SSOAuthException;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.SignUpMCRestException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Slf4j
public final class SSOWebClientErrorHandler {

    private SSOWebClientErrorHandler() {
    }

    public static Mono<SignUpMCRestException> manageError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(Map.class)
            .flatMap(SSOWebClientErrorHandler::handleResponse);
    }

    private static Mono<SignUpMCRestException> handleResponse(Map<String, Object> response) {
        //log.info("Response received on Web Client Error Handler: {}", response);

        StringBuilder message = new StringBuilder();
        int code = HttpStatus.INTERNAL_SERVER_ERROR.value();

        if (response.get("message") != null) {
            message.append(response.get("message"));
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
