package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.web.dto.DtoQuizValid;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuizValidMapper implements RowMapper<DtoQuizValid> {
    @Override
    public DtoQuizValid mapRow(ResultSet rs, int rowNum) throws SQLException {

        DtoQuizValid quiz = DtoQuizValid.builder()
                                        .id(rs.getString("quiz_id"))
                                        .title(rs.getString("title"))
                                        .description(rs.getString("description"))
                                        .imageContent(rs.getBytes("image_content"))
                                        .creationDate(rs.getDate("ver_creation_datetime"))
                                        .creatorId(rs.getString("creator_id"))
                                        .username(rs.getString("username"))
                                        .language(rs.getString("quiz_lang"))
                                        .adminComment(rs.getString("admin_commentary"))
                                        .published(rs.getBoolean("published"))
                                        .activated(rs.getBoolean("activated"))
                                        .build();

        return quiz;
    }
}