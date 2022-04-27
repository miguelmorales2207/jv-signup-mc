package co.com.dk.juanvaldez.jvsignupmc.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import co.com.dk.juanvaldez.jvsignupmc.data.domain.Prestamo;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.BusinessRuleException;
import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import co.com.dk.juanvaldez.jvsignupmc.services.SignUpService;
import co.com.dk.juanvaldez.jvsignupmc.vo.ApiResponseVO;
import co.com.dk.juanvaldez.jvsignupmc.data.domain.User;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final SignUpService signUpService;
    private Loggin logger = new Loggin();

    public UserController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @GetMapping(value = "/activate/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseVO<Prestamo>> activate(@PathVariable String id) {
        logger.log("Start USER activation process.");
        Prestamo prestamo = signUpService.activate(id);
        logger.log("USER activated successfully from service.");

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponseVO.<Prestamo>builder()
                .code(HttpStatus.OK.value())
                .message("UserSecurity has been successfully activated.")
                .data(prestamo)
                .build());
    }

    @PostMapping(value = "/signup", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseVO<User>> signUp(@Valid @RequestBody User createUser)
        throws BusinessRuleException {
        logger.log("Start USER creation process for signup.");
        User userCreated = signUpService.signUp(createUser);
        logger.log("USER created successfully from service.");

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponseVO.<User>builder()
                .code(HttpStatus.CREATED.value())
                .message("UserSecurity has been created successfully.")
                .data(userCreated)
                .build());
    }

}
