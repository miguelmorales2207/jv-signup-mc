package co.com.dk.juanvaldez.jvsignupmc.services;

import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.SPOONITY_USER_REGISTER;
import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.SPOONITY_USER_EMAIL_EXISTS;
import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.SPOONITY_USER_CEDULA_EXISTS;

import co.com.dk.juanvaldez.jvsignupmc.data.domain.User;
import co.com.dk.juanvaldez.jvsignupmc.data.domain.UserValidation;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.BusinessRuleException;
import co.com.dk.juanvaldez.jvsignupmc.http.WebClientRequester;
import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Value("${spoonity.url.resource}")
    private String spoonityUrl;

    private final WebClientRequester webClientRequester;
    private Loggin logger = new Loggin();

    public SignUpService(WebClientRequester webClientRequester) {
        this.webClientRequester = webClientRequester;
    }

    public User activate(String id) {
        String uri = spoonityUrl + id;
        //logger.log("Requesting external service to ACTIVATE USER: {}", uri);

        logger.log("Requesting external service to ACTIVATE USER.");
        User apiResponse = webClientRequester
            .executeGetRequest(uri)
            .bodyToMono(User.class).block();
        logger.log("ApiResponse of ACTIVATE USER received successfully.");

        return apiResponse;
    }

    public User signUp(User createUser) throws BusinessRuleException {
        //validacion de cedula y email
        validateUserExists(createUser.getEmailAddress(), createUser.getCedula());

        //consumo API registrar usuario
        User userCreated = registerUserSpoonityAPI(createUser);

        //retorna usuario si no hay errores en el proceso
        return userCreated;
    }

    private void validateUserExists(String email, String cedula)
        throws BusinessRuleException {
        Integer vendor = 1;

        UserValidation userEmailValidation = userEmailExistsSpoonityAPI(email);
        System.out.println("//--- Vendor: " + userEmailValidation.getVendor().toString());
        if (userEmailValidation.getExists()) {
            throw new BusinessRuleException(
                String.format("Usuario con Email %1$s ya existe.", email));
        }

        Boolean cedulaExists = userCedulaExistsSpoonityAPI(cedula, vendor);
        if (cedulaExists) {
            throw new BusinessRuleException(
                String.format("Usuario con Cedula %1$s ya existe.", cedula));
        }
    }

    private UserValidation userEmailExistsSpoonityAPI(String email) {
        String parameters = "?email=" + email;
        String uri = spoonityUrl + SPOONITY_USER_EMAIL_EXISTS + parameters;

        UserValidation apiResponse = webClientRequester
            .executeGetRequest(uri)
            .bodyToMono(UserValidation.class).block();

        return apiResponse;
    }

    private boolean userCedulaExistsSpoonityAPI(String cedula, Integer vendor) {
        String parameters = "?cedula=" + cedula + "&vendor=" + vendor;
        String uri = spoonityUrl + SPOONITY_USER_CEDULA_EXISTS + parameters;

        UserValidation apiResponse = webClientRequester
            .executeGetRequest(uri)
            .bodyToMono(UserValidation.class).block();

        return apiResponse.getExists();
    }

    private User registerUserSpoonityAPI(User createUser) {
        String uri = spoonityUrl + SPOONITY_USER_REGISTER;
        //logger.log("Requesting external service to CREATE USER: {}", uri);

        logger.log("Requesting external service to CREATE USER.");
        User apiResponse = webClientRequester
            .executePostRequest(uri, createUser)
            .bodyToMono(User.class).block();
        logger.log("ApiResponse of CREATE USER received successfully.");

        return apiResponse;
    }

}
