package net.dreamfteam.quiznet.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Builder

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoChatUser {

    private String id;

    private String username;

    private Date lastTimeOnline;

    private  byte[] image;

    private Date joinedToChatDate;
}
