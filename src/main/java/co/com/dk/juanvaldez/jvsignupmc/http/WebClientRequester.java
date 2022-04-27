package co.com.dk.juanvaldez.jvsignupmc.http;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import co.com.dk.juanvaldez.jvsignupmc.data.domain.requestUser.RequestUserBody;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.SSOWebClientErrorHandler;
import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import co.com.dk.juanvaldez.jvsignupmc.security.UserSecurity;
import co.com.dk.juanvaldez.jvsignupmc.exceptions.WebClientErrorHandler;
import co.com.dk.juanvaldez.jvsignupmc.utils.JwtTokenUtils;
import co.com.dk.juanvaldez.jvsignupmc.vo.SSOAuthRequestVO;
import co.com.dk.juanvaldez.jvsignupmc.vo.SSOAuthResponseVO;
import co.com.dk.juanvaldez.jvsignupmc.vo.SSOResponseVO;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

@Repository
public class WebClientRequester {

    @Value("${sso.auth.url}")
    private String authUrl;

    @Value("${sso.auth.client.key}")
    private String clientKey;

    @Value("${sso.auth.client.secret}")
    private String clientSecret;

    @Value("${sso.auth.client.issuer}")
    private String clientIssuer;

    @Value("${sso.token.cache.key}")
    private String tokenCacheKey;

    @Value("${jwt.token.secret}")
    private String jwtSecret;

    @Value("${sso.time.subtract.ttl.in.minutes}")
    private String ttlInMinutes;

    public static final String PREFIX_TOKEN = "Bearer ";

    private final WebClient webClient;
    //private final ValueOperations<String, String> valueOperations;
    private final ModelMapper modelMapper;
    private Loggin logger = new Loggin();

    public WebClientRequester(//ValueOperations<String, String> valueOperations,
        WebClient webClient,
        ModelMapper modelMapper) {
        //this.valueOperations = valueOperations;
        this.webClient = webClient;
        this.modelMapper = modelMapper;
    }

    public ResponseSpec executeGetRequest(String uri) {
        String token = getToken();

        return webClient.get()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, token)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::isError, WebClientErrorHandler::manageError);
    }

    public ResponseSpec executePostRequest(String uri, RequestUserBody requestUser) {
        requestUser.setUser(getAuthenticatedUser());

        String token = getToken();

        return webClient.post()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, token)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(requestUser), requestUser.getClass())
            .retrieve()
            .onStatus(HttpStatus::isError, WebClientErrorHandler::manageError);
    }

    private String getToken() {

        /*log.trace("Getting cache token with key {}", tokenCacheKey);
        String token = valueOperations.get(tokenCacheKey);
        if (token != null) {
            log.trace("With this key {} was obtained this token {} from cache successfully.",
            tokenCacheKey, token);
            return PREFIX_TOKEN.concat(token);
        }*/
        //log.trace("With this key {} was not found token in cache.", tokenCacheKey);

        //log.trace("Getting token from this url {} ", authUrl);

        SSOAuthResponseVO ssoAuthResponseVO = authenticate();
        //String cacheToken = this.saveCache(ssoAuthResponseDTO, tokenCacheKey);
        //return PREFIX_TOKEN.concat(cacheToken);
        return PREFIX_TOKEN.concat(ssoAuthResponseVO.getAccessToken());
    }

    private SSOAuthResponseVO authenticate() {

        SSOAuthRequestVO ssoAuthRequestDTO = SSOAuthRequestVO.builder()
            .issuer(clientIssuer)
            .authKey(clientKey)
            .authSecret(clientSecret)
            .build();

        SSOResponseVO<SSOAuthResponseVO> ssoResponseDTO = webClient.post()
            .uri(authUrl)
            .accept(APPLICATION_JSON)
            .body(Mono.just(ssoAuthRequestDTO), ssoAuthRequestDTO.getClass())
            .retrieve()
            .onStatus(HttpStatus::isError, SSOWebClientErrorHandler::manageError)
            .bodyToMono(new ParameterizedTypeReference
                <SSOResponseVO<SSOAuthResponseVO>>() {
            }).block();

        //log.trace("ssoResponseDTO: {}", ssoResponseDTO);

        return ssoResponseDTO.getResult();
    }

    /*private String saveCache(SSOAuthResponseVO ssoAuthResponseDTO, String redisKey) {

        Long expiresIn = JwtTokenUtils
            .getExpiration(ssoAuthResponseDTO.getAccessToken(), jwtSecret);

        LocalDateTime expirationDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(
            expiresIn), TimeZone.getDefault().toZoneId()
        );

        Duration diff = Duration.between(LocalDateTime.now(), expirationDate)
            .minusMinutes(Long.parseLong(ttlInMinutes));

        //log.trace("Set value in Redis with: duration={}, key={}, value={}.",
        //    diff.getSeconds(), redisKey, ssoAuthResponseDTO.getAccessToken());

        valueOperations.set(redisKey, ssoAuthResponseDTO.getAccessToken(), diff);

        return ssoAuthResponseDTO.getAccessToken();
    }*/

    private co.com.dk.juanvaldez.jvsignupmc.data.domain.requestUser.User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.log("Getting authenticated USER from or token.");
        UserSecurity userSecurity = (UserSecurity) authentication.getPrincipal();
        //log.trace("UserSecurity obtained successfully from or token. {}", userSecurity);
        logger.log("USER obtained successfully from or token.");

        return modelMapper
            .map(userSecurity, co.com.dk.juanvaldez.jvsignupmc.data.domain.requestUser.User.class);
    }

}
