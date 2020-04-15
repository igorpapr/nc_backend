package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;


@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class User {

    private long id;

    private String email;

    private String password;

    private String username;

    private boolean activated;

    private Date creationDate;

}
