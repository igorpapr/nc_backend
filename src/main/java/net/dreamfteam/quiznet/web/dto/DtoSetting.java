package net.dreamfteam.quiznet.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DtoSetting<T> {
    private T value;
}
