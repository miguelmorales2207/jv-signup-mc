package co.com.dk.juanvaldez.jvsignupmc.services;

import co.com.dk.juanvaldez.jvsignupmc.data.domain.Prestamo;
import co.com.dk.juanvaldez.jvsignupmc.data.domain.User;
import co.com.dk.juanvaldez.jvsignupmc.http.WebClientRequester;
import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Value("${spoonity.url.resource}")
    private String spoonityUrl;

    @Value("${spoonity.uri.service.resource}")
    private String spoonityUri;

    private final WebClientRequester webClientRequester;
    private Loggin logger = new Loggin();

    public SignUpService(WebClientRequester webClientRequester) {
        this.webClientRequester = webClientRequester;
    }

    public Prestamo activate(String id) {
        String uri = spoonityUrl + spoonityUri + id;
        //logger.log("Requesting external service to ACTIVATE USER: {}", uri);

        logger.log("Requesting external service to ACTIVATE USER.");
        Prestamo apiResponse = webClientRequester
            .executeGetRequest(uri)
            .bodyToMono(Prestamo.class).block();
        logger.log("ApiResponse of ACTIVATE USER received successfully.");

        return apiResponse;
    }

    public User signup(User createUser) {
        String uri = spoonityUrl + spoonityUri;
        //logger.log("Requesting external service to CREATE USER: {}", uri);

        logger.log("Requesting external service to CREATE USER.");
        User apiResponse = webClientRequester
            .executePostRequest(uri, createUser)
            .bodyToMono(User.class).block();
        logger.log("ApiResponse of CREATE USER received successfully.");

        return apiResponse;
    }

}
