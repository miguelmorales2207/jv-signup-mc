package co.com.dk.juanvaldez.jvsignupmc.exceptions.handlers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import co.com.dk.juanvaldez.jvsignupmc.exceptions.SignUpMCRestException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class WebClientErrorHandlerTest {

    @Mock
    private ClientResponse clientResponse;

    @Test
    void test_ManageError_Should_ReturnOrchOrderRestException_When_Invoked() {
        when(clientResponse.bodyToMono(Map.class))
            .thenAnswer(i -> {
                Map<String, Object> map = new LinkedHashMap();
                map.put("code", 400);
                map.put("error", "Error on client");
                map.put("message", "Message");
                return Mono.just(map);
            });

        Mono<SignUpMCRestException> result = WebClientErrorHandler.manageError(clientResponse);
        assertNotNull(result);
    }

}
