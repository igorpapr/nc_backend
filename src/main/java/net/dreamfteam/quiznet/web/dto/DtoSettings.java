package net.dreamfteam.quiznet.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoSettings {
    private String userId;
    private String seeFriendsActivities;
    private String seeAnnouncements;
}
