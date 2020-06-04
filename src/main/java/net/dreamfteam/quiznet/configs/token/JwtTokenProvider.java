package net.dreamfteam.quiznet.configs.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.constants.Constants;
import net.dreamfteam.quiznet.data.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static javax.management.timer.Timer.ONE_DAY;


/**
 * Util class that provides methods for generation, validation, etc. of JWT token.
 *
 * @author Yevhen Khominich
 * @version 1.0
 */

@Component
@Slf4j
public class JwtTokenProvider {

    final private UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenProvider(@Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Value("${jwt.secret.key}")
    private String secret;

    public String provideToken(String username) {

        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(username);

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + ONE_DAY);

        String userId = jwtUser.getId();

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.JWT_CLAIMS_ID, userId);
        claims.put(Constants.JWT_CLAIMS_USERNAME, jwtUser.getUsername());
        claims.put(Constants.JWT_CLAIMS_EMAIL, jwtUser.getEmail());
        claims.put(Constants.JWT_CLAIMS_ROLE, jwtUser.getRole().name());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String provideTokenForAnonym(String username) {

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + ONE_DAY);

        Map<String, Object> claims = new HashMap<>();

        String id = Constants.ANONYM_POINTER + UUID.randomUUID();

        claims.put(Constants.JWT_CLAIMS_USERNAME, username);
        claims.put(Constants.JWT_CLAIMS_ROLE, Role.ROLE_ANONYM.name());
        claims.put(Constants.JWT_CLAIMS_ID, id);

        return Jwts.builder()
                .setSubject(id)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    public Authentication getAuthentication(String token) {

        UserDetails userDetails;

        if (isAnonym(token)) {
            userDetails = JwtUserFactory.anonymUser(getUsernameFromJwt(token), getUserIdFromJwt(token));
        } else userDetails = userDetailsService.loadUserByUsername(getUsernameFromJwt(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error(Constants.NOT_CORRECT_JWT_SIGNATURE, ex);
        } catch (MalformedJwtException ex) {
            log.error(Constants.INVALID_JWT, ex);
        } catch (ExpiredJwtException ex) {
            log.error(Constants.EXPIRED_JWT, ex);
        } catch (UnsupportedJwtException ex) {
            log.error(Constants.UNSUPPORTED_JWT, ex);
        } catch (IllegalArgumentException ex) {
            log.error(Constants.EMPTY_CLAIMS_JWT, ex);
        }
        return false;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.HEADER_STRING);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constants.TOKEN_PREFIX)) {
            return bearerToken.substring(Constants.TOKEN_PREFIX.length());
        }
        return null;
    }

    public String getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return (String) claims.get(Constants.JWT_CLAIMS_ID);
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return (String) claims.get(Constants.JWT_CLAIMS_USERNAME);
    }

    public boolean isAnonym(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.get(Constants.JWT_CLAIMS_ROLE).equals(Role.ROLE_ANONYM.name());

    }
}
