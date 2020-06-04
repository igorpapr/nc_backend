package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Notification {
    private String id;
    private String userId;
    private String content;
    private String contentUk;
    private String link;
    private int typeId;
    private Date date;
    private boolean seen;
}
