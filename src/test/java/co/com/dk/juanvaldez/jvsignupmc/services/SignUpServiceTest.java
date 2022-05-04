package co.com.dk.juanvaldez.jvsignupmc.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import co.com.dk.juanvaldez.jvsignupmc.http.WebClientRequester;
import co.com.dk.juanvaldez.jvsignupmc.vo.request.PhoneNumber;
import co.com.dk.juanvaldez.jvsignupmc.vo.request.User;
import co.com.dk.juanvaldez.jvsignupmc.vo.response.UserValidation;
import co.com.dk.juanvaldez.jvsignupmc.vo.response.ValidUser;
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

    @Test
    void test_RegisterUser_Should_CreateUser_When_ValidInput() {

        String parameterUri = "null/user/email/exists?email=Julito@email.com";
        String parameterUri2 = "null/user/cedula/exists?cedula=1234567890&vendor=1";
        String parameterUri3 = "null/user/mobile/exists?vendor=1&mobile=3004005060";
        UserValidation userValidation = new UserValidation();
        ValidUser validUser = new ValidUser();

        when(webClientRequester.executeGetRequest(parameterUri))
            .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserValidation.class))
            .thenReturn(Mono.just(userValidation));

        when(webClientRequester.executeGetRequest(parameterUri2))
            .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserValidation.class))
            .thenReturn(Mono.just(userValidation));

        when(webClientRequester.executeGetRequest(parameterUri3))
            .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserValidation.class))
            .thenReturn(Mono.just(userValidation));

        when(webClientRequester.executePostRequest("null/user/register", getUser()))
            .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ValidUser.class))
            .thenReturn(Mono.just(validUser));

        assertAll(() -> signUpService.signUp(getUser()));
    }

    private User getUser() {
        return User.builder()
            .firstName("Julito")
            .lastName("Alimaña")
            .anonymous(false)
            .emailAddress("Julito@email.com")
            .password("Spoonity5")
            .terms(true)
            .vendor(1)
            .phoneNumber(new PhoneNumber("57", "3004005060"))
            .birthdate(27)
            .cedula("1234567890")
            .build();
    }

}
