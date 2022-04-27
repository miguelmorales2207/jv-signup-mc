package co.com.dk.juanvaldez.jvsignupmc.exceptions;

import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Slf4j
public class WebClientErrorHandler {

    private static Loggin logger = new Loggin();

    private WebClientErrorHandler() {
    }

    public static Mono<WebClientRestException> manageError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(Map.class).flatMap(WebClientErrorHandler::handleResponse);
    }

    private static Mono<WebClientRestException> handleResponse(Map<String, Object> response) {
        //logger.log("Response received on Web Client Error Handler: {}", response);
        logger.log("Response received on Web Client Error Handler.");

        List<String> errors = new ArrayList<>();
        StringBuilder message = new StringBuilder();
        Object data = null;
        int code = 0;

        if (response.get("message") != null) {
            message.append(response.get("message"));
        }

        if (response.get("error") != null) {
            errors.addAll((List<String>) response.get("error"));
        }

        if (response.get("data") != null) {
            data = response.get("data");
        }

        if (response.get("code") != null) {
            code = Integer.parseInt(response.get("code").toString());
        } else if (response.get("status") != null) {
            code = Integer.parseInt(response.get("status").toString());
        }

        return Mono.error(new WebClientRestException(message.toString(), code, errors, data));
    }

}
