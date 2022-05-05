package co.com.dk.juanvaldez.jvsignupmc.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import co.com.dk.juanvaldez.jvsignupmc.http.WebClientRequester;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class ActivateUserVOServiceTest {

    @InjectMocks
    private ActivateUserService activateUserService;

    @Mock
    private WebClientRequester webClientRequester;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Test
    void test_ActivateSMS_Should_SendVerificationCodeEmail_When_ValidInput() {

        String parameterUri = "null/user/activate/sms?session_identifier=x&phone=y&country=z";
        Object response = new Object();

        when(webClientRequester.executeGetRequest(parameterUri))
            .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class))
            .thenReturn(Mono.just(response));

        assertAll(() -> activateUserService.activateSMS("x", "y", "z"));
    }

    @Test
    void test_Activate_Should_ActivateUser_When_ValidInput() {

        String parameterUri = "null/user/activate?token=x&session_identifier=y";
        Object response = new Object();

        when(webClientRequester.executeGetRequest(parameterUri))
            .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class))
            .thenReturn(Mono.just(response));

        assertAll(() -> activateUserService.activate("x", "y"));
    }

}
