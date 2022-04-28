package co.com.dk.juanvaldez.jvsignupmc.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import co.com.dk.juanvaldez.jvsignupmc.exceptions.BusinessRuleException;
import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import co.com.dk.juanvaldez.jvsignupmc.services.ActivateUserService;
import co.com.dk.juanvaldez.jvsignupmc.services.SignUpService;
import co.com.dk.juanvaldez.jvsignupmc.vo.ApiResponseVO;
import co.com.dk.juanvaldez.jvsignupmc.data.domain.User;
import co.com.dk.juanvaldez.jvsignupmc.vo.responseAPI.ValidUser;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping(value = "/activate", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseVO<ValidUser>> activate(
        @RequestParam(value = "session_identifier", required = true) String sessionId,
        @RequestParam(value = "phone", required = false) Integer phone,
        @RequestParam(value = "country", required = false) String country) {

        logger.log("Start USER activation process from controller.");
        ValidUser userValidated = activateUserService.activate(sessionId, phone, country);
        logger.log("USER activated successfully from controller.");

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponseVO.<ValidUser>builder()
                .code(HttpStatus.OK.value())
                .message("User has been activated successfully.")
                .data(userValidated)
                .build());
    }

    @PostMapping(value = "/signup", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseVO<User>> signUp(@Valid @RequestBody User createUser)
        throws BusinessRuleException {
        logger.log("Start USER register process from controller.");
        User userCreated = signUpService.signUp(createUser);
        logger.log("USER registered successfully from controller.");

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponseVO.<User>builder()
                .code(HttpStatus.CREATED.value())
                .message("User has been created successfully.")
                .data(userCreated)
                .build());
    }

}
