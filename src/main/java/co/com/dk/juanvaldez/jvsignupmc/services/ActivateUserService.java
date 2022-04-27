package co.com.dk.juanvaldez.jvsignupmc.services;

import co.com.dk.juanvaldez.jvsignupmc.http.WebClientRequester;
import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ActivateUserService {

    @Value("${spoonity.url.resource}")
    private String spoonityUrl;

    private final WebClientRequester webClientRequester;
    private Loggin logger = new Loggin();

    public ActivateUserService(WebClientRequester webClientRequester) {
        this.webClientRequester = webClientRequester;
    }

}
