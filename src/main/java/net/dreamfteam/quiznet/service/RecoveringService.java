package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.web.dto.DtoChangePassword;
import net.dreamfteam.quiznet.web.dto.DtoMail;

public interface RecoveringService {
    void sendRecoveringUrl(DtoMail user);

    String confirmRecovery(String recoverUrl);

    void changePassword(DtoChangePassword passwordDto);
}
