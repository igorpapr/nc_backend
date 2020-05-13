package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.Game;
import net.dreamfteam.quiznet.data.entities.Quiz;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameMapper implements RowMapper<Game> {
    @Override
    public Game mapRow(ResultSet rs, int rowNum) throws SQLException {

        return Game.builder()
                .quizId(rs.getString("quiz_id"))
                .id(rs.getString("game_id"))
                .additionalPoints(rs.getBoolean("time_additional_points"))
                .maxUsersCount(rs.getInt("max_num_of_users"))
                .numberOfQuestions(rs.getInt("number_of_questions"))
                .roundDuration(rs.getInt("round_duration"))
                .accessId(rs.getString("access_code"))
                .startDatetime(rs.getDate("datetime_start"))
                .breakTime(rs.getInt("break_time"))
                .build();
    }
}
