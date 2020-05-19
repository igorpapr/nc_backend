package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.QuizFiltered;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuizFilteredMapper implements RowMapper<QuizFiltered> {
	@Override
	public QuizFiltered mapRow(ResultSet rs, int rowNum) throws SQLException {
		QuizFiltered quiz = QuizFiltered.builder()
				.id(rs.getString("quiz_id"))
				.title(rs.getString("title"))
				.imageContent(rs.getBytes("image"))
				.build();

		return quiz;
	}
}