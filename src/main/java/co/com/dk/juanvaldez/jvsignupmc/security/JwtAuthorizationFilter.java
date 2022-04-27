package co.com.dk.juanvaldez.jvsignupmc.security;

import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.AUDIENCE_INTERNAL;
import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.AUDIENCE_PUBLIC;
import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.AUDIENCE_SERVICE;
import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.BEARER;
import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.UNAUTHORIZED_REQUEST;
import static co.com.dk.juanvaldez.jvsignupmc.constants.WebURIConstants.USER_SERVICE;
import static co.com.dk.juanvaldez.jvsignupmc.constants.PermissionsConstants.PUBLIC;
import static co.com.dk.juanvaldez.jvsignupmc.constants.PermissionsConstants.SERVICE;

import co.com.dk.juanvaldez.jvsignupmc.loggin.Loggin;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.com.dk.juanvaldez.jvsignupmc.vo.ApiResponseVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String ROLE = "ROLE_";

    private final Environment environment;

    private Loggin logger = new Loggin();

    public JwtAuthorizationFilter(@NotNull final Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {

        if (!existJwtToken(request)) {
            logger.log("Token does not exist.");
            denyRequest(response);
            return;
        }

        logger.log("Starting authentication process...");

        logger.log("Getting JWT token from request.");
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION)
            .substring(BEARER.length());
        //logger.log("JWT token: {}", jwtToken);

        try {
            logger.log("Getting claims from JWT");
            Claims claims = getClaims(jwtToken);
            logger.log("Claims obtained successfully");

            if (!isValidClaims(claims)) {
                denyRequest(response);
                return;
            }

            logger.log("Token is valid.");
            logger.log("Authenticate User");
            UsernamePasswordAuthenticationToken auth = authenticateUser(claims);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
            logger.log("Authenticated user successfully");

        } catch (JwtException e) {
            //logger.log(e.getMessage(), e);
            denyRequest(response);
            return;
        }

        filterChain.doFilter(request, response);
        logger.log("Authentication process ended successfully.");
    }


    private Claims getClaims(String token) {
        String tokenSecret = environment.getRequiredProperty("jwt.token.secret");
        return Jwts.parser().setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8))
            .parseClaimsJws(token).getBody();
    }

    private boolean isValidClaims(Claims claims) {

        //logger.log("claims.getAudience(): {}", claims.getAudience());

        if (AUDIENCE_SERVICE.equals(claims.getAudience()) ||
            AUDIENCE_PUBLIC.equals(claims.getAudience())) {
            return isValidAudience(claims);
        } else {
            if (isValidUser(claims) && isValidModules(claims)) {
                return this.isValidAudience(claims);
            }
        }

        return false;
    }

    private boolean isValidAudience(Claims claims) {

        logger.log("Getting system audiences.");
        String tokenAudValues = environment.getRequiredProperty("jwt.token.aud");
        List<String> tokenAudValidValues = Arrays.asList(tokenAudValues.split(","));
        //logger.log("Audiences obtained successfully: {}", tokenAudValues);

        logger.log("Getting token audience.");
        String tokenAud = claims.getAudience();
        //logger.log("Token audience obtained successfully: {}", tokenAud);

        logger.log("Validating audience");
        if (tokenAud == null || !tokenAudValidValues.contains(tokenAud)) {
            logger.log("Audience no valid");
            return false;
        }
        logger.log("Valid audience");
        return true;
    }

    private boolean isValidModules(Claims claims) {
        logger.log("Getting system modules.");
        String tokenModValidValue = environment.getRequiredProperty("jwt.token.mod");
        List<String> listMods = Arrays.asList(tokenModValidValue.split(","));
        //logger.log("Modules obtained successfully: {}", tokenModValidValue);

        logger.log("Getting token modules.");
        List<String> tokenMods = claims.get("mod", ArrayList.class);
        //logger.log("Token modules obtained successfully: {}", tokenMods);

        logger.log("Validating modules");
        if (tokenMods == null || tokenMods.stream().noneMatch(listMods::contains)) {
            logger.log("Modules no valid");
            return false;
        }
        logger.log("Valid modules");
        return true;
    }

    private boolean isValidUser(Claims claims) {
        logger.log("Getting token user.");
        Map<String, Object> user = claims.get("user", Map.class);
        if (user == null || user.get("user_id") == null) {
            logger.log("User objet no valid");
            return false;
        }
        //logger.log("Token user obtained successfully: {}", user);
        return true;
    }

    private boolean existJwtToken(@NotNull final HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return !(authHeader == null || !authHeader.startsWith(BEARER));
    }

    public UsernamePasswordAuthenticationToken authenticateUser(Claims claims) {
        Set<SimpleGrantedAuthority> grantedValues = getGrantedValues(claims);
        Map<String, Object> tokenUser = claims.get("user", Map.class);

        UserSecurity user = UserSecurity.builder()
            .id(USER_SERVICE)
            .build();

        if (tokenUser != null) {
            user = getUserDataFromTokenUser(tokenUser);
        }

        user.setRoles(grantedValues);

        return new UsernamePasswordAuthenticationToken(user, "", grantedValues);
    }

    private static Set<SimpleGrantedAuthority> getGrantedValues(Claims claims) {

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        String audience = claims.getAudience();

        //logger.log("Audience to granted values: {}", audience);

        switch (audience) {

            case AUDIENCE_SERVICE:
                authorities.add(new SimpleGrantedAuthority(
                    ROLE + SERVICE.toUpperCase(Locale.getDefault())));
                break;

            case AUDIENCE_PUBLIC:
                authorities.add(new SimpleGrantedAuthority(
                    ROLE + PUBLIC.toUpperCase(Locale.getDefault())));
                break;

            case AUDIENCE_INTERNAL:
                //logger.log("Getting token permissions.");
                List<String> scopes = claims.get("per", ArrayList.class);
                //logger.log("Token permissions obtained successfully: {}", scopes);

                if (scopes != null) {
                    scopes.forEach(
                        x -> authorities.add(
                            new SimpleGrantedAuthority(ROLE + x
                                .toUpperCase(Locale.getDefault()))));
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + audience);
        }

        //logger.log("Authorities granted values: {}", authorities);

        return authorities;
    }


    private void denyRequest(@NotNull final HttpServletResponse response) {

        try (PrintWriter writer = response.getWriter()) {
            ApiResponseVO<Object> apiResponse = new ApiResponseVO<>();
            apiResponse.setMessage(UNAUTHORIZED_REQUEST);
            apiResponse.setCode(HttpStatus.UNAUTHORIZED.value());
            String dataResponse = new ObjectMapper().writeValueAsString(apiResponse);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            writer.write(dataResponse);
            writer.flush();

        } catch (IOException e) {
            logger.log("Error while writing response to deny request.");
            //logger.log(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private static UserSecurity getUserDataFromTokenUser(Map<String, Object> tokenUser) {

        List<Company> companyList = getCompanyListFromTokenCompanies(
            (List<Map<String, String>>) tokenUser.get("companies")
        );

        return UserSecurity.builder()
            .id(Long.valueOf(tokenUser.get("user_id").toString()))
            .firstName(tokenUser.get("first_name").toString())
            .lastName(tokenUser.get("last_name").toString())
            .email(tokenUser.get("email").toString())
            .phone(tokenUser.get("phone").toString())
            .countryId(Long.valueOf(tokenUser.get("country_id").toString()))
            .allowAdmin(Boolean.valueOf(tokenUser.get("allow_admin").toString()))
            .companies(companyList)
            .defaultCompanyId(Long.valueOf(tokenUser.get("default_company_id").toString()))
            .legacyId(Long.valueOf(tokenUser.get("legacy_id").toString()))
            .build();
    }

    private static List<Company> getCompanyListFromTokenCompanies(
        List<Map<String, String>> tokenCompanies) {
        List<Company> resultSet = new ArrayList<>();
        tokenCompanies.forEach((Map<String, String> setCompany) -> {
            Company company = Company.builder()
                .id(Long.valueOf(String.valueOf(setCompany.get("id"))))
                .name(setCompany.get("name"))
                .timezone(setCompany.get("timezone"))
                .build();
            resultSet.add(company);
        });
        return resultSet;
    }


}
