package co.com.dk.juanvaldez.jvsignupmc.exceptions.handlers;

import co.com.dk.juanvaldez.jvsignupmc.exceptions.BusinessRuleException;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.SignUpMCException;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.SignUpMCRestException;
import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import com.fasterxml.jackson.core.JsonParseException;
import co.com.dk.juanvaldez.jvsignupmc.vo.ApiResponseVO;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.client.WebClientException;

@ControllerAdvice
public class RestControllerExceptionHandler {

    private Loggin logger = new Loggin();

    @ExceptionHandler(value = BusinessRuleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ApiResponseVO<Object>> handleBusinessRuleException(
        BusinessRuleException ex) {
        logger.log(String.format(ex.getMessage(), ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponseVO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .error(Collections.singletonList(ex.getMessage()))
                .build());
    }

    @ExceptionHandler(value = SignUpMCException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ApiResponseVO<Object>> handleException(Exception ex) {
        logger.log(String.format(ex.getMessage(), ex));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponseVO.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .error(Collections.singletonList(ex.getMessage()))
                .build());
    }

    @ExceptionHandler(value = WebClientException.class)
    protected ResponseEntity<ApiResponseVO<Object>> handleWebClientException(
        WebClientException webClientException) {
        logger.log(String.format(webClientException.getMessage(), webClientException));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponseVO.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .error(Collections.singletonList(webClientException.getMessage()))
                .build());
    }

    @ExceptionHandler(value = SignUpMCRestException.class)
    protected ResponseEntity<ApiResponseVO<Object>> handleOrchMenuRestException(
        SignUpMCRestException signUpMCRestException) {
        logger.log(String.format(signUpMCRestException.getMessage(), signUpMCRestException));
        return ResponseEntity.status(signUpMCRestException.getHttpStatusCode())
            .body(ApiResponseVO.builder()
                .code(signUpMCRestException.getHttpStatusCode())
                .message(signUpMCRestException.getMessage())
                .error(signUpMCRestException.getErrors())
                .build());
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponseVO<Object>> handleUnauthorized(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponseVO.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message(HttpStatus.FORBIDDEN.getReasonPhrase())
                .error(Collections.singletonList(ex.getMessage()))
                .build()
            );
    }

    @ExceptionHandler(value = {IOException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ApiResponseVO<Object>> handleIOException(IOException ex) {
        logger.log(String.format(ex.getMessage(), ex));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponseVO.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .error(Collections.singletonList(ex.getMessage()))
                .build()
            );
    }

    @ExceptionHandler(value = {ServletException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ApiResponseVO<Object>> handleServletException(ServletException ex) {
        logger.log(String.format(ex.getMessage(), ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponseVO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .error(Collections.singletonList(ex.getMessage()))
                .build()
            );
    }

    @ExceptionHandler(value = {JsonParseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ApiResponseVO<Object>> handleJsonParseException(
        JsonParseException ex) {
        logger.log(String.format(ex.getMessage(), ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponseVO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .error(Collections.singletonList(ex.getMessage()))
                .build()
            );
    }

}

