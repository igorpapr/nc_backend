package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.web.dto.LoginRequest;

public interface ActivationService {

    String verifyUser(String activationUrl);

    String isUserVerified(String username);
}
