package net.dreamfteam.quiznet.configs.constants;

public class SqlConstants {

    //Notifications constants
    public static final String NOTIFICATIONS_GET_UNSEEN_BY_USER_ID =
            "SELECT " +
                    "CASE value " +
                    "WHEN 'uk' " +
                    "THEN content_uk " +
                    "WHEN 'en' " +
                    "THEN content " +
                    "END AS content, " +

                    "notif_id, n.user_id, date_time, seen, link, type_id " +
                    "FROM user_notifications n INNER JOIN user_settings s ON n.user_id = s.user_id " +
                    "WHERE seen = false AND n.user_id = UUID(?) AND setting_id = UUID(?)";

    public static final String NOTIFICATIONS_GET_BY_ID =
            "SELECT " +
                    "CASE value " +
                    "WHEN 'uk' " +
                    "THEN content_uk " +
                    "WHEN 'en' " +
                    "THEN content " +
                    "END AS content, " +

                    "notif_id, n.user_id, date_time, seen, link, type_id " +
                    "FROM user_notifications n INNER JOIN user_settings s ON n.user_id = s.user_id " +
                    "WHERE notif_id = UUID(?) AND setting_id = ?";

    public static final String NOTIFICATIONS_INSERT =
            "INSERT INTO user_notifications " +
                    "(content, user_id, content_uk, link, type_id) " +
                    "VALUES (?,?,?,?,?)";

    public static final String NOTIFICATIONS_UPDATE_SEEN =
            "UPDATE user_notifications " +
                    "SET seen = true " +
                    "WHERE user_id = UUID(?)";

    public static final String NOTIFICATIONS_DELETE_HALF_YEAR =
            "DELETE " +
                    "FROM user_notifications " +
                    "WHERE date_time < CURRENT_TIMESTAMP  - interval '181' day";



    //Settings constants
    public static final String SETTINGS_INIT_SETTINGS_DEFAULTS =
            "INSERT INTO user_settings (user_id, setting_id, value) " +
                    "SELECT UUID(?) , setting_id, default_value " +
                    "FROM settings " +
                    "WHERE title=?";

    public static final String SETTINGS_INIT_SETTINGS_LANGUAGE =
            "INSERT INTO user_settings (user_id, setting_id, value) " +
                    "VALUES (UUID(?),UUID('e8449301-6d6f-4376-8247-b7d1f8df6416'),?)";

    public static final String SETTINGS_GET_TITLES_DEFAULT =
            "SELECT title " +
                    "FROM settings " +
                    "WHERE default_value IS NOT NULL";

    public static final String SETTINGS_GET_TITLES_PRIVILEGED =
            "SELECT title " +
                    "FROM settings " +
                    "WHERE privileged = TRUE AND default_value IS NOT NULL";

    public static final String SETTINGS_EDIT_SETTINGS =
            "UPDATE user_settings " +
                    "SET value=? " +
                    "WHERE user_id = UUID(?) AND setting_id = UUID(?)";

    public static final String SETTINGS_GET_SETTINGS =
            "SELECT " +
                    "CASE " +
                    "WHEN ? = 'uk' " +
                    "THEN title_uk " +
                    "ELSE title " +
                    "END AS title, " +

                    "CASE " +
                    "WHEN ? = 'uk' " +
                    "THEN description_uk " +
                    "ELSE description " +
                    "END AS description, " +

                    "settings.setting_id, value " +
                    "FROM settings INNER JOIN " +
                    "user_settings on settings.setting_id=user_settings.setting_id " +
                    "WHERE user_id=UUID(?)";

    public static final String SETTINGS_GET_LANGUAGE =
            "SELECT value " +
                    "FROM user_settings " +
                    "WHERE user_id = UUID(?) AND setting_id = 'e8449301-6d6f-4376-8247-b7d1f8df6416'";

    public static final String SETTINGS_GET_NOTIFICATION_SETTING =
            "SELECT value " +
                    "FROM user_settings " +
                    "WHERE userId = UUID(?) AND setting_id = '34c00e41-9eab-49f9-8a9a-4862f6379dd0'";
}
