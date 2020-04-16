package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.web.dto.LoginRequest;

public interface ActivationService {

    void activateUser(String hashedId);

    String isUserActivated(LoginRequest loginRequest);
}
