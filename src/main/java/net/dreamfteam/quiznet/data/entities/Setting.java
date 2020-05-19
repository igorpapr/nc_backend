package net.dreamfteam.quiznet.data.entities;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Setting {
    private String id;
    private String title;
    private String description;
    private String value;

}
