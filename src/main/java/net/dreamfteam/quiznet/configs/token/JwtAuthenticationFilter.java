package net.dreamfteam.quiznet.configs.token;


import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.constants.Constants;
import net.dreamfteam.quiznet.exception.ValidationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * JWT token filter that handles all HTTP requests to application.
 *
 * @author Yevhen Khominich
 * @version 1.0
 */

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    final private JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            String jwtToken = tokenProvider.resolveToken((HttpServletRequest) servletRequest);

            if (StringUtils.hasText(jwtToken) && tokenProvider.validateToken(jwtToken)) {

                String usernameFromJwt = tokenProvider.getUsernameFromJwt(jwtToken);

                if (StringUtils.isEmpty(usernameFromJwt)) {
                    throw new ValidationException(Constants.USER_NOT_FOUND_WITH_USERNAME + usernameFromJwt);
                }

                Authentication authentication = tokenProvider.getAuthentication(jwtToken);

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        } catch (Exception ex) {
            log.error(Constants.AUTHENTICATION_NOT_SET, ex);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}


