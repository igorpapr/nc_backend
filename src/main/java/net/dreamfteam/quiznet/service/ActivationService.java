package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.web.dto.LoginRequest;

public interface ActivationService {

    String verifyUser(String activationUrl);

    String isUserVerified(User user);

    String getAnonymToken(String username);
}
