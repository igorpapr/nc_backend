package net.dreamfteam.quiznet.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dreamfteam.quiznet.data.entities.Chat;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoChatWithParticipants {

    private String title;

    private List<DtoChatUser> participants;

    public static DtoChatWithParticipants toDtoChatWithParticipants(Chat chat) {
        return builder()
                .title(chat.getTitle()).build();
    }
}
