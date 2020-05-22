package net.dreamfteam.quiznet.data.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Notification {
    private String id;
    private String userId;
    private String content;
    private String contentUk;
    private String link;
    private Date date;
    private boolean seen;
}
