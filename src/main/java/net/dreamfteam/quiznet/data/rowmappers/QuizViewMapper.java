package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.QuizView;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuizViewMapper implements RowMapper<QuizView> {
	@Override
	public QuizView mapRow(ResultSet rs, int rowNum) throws SQLException {
		QuizView quizView = QuizView.builder()
				.quiz_id(rs.getString("quiz_id"))
				.title(rs.getString("title"))
				.image_content(rs.getBytes("image_content"))
				.build();

		return quizView;
	}
}
