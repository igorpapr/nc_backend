package net.dreamfteam.quiznet.web.dto;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DtoActivity {

	private String content;

	private Date datetime;

	private String userId;


}
