package net.dreamfteam.quiznet.configs.interceptors;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.constants.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
@Slf4j
public class OnlineServiceInterceptor implements HandlerInterceptor {

    final private UserService userService;

    final private IAuthenticationFacade authenticationFacade;

    @Autowired
    public OnlineServiceInterceptor(UserService userService, IAuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = authenticationFacade.getUserId();
        if (userId != null && !userId.startsWith(Constants.ANONYM_POINTER)) {
            User currentUser = userService.getById(userId);
            currentUser.setLastTimeOnline(new Date());
            userService.update(currentUser);
        }
        return true;
    }

}
