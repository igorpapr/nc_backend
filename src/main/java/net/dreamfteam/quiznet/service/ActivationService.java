package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.web.dto.LoginRequest;

public interface ActivationService {

    void activateUser(String activationUrl);

    String isUserActivated(LoginRequest loginRequest);
}
