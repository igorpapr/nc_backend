package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.Rating;
import net.dreamfteam.quiznet.data.entities.Setting;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingMapper implements RowMapper<Rating> {

    @Override
    public Rating mapRow(ResultSet rs, int i) throws SQLException {
        return Rating.builder()
                .quizId(rs.getString("quiz_id"))
                .userId(rs.getString("user_id"))
                .rating(rs.getInt("rating_points"))
                .build();
    }
}
