package net.dreamfteam.quiznet.data.entities;

import lombok.*;

import java.util.Date;


@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class UserView {

	private String id;

	private String username;

	private Date lastTimeOnline;

	private boolean online;

	private byte[] imageContent;
}
