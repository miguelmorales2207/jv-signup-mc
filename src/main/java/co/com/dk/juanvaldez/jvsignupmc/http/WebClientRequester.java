package co.com.dk.juanvaldez.jvsignupmc.http;

import co.com.dk.juanvaldez.jvsignupmc.data.domain.requestUser.RequestUserBody;
import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import co.com.dk.juanvaldez.jvsignupmc.security.User;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.WebClientErrorHandler;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

@Repository
public class WebClientRequester {

    private final WebClient webClient;
    private final ModelMapper modelMapper;
    private Loggin logger = new Loggin();

    public WebClientRequester(WebClient webClient, ModelMapper modelMapper) {
        this.webClient = webClient;
        this.modelMapper = modelMapper;
    }

    public ResponseSpec executeGetRequest(String uri) {

        return webClient.get()
            .uri(uri)
            //.header(HttpHeaders.AUTHORIZATION, token)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::isError, WebClientErrorHandler::manageError);
    }

    public ResponseSpec executePostRequest(String uri, RequestUserBody requestUser) {
        requestUser.setUser(getAuthenticatedUser());

        return webClient.post()
            .uri(uri)
            //.header(HttpHeaders.AUTHORIZATION, token)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(requestUser), requestUser.getClass())
            .retrieve()
            .onStatus(HttpStatus::isError, WebClientErrorHandler::manageError);
    }

    private co.com.dk.juanvaldez.jvsignupmc.data.domain.requestUser.User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.log("Getting authenticated USER from or token.");
        User user = (User) authentication.getPrincipal();
        //log.trace("User obtained successfully from or token. {}", user);
        logger.log("USER obtained successfully from or token.");

        return modelMapper
            .map(user, co.com.dk.juanvaldez.jvsignupmc.data.domain.requestUser.User.class);
    }

}
