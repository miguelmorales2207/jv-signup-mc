package co.com.dk.juanvaldez.jvsignupmc.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import co.com.dk.juanvaldez.jvsignupmc.exceptions.BusinessRuleException;
import co.com.dk.juanvaldez.jvsignupmc.http.WebClientRequester;
import co.com.dk.juanvaldez.jvsignupmc.vo.request.PhoneNumberVO;
import co.com.dk.juanvaldez.jvsignupmc.vo.request.UserVO;
import co.com.dk.juanvaldez.jvsignupmc.vo.response.UserValidationVO;
import co.com.dk.juanvaldez.jvsignupmc.vo.response.ValidUserVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class SignUpServiceTest {

    @InjectMocks
    private SignUpService signUpService;

    @Mock
    private WebClientRequester webClientRequester;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec2;

    @Mock
    private WebClient.ResponseSpec responseSpec3;

    @Test
    void test_RegisterUser_Should_CreateUser_When_ValidInput() {

        String parameterUri = "null/user/email/exists?email=Julito@email.com";
        String parameterUri2 = "null/user/cedula/exists?cedula=1234567890&vendor=1";
        String parameterUri3 = "null/user/mobile/exists?vendor=1&mobile=3004005060";
        UserValidationVO userValidationVO = new UserValidationVO();
        ValidUserVO validUserVO = new ValidUserVO();

        when(webClientRequester.executeGetRequest(parameterUri))
            .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserValidationVO.class))
            .thenReturn(Mono.just(userValidationVO));

        when(webClientRequester.executeGetRequest(parameterUri2))
            .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserValidationVO.class))
            .thenReturn(Mono.just(userValidationVO));

        when(webClientRequester.executeGetRequest(parameterUri3))
            .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserValidationVO.class))
            .thenReturn(Mono.just(userValidationVO));

        when(webClientRequester.executePostRequest("null/user/register", getUser()))
            .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ValidUserVO.class))
            .thenReturn(Mono.just(validUserVO));

        assertAll(() -> signUpService.signUp(getUser()));
    }

    @Test
    void test_RegisterUser_Should_FailCreateUser_When_ParameterEmailAlreadyExists() {

        String parameterUri = "null/user/email/exists?email=Julito@email.com";
        UserValidationVO userValidationVO = new UserValidationVO();
        userValidationVO.setExists(true);

        when(webClientRequester.executeGetRequest(parameterUri))
            .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserValidationVO.class))
            .thenReturn(Mono.just(userValidationVO));

        assertThrows(BusinessRuleException.class,
            () -> signUpService.signUp(getUser()));
    }

    @Test
    void test_RegisterUser_Should_FailCreateUser_When_ParameterCedulaAlreadyExists() {

        String parameterUri = "null/user/email/exists?email=Julito@email.com";
        String parameterUri2 = "null/user/cedula/exists?cedula=1234567890&vendor=1";

        UserValidationVO userValidationVO = new UserValidationVO();
        userValidationVO.setExists(false);
        when(webClientRequester.executeGetRequest(parameterUri))
            .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserValidationVO.class))
            .thenReturn(Mono.just(userValidationVO));

        UserValidationVO userValidationVO2 = new UserValidationVO();
        userValidationVO2.setExists(true);
        when(webClientRequester.executeGetRequest(parameterUri2))
            .thenReturn(responseSpec2);
        when(responseSpec2.bodyToMono(UserValidationVO.class))
            .thenReturn(Mono.just(userValidationVO2));

        assertThrows(BusinessRuleException.class,
            () -> signUpService.signUp(getUser()));
    }

    @Test
    void test_RegisterUser_Should_FailCreateUser_When_ParameterMobileAlreadyExists() {

        String parameterUri = "null/user/email/exists?email=Julito@email.com";
        String parameterUri2 = "null/user/cedula/exists?cedula=1234567890&vendor=1";
        String parameterUri3 = "null/user/mobile/exists?vendor=1&mobile=3004005060";

        UserValidationVO userValidationVO = new UserValidationVO();
        userValidationVO.setExists(false);
        when(webClientRequester.executeGetRequest(parameterUri))
            .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserValidationVO.class))
            .thenReturn(Mono.just(userValidationVO));

        UserValidationVO userValidationVO2 = new UserValidationVO();
        userValidationVO2.setExists(false);
        when(webClientRequester.executeGetRequest(parameterUri2))
            .thenReturn(responseSpec2);
        when(responseSpec2.bodyToMono(UserValidationVO.class))
            .thenReturn(Mono.just(userValidationVO2));

        UserValidationVO userValidationVO3 = new UserValidationVO();
        userValidationVO3.setExists(true);
        when(webClientRequester.executeGetRequest(parameterUri3))
            .thenReturn(responseSpec3);
        when(responseSpec3.bodyToMono(UserValidationVO.class))
            .thenReturn(Mono.just(userValidationVO3));

        assertThrows(BusinessRuleException.class,
            () -> signUpService.signUp(getUser()));
    }

    private UserVO getUser() {
        return UserVO.builder()
            .firstName("Julito")
            .lastName("Alimaña")
            .anonymous(false)
            .emailAddress("Julito@email.com")
            .password("Spoonity5")
            .terms(true)
            .vendor(1)
            .phoneNumberVO(new PhoneNumberVO("57", "3004005060"))
            .birthdate(27)
            .cedula("1234567890")
            .build();
    }

}
