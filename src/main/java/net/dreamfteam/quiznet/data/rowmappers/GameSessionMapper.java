package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.GameSession;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameSessionMapper implements RowMapper<GameSession> {
    @Override
    public GameSession mapRow(ResultSet resultSet, int i) throws SQLException {
        return GameSession.builder()
                .id(resultSet.getString("game_session_id"))
                .username(resultSet.getString("username"))
                .userId(resultSet.getString("user_id"))
                .gameId(resultSet.getString("game_id"))
                .score(resultSet.getInt("score"))
                .winner(resultSet.getBoolean("is_winner"))
                .creator(resultSet.getBoolean("is_creator"))
                .savedByUser(resultSet.getBoolean("saved_by_user"))
                .durationTime(resultSet.getInt("duration_time"))
                .build();
    }
}
