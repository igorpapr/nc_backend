package net.dreamfteam.quiznet.data.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Chat {

    private String id;

    private String title;

    private boolean isCreator;

    private boolean isPersonal;

    private Date joinedDate;

    private byte[] image;


}
