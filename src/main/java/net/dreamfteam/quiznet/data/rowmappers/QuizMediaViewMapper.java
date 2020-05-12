package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.Quiz;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuizMediaViewMapper implements RowMapper<QuizMediaView> {
	@Override
	public QuizMediaView mapRow(ResultSet rs, int rowNum) throws SQLException {
		QuizMediaView quizMediaView = QuizMediaView.builder()
				.quizId(rs.getString("quiz_id"))
				.title(rs.getString("title"))
				.description(rs.getString("description"))
				.imageContent(rs.getBytes("image_content"))
				.build();

		return quizMediaView;
	}
}
