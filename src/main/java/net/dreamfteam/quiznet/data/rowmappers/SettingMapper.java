package net.dreamfteam.quiznet.data.rowmappers;


import net.dreamfteam.quiznet.data.entities.Setting;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SettingMapper implements RowMapper<Setting> {

    @Override
    public Setting mapRow(ResultSet resultSet, int i) throws SQLException {
        return Setting.builder()
                .id(resultSet.getString("setting_id"))
                .title(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .value(resultSet.getString("value"))
                .build();
    }
}
