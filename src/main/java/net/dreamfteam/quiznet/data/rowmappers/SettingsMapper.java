package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.Settings;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsMapper implements ResultSetExtractor<Settings> {

    @Override
    public Settings extractData(ResultSet resultSet) throws SQLException, DataAccessException {

        Settings settings = Settings.builder().build();
        while (resultSet.next()) {

            switch (resultSet.getString("title")) {
                case "Notifications":
                    settings.setSeeAnnouncements(
                            Boolean.parseBoolean(resultSet.getString("value")));
                    break;
                case "Friends related activities":
                    settings.setSeeFriendsActivities(
                            Boolean.parseBoolean(resultSet.getString("value")));
            }
        }
        return settings;
    }
}
