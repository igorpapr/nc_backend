package net.dreamfteam.quiznet.data.entities;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Settings {
    private boolean seeFriendsActivities;
    private boolean seeAnnouncements;
}
