package co.com.dk.juanvaldez.jvsignupmc.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import co.com.dk.juanvaldez.jvsignupmc.exceptions.BusinessRuleException;
import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import co.com.dk.juanvaldez.jvsignupmc.services.ActivateUserService;
import co.com.dk.juanvaldez.jvsignupmc.services.SignUpService;
import co.com.dk.juanvaldez.jvsignupmc.vo.ApiResponseVO;
import co.com.dk.juanvaldez.jvsignupmc.vo.request.UserVO;
import co.com.dk.juanvaldez.jvsignupmc.vo.response.ValidUserVO;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final SignUpService signUpService;
    private final ActivateUserService activateUserService;
    private Loggin logger = new Loggin();

    public UserController(SignUpService signUpService, ActivateUserService activateUserService) {
        this.signUpService = signUpService;
        this.activateUserService = activateUserService;
    }

    @GetMapping(value = "/activate/sms", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseVO> activateSMS(
        @RequestParam(value = "session_identifier", required = true) String sessionId,
        @RequestParam(value = "phone", required = true) String phone,
        @RequestParam(value = "country", required = true) String country) {
        logger.log("Start the process of sending the verification code for USER activation.");
        activateUserService.activateSMS(sessionId, phone, country);
        logger.log("Successful sending of verification code for USER activation.");

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponseVO.builder()
                .code(HttpStatus.OK.value())
                .message("Verification code has been sent successfully.")
                .build());
    }

    @GetMapping(value = "/activate", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseVO> activate(
        @RequestParam(value = "token", required = true) String token,
        @RequestParam(value = "session_identifier", required = true) String sessionId) {
        logger.log("Start USER activation process.");
        activateUserService.activate(token, sessionId);
        logger.log("USER activated successfully.");

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponseVO.builder()
                .code(HttpStatus.OK.value())
                .message("User has been activated successfully.")
                .build());
    }

    @PostMapping(value = "/register", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseVO<ValidUserVO>> signUp(@Valid @RequestBody UserVO createUser)
        throws BusinessRuleException {
        logger.log("Start USER register process.");
        ValidUserVO userCreated = signUpService.signUp(createUser);
        logger.log("USER registered successfully.");

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponseVO.<ValidUserVO>builder()
                .code(HttpStatus.CREATED.value())
                .message("User has been created successfully.")
                .data(userCreated)
                .build());
    }

}
