package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.Question;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionMapper implements RowMapper<Question> {
    @Override
    public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
        Question question = Question.builder()
                .id(rs.getString("question_id"))
                .quizId(rs.getString("quiz_id"))
                .title(rs.getString("title"))
                .content(rs.getString("content"))
                .imageContent(rs.getBytes("img"))  // NOT A REFERENCE(REAL IMAGE)
                .points(rs.getInt("points"))
                .typeId(rs.getInt("type_id"))
                .build();

        return question;
    }
}
