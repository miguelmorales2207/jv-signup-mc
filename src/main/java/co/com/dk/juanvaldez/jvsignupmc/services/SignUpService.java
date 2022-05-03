package co.com.dk.juanvaldez.jvsignupmc.services;

import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.SPOONITY_USER_MOBILE_EXISTS;
import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.SPOONITY_USER_REGISTER;
import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.SPOONITY_USER_EMAIL_EXISTS;
import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.SPOONITY_USER_CEDULA_EXISTS;

import co.com.dk.juanvaldez.jvsignupmc.vo.request.User;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.SignUpMCRestException;
import co.com.dk.juanvaldez.jvsignupmc.vo.response.UserValidation;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.BusinessRuleException;
import co.com.dk.juanvaldez.jvsignupmc.http.WebClientRequester;
import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import co.com.dk.juanvaldez.jvsignupmc.vo.response.ValidUser;
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

    public ValidUser signUp(User createUser) throws BusinessRuleException {

        logger.log("Validate if the USER email or cedula exists.");
        validateUserExists(createUser);
        logger.log("Email and cedula does not exists, continuous the USER register process.");

        logger.log("Creating new USER...");
        ValidUser userCreated = registerUserSpoonityApi(createUser);
        logger.log("New USER has been created.");

        return userCreated;
    }

    private void validateUserExists(User user) throws BusinessRuleException {
        logger.log("Validate if the USER email exists.");
        boolean userValidation = userEmailExistsSpoonityApi(user.getEmailAddress());
        if (userValidation) {
            throw new BusinessRuleException(
                String.format("Usuario con Email %1$s ya existe.", user.getEmailAddress()));
        }

        logger.log("Validate if the USER cedula exists.");
        boolean cedulaExists = userCedulaExistsSpoonityApi(user.getCedula(), user.getVendor());
        if (cedulaExists) {
            throw new BusinessRuleException(
                String.format("Usuario con Cedula %1$s ya existe.", user.getCedula()));
        }

        logger.log("Validate if the USER mobile exists.");
        boolean mobileExists = userMobileExistsSpoonityApi(user.getVendor(),
            user.getPhoneNumber().getNumber());
        if (mobileExists) {
            throw new BusinessRuleException(
                String.format("Usuario con Mobil %1$s ya existe.",
                    user.getPhoneNumber().getNumber()));
        }

    }

    private boolean userEmailExistsSpoonityApi(String email) throws SignUpMCRestException {
        String parameters = "?email=" + email;
        String uri = spoonityUrl + SPOONITY_USER_EMAIL_EXISTS + parameters;

        logger.log(String.format("Requesting external service to VALIDATE USER EMAIL:{}", uri));
        UserValidation apiResponse = webClientRequester
            .executeGetRequest(uri)
            .bodyToMono(UserValidation.class)
            .block();
        logger.log("API Response of VALIDATE USER EMAIL received successfully.");

        return apiResponse.isExists();
    }

    private boolean userCedulaExistsSpoonityApi(String cedula, Integer vendor)
        throws SignUpMCRestException {
        String parameters = "?cedula=" + cedula + "&vendor=" + vendor;
        String uri = spoonityUrl + SPOONITY_USER_CEDULA_EXISTS + parameters;

        logger.log(String.format("Requesting external service to VALIDATE USER CEDULA:{}", uri));
        UserValidation apiResponse = webClientRequester
            .executeGetRequest(uri)
            .bodyToMono(UserValidation.class)
            .block();
        logger.log("API Response of VALIDATE USER CEDULA received successfully.");

        return apiResponse.isExists();
    }

    private boolean userMobileExistsSpoonityApi(Integer vendor, String phone)
        throws SignUpMCRestException {
        String parameters = "?vendor=" + vendor + "&mobile=" + phone;
        String uri = spoonityUrl + SPOONITY_USER_MOBILE_EXISTS + parameters;

        logger.log(String.format("Requesting external service to VALIDATE USER MOBILE:{}", uri));
        UserValidation apiResponse = webClientRequester
            .executeGetRequest(uri)
            .bodyToMono(UserValidation.class)
            .block();
        logger.log("API Response of VALIDATE USER MOBILE received successfully.");

        return apiResponse.isExists();
    }

    private ValidUser registerUserSpoonityApi(User createUser) throws SignUpMCRestException {
        String uri = spoonityUrl + SPOONITY_USER_REGISTER;

        logger.log(String.format("Requesting external service to CREATE USER:{}", uri));
        ValidUser apiResponse = webClientRequester
            .executePostRequest(uri, createUser)
            .bodyToMono(ValidUser.class)
            .block();
        logger.log("API Response of CREATE USER received successfully.");

        return apiResponse;
    }

}
