package co.com.dk.juanvaldez.jvsignupmc.exceptions.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import co.com.dk.juanvaldez.jvsignupmc.vo.ApiResponseVO;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.SignUpMCRestException;
import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ExtendWith(MockitoExtension.class)
class RestExceptionHandlerTest {

    @InjectMocks
    private RestControllerExceptionHandler restControllerExceptionHandler;

    @Test
    void test_HandlerWebClientException_Should_ReturnApiResponse500Code_When_Invoked() {
        HttpHeaders headers = new HttpHeaders();
        WebClientResponseException webClientException = new WebClientResponseException(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", headers, null, null);

        ResponseEntity<ApiResponseVO<Object>> exception
            = restControllerExceptionHandler.handleWebClientException(webClientException);

        assertNotNull(exception.getBody());
    }

    @Test
    void test_HandlerOrchMenuRestException_Should_ReturnApiResponse400Code_When_Invoked() {
        SignUpMCRestException orchMenuRestException = new SignUpMCRestException(
            "Error", HttpStatus.BAD_REQUEST.value(), null, null);

        ResponseEntity<ApiResponseVO<Object>> exception
            = restControllerExceptionHandler.handleSignUpMCRestException(orchMenuRestException);

        assertNotNull(exception.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void test_HandlerUnauthorized_Should_ReturnApiResponse403Code_When_Invoked() {
        AccessDeniedException exceptionInvoke = new AccessDeniedException("Error");
        ResponseEntity<ApiResponseVO<Object>> exception =
            restControllerExceptionHandler.handleUnauthorized(exceptionInvoke);

        assertNotNull(exception.getBody());
        assertEquals(HttpStatus.FORBIDDEN.value(), exception.getBody().getCode());
        assertEquals("Forbidden", exception.getBody().getMessage());
    }

    @Test
    void test_HandleIOException_Should_ReturnApiResponse500Code_When_Invoked() {
        IOException exceptionInvoke = new IOException("Error", new Throwable());

        ResponseEntity<ApiResponseVO<Object>> exception =
            restControllerExceptionHandler.handleIOException(exceptionInvoke);

        assertNotNull(exception.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getBody().getCode());
        assertEquals("Internal Server Error", exception.getBody().getMessage());
    }

    @Test
    void test_RestControllerExceptionHandler_Should_ReturnApiResponse400Code_When_Invoked() {
        ServletException exceptionInvoke = new ServletException("Error", new Throwable());

        ResponseEntity<ApiResponseVO<Object>> exception =
            restControllerExceptionHandler.handleServletException(exceptionInvoke);

        assertNotNull(exception.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getBody().getCode());
        assertEquals("Bad Request", exception.getBody().getMessage());
    }

}
