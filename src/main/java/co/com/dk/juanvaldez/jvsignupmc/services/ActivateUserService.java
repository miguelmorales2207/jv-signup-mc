package co.com.dk.juanvaldez.jvsignupmc.services;

import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.SPOONITY_IS_VALIDATED;
import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.SPOONITY_USER_ACTIVATE;
import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.SPOONITY_USER_ACTIVATE_BY_SMS;

import co.com.dk.juanvaldez.jvsignupmc.exceptions.SignUpMCRestException;
import co.com.dk.juanvaldez.jvsignupmc.http.WebClientRequester;
import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import co.com.dk.juanvaldez.jvsignupmc.vo.response.ValidUser;
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

    public void activateSMS(String sessionId, String phone, String country) {
        logger.log("Send confirmation to USER by SMS.");
        userActivationBySMS(sessionId, phone, country);
        logger.log("Confirmation have been sent to USER by SMS.");
    }

    public void activate(String token, String sessionId) {
        logger.log("Activate USER.");
        activateUser(token, sessionId);
        logger.log("USER has been activated successfully.");

        //USER is validate?
        /*logger.log("Validate USER confirmation.");
        ValidUser z = userIsValidate("");
        logger.log("USER confirmation successfully.");*/
    }

    private Object userActivationBySMS(String sessionId, String phone, String country)
        throws SignUpMCRestException {
        String parameters = "?session_identifier=" + sessionId;
        String optionalParameter = "&phone=" + phone + "&country=" + country;
        if (phone != null && country != null) {
            parameters = parameters.concat(optionalParameter);
        }
        String uri = spoonityUrl + SPOONITY_USER_ACTIVATE_BY_SMS + parameters;

        logger.log(String.format("Requesting external service to SEND CONFIRMATION "
            + "for USER ACTIVATION by SMS: {}", uri));
        Object apiResponse = webClientRequester
            .executeGetRequest(uri)
            .bodyToMono(Object.class).block();
        logger.log("API Response of SEND CONFIRMATION for USER ACTIVATION by SMS "
            + "received successfully.");

        return apiResponse;
    }

    private Object activateUser(String token, String sessionId) {
        String parameters = "?token=" + token + "&session_identifier=" + sessionId;
        String uri = spoonityUrl + SPOONITY_USER_ACTIVATE + parameters;

        logger.log(String.format("Requesting external service to USER ACTIVATION: {}", uri));
        Object apiResponse = webClientRequester
            .executeGetRequest(uri)
            .bodyToMono(Object.class).block();
        logger.log("API Response of USER ACTIVATION received successfully.");

        return apiResponse;
    }

    private ValidUser userIsValidate(String session) {
        String parameters = "?session=" + session;
        String uri = spoonityUrl + SPOONITY_IS_VALIDATED + parameters;

        logger.log(String.format("Requesting external service to confirm USER VALIDATE: {}", uri));
        ValidUser apiResponse = webClientRequester
            .executeGetRequest(uri)
            .bodyToMono(ValidUser.class).block();
        logger.log("API Response of confirmation USER VALIDATE received successfully.");

        return apiResponse;
    }

}
