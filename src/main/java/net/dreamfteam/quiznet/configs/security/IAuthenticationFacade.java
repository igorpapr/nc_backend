package net.dreamfteam.quiznet.configs.security;


import net.dreamfteam.quiznet.data.entities.Role;

public interface IAuthenticationFacade {
    String getUserId();

    String getUsername();

    Role getRole();
}
