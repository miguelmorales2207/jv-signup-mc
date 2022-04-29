package co.com.dk.juanvaldez.jvsignupmc.services;

import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.SPOONITY_USER_REGISTER;
import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.SPOONITY_USER_EMAIL_EXISTS;
import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.SPOONITY_USER_CEDULA_EXISTS;

import co.com.dk.juanvaldez.jvsignupmc.data.domain.User;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.SignUpMCException;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.SignUpMCRestException;
import co.com.dk.juanvaldez.jvsignupmc.vo.responseAPI.UserValidation;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.BusinessRuleException;
import co.com.dk.juanvaldez.jvsignupmc.http.WebClientRequester;
import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import co.com.dk.juanvaldez.jvsignupmc.vo.responseAPI.ValidUser;
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

    public ValidUser signUp(User createUser) throws BusinessRuleException, SignUpMCException {

        //logger.log("Validate if the USER email or cedula exists.");
        //validateUserExists(createUser);
        //logger.log("Email and cedula does not exists, continuous the USER register process.");

        logger.log("Creating new USER...");
        ValidUser userCreated = registerUserSpoonityAPI(createUser);
        logger.log("New USER has been created.");

        return userCreated;
    }

    private void validateUserExists(User user) throws BusinessRuleException {
        logger.log("Validate if the USER email exists.");
        UserValidation userValidation = userEmailExistsSpoonityAPI(user.getEmailAddress());
        if (userValidation.getExists()) {
            throw new BusinessRuleException(
                String.format("Usuario con Email %1$s ya existe.", user.getEmailAddress()));
        }

        logger.log("Validate if the USER cedula exists.");
        Boolean cedulaExists = userCedulaExistsSpoonityAPI(user.getCedula(), user.getVendor());
        if (cedulaExists) {
            throw new BusinessRuleException(
                String.format("Usuario con Cedula %1$s ya existe.", user.getCedula()));
        }
    }

    private UserValidation userEmailExistsSpoonityAPI(String email) throws SignUpMCRestException {
        String parameters = "?email=" + email;
        String uri = spoonityUrl + SPOONITY_USER_EMAIL_EXISTS + parameters;

        logger.log("Requesting external service to VALIDATE USER EMAIL.");
        UserValidation apiResponse = webClientRequester
            .executeGetRequest(uri)
            .bodyToMono(UserValidation.class).block();
        logger.log("API Response of VALIDATE USER EMAIL received successfully.");

        return apiResponse;
    }

    private boolean userCedulaExistsSpoonityAPI(String cedula, Integer vendor)
        throws SignUpMCRestException {
        String parameters = "?cedula=" + cedula + "&vendor=" + vendor;
        String uri = spoonityUrl + SPOONITY_USER_CEDULA_EXISTS + parameters;

        logger.log("Requesting external service to VALIDATE USER CEDULA.");
        UserValidation apiResponse = webClientRequester
            .executeGetRequest(uri)
            .bodyToMono(UserValidation.class).block();
        logger.log("API Response of VALIDATE USER CEDULA received successfully.");

        return apiResponse.getExists();
    }

    private ValidUser registerUserSpoonityAPI(User createUser) throws SignUpMCRestException {
        String uri = spoonityUrl + SPOONITY_USER_REGISTER;
        //logger.log("Requesting external service to CREATE USER: {}", uri);

        logger.log("Requesting external service to CREATE USER.");
        ValidUser apiResponse = webClientRequester
            .executePostRequest(uri, createUser)
            .bodyToMono(ValidUser.class).block();
        logger.log("API Response of CREATE USER received successfully.");

        return apiResponse;
    }

}
