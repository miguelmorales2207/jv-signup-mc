package co.com.dk.juanvaldez.jvsignupmc.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import co.com.dk.juanvaldez.jvsignupmc.services.ActivateUserService;
import co.com.dk.juanvaldez.jvsignupmc.services.SignUpService;
import co.com.dk.juanvaldez.jvsignupmc.utils.ObjectMapperUtils;
import co.com.dk.juanvaldez.jvsignupmc.vo.request.PhoneNumberVO;
import co.com.dk.juanvaldez.jvsignupmc.vo.request.UserVO;
import co.com.dk.juanvaldez.jvsignupmc.vo.response.StatusVO;
import co.com.dk.juanvaldez.jvsignupmc.vo.response.ValidUserVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserVOControllerTest {

    @MockBean
    private SignUpService signUpService;

    @MockBean
    private ActivateUserService activateUserService;

    @Autowired
    private MockMvc mockMvc;

    private String key = "37f5f336a367dcbf73c9eafb7b4fbcee";

    @Autowired
    private ObjectMapper objectMapper;

    private String modules = "user";

    @Test
    void test_ActivateSMS_Should_Return200_When_ValidInput() throws Exception {

        //String token = getJWT(modules, PROMOTION_ARTICLE_LIST, AUDIENCE_INTERNAL);

        //Call
        this.mockMvc.perform(get("/user/activate/sms?session_identifier=123&phone=123&country=CO")
            .header(HttpHeaders.AUTHORIZATION, key)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.error").isEmpty());

        //Mock
        verify(activateUserService).activateSMS("123", "123", "CO");
    }

    /*@Test
    void test_ActivateSMS_Should_Return401_When_ValidInput() throws Exception {

        //String token = getJWT(modules, PROMOTION_ARTICLE_LIST, AUDIENCE_INTERNAL);

        //Call
        this.mockMvc.perform(get("/user/activate/sms?session_identifier=123&phone=123&country=CO")
            .header(HttpHeaders.AUTHORIZATION, "")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
            .andDo(print())
            .andExpect(statusVO().isUnauthorized())
            .andExpect(jsonPath("$.code").value(401))
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.error").isEmpty());
    }*/

    @Test
    void test_Activate_Should_Return200_When_ValidInput() throws Exception {

        //String token = getJWT(modules, PROMOTION_ARTICLE_LIST, AUDIENCE_INTERNAL);

        //Call
        this.mockMvc.perform(get("/user/activate?token=123&session_identifier=123")
            .header(HttpHeaders.AUTHORIZATION, key)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.error").isEmpty());

        //Mock
        verify(activateUserService).activate("123", "123");
    }

    /*@Test
    void test_Activate_Should_Return401_When_ValidInput() throws Exception {

        //String token = getJWT(modules, PROMOTION_ARTICLE_LIST, AUDIENCE_INTERNAL);

        //Call
        this.mockMvc.perform(get("/user/activate?token=123&session_identifier=123")
            .header(HttpHeaders.AUTHORIZATION, "")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
            .andDo(print())
            .andExpect(statusVO().isUnauthorized())
            .andExpect(jsonPath("$.code").value(401))
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.error").isEmpty());
    }*/

    @Test
    void test_Create_Should_Return201_When_RegisterUser() throws Exception {

        //Data to return
        //String token = getJWT(modules, PermissionsConstants.CAMPAIGN_CREATE, AUDIENCE_INTERNAL);

        //Mock
        when(signUpService.signUp(getUser()))
            .thenReturn(getValidUser());

        //Call
        this.mockMvc.perform(post("/user/register")
            .header(HttpHeaders.AUTHORIZATION, key)
            .content(ObjectMapperUtils.getObjectMapper().writeValueAsBytes(getUser()))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value(201))
            .andExpect(jsonPath("$.message").value("UserVO has been created successfully."))
            .andExpect(jsonPath("$.data").isNotEmpty())
            .andExpect(jsonPath("$.error").isEmpty());

        verify(signUpService).signUp(getUser());
    }

    /*@Test
    void test_Create_Should_Return401_When_RegisterUser() throws Exception {

        //Call
        this.mockMvc.perform(post("/user/register")
            .header(HttpHeaders.AUTHORIZATION, "")
            .content(ObjectMapperUtils.getObjectMapper().writeValueAsBytes(getUser()))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
            .andDo(print())
            .andExpect(statusVO().isCreated())
            .andExpect(jsonPath("$.code").value(201))
            .andExpect(jsonPath("$.message").value("UserVO has been created successfully."))
            .andExpect(jsonPath("$.data").isNotEmpty())
            .andExpect(jsonPath("$.error").isEmpty());

        verify(signUpService).signUp(getUser());
    }*/

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
            .build();
    }

    private ValidUserVO getValidUser() {
        return ValidUserVO.builder()
            .id(1L)
            .firstName("Julito")
            .lastName("Alimaña")
            .emailAddress("Julito@email.com")
            .birthdate(Date.from(Instant.now()))
            .dateCreated(Date.from(Instant.now()))
            .dateUpdated(Date.from(Instant.now()))
            .statusVO(new StatusVO(1, "ok"))
            .build();
    }

}
