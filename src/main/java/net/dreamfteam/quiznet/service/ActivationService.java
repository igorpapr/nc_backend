package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.web.dto.LoginRequest;

public interface ActivationService {

    String activateUser(String activationUrl);

    String isUserActivated(String username);
}
