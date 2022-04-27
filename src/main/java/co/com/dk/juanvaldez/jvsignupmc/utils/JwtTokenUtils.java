package co.com.dk.juanvaldez.jvsignupmc.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JwtTokenUtils {

    private static final String EXPIRATION_KEY = "exp";

    private JwtTokenUtils() {
        // Empty constructor
    }

    private static Claims getClaims(String token, String secret) {
        return Jwts.parser()
            .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
            .parseClaimsJws(token).getBody();
    }

    public static Long getExpiration(String token, String secret) {
        return getClaims(token, secret)
            .get(EXPIRATION_KEY, Date.class)
            .getTime();
    }

}
