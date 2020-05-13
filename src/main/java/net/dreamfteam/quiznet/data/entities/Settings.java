package net.dreamfteam.quiznet.data.entities;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Settings {
    private String userId;
    private boolean seeFriendsActivities;
    private boolean seeAnnouncements;
}
