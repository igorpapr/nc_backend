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

    //Announcements constants

    public static final String ANNOUNCEMENTS_CREATE_ANNOUNCEMENT =
            "INSERT INTO announcements " +
                    "(creator_id, title, text_content, datetime_creation," +
                    " datetime_publication, is_published, image)" +
                    " VALUES (?,?,?,?,?,?,?)";

    public static final String ANNOUNCEMENTS_GET_ANNOUNCEMENT_BY_ID =
            "SELECT announcement_id,  creator_id as username, " +
                    "title, text_content, announcements.image, " +
                    "datetime_creation, is_published, datetime_publication" +
                    " from announcements join users " +
                    "on announcements.creator_id = users.user_id " +
                    "where announcement_id = UUID(?)";


    public static final String ANNOUNCEMENTS_GET_ALL_ANNOUNCEMENTS =
            "SELECT announcement_id, username, title, text_content," +
                    " announcements.image, datetime_creation, is_published, " +
                    "datetime_publication from announcements " +
                    "join users on announcements.creator_id = users.user_id " +
                    "where datetime_publication < current_timestamp " +
                    "order by datetime_publication desc limit ? offset ? rows;";

    public static final String ANNOUNCEMENTS_EDIT_ANNOUNCEMENT_WITH_IMAGE =
            "UPDATE announcements SET creator_id = UUID(?), title = ?,  text_content = ?"
                    + ",is_published = ?, image = ? WHERE  announcement_id = UUID(?)";

    public static final String ANNOUNCEMENTS_EDIT_ANNOUNCEMENT_WITHOUT_IMAGE =
            "UPDATE announcements SET creator_id = UUID(?), title = ?,  text_content = ?"
                    + ",is_published = ? WHERE  announcement_id = UUID(?)";

    public static final String ANNOUNCEMENTS_DELETE_ANNOUNCEMENT =
            "DELETE FROM announcementS WHERE announcement_id = UUID(?)";

    public static final String ANNOUNCEMENTS_GET_ANNOUNCEMENTS_AMOUNT =
            "SELECT count(*) from announcements";

    // Users Constants Queries

    public static final String SELECT_USER_QUERY =
            "SELECT user_id, email, password, username, is_activated, is_verified, last_time_online," +
                    "image, about_me, recovery_url, recovery_sent_time, activation_url, " +
                    "date_acc_creation, role, roles.role_id " +
            "FROM users INNER JOIN roles ON users.role_id = roles.role_id\n";

    public static final String USERS_UPDATE_QUERY_BY_ID =
            "UPDATE users SET username = ?, email = ?, password= ?, is_activated = ?, is_verified = ?," +
                    "last_time_online = ?, image = ?, about_me = ?, recovery_url = ?, recovery_sent_time = ?, role_id = ? " +
            "WHERE user_id = UUID(?) ;";

    public static final String DELETE_USER_QUERY = "DELETE FROM users\n";

    public static final String USERS_GET_ALL_BY_ROLE_USER = SELECT_USER_QUERY +
            "WHERE roles.role_id = 1 AND is_activated = true ORDER BY last_time_online DESC ;";

    public static final String USERS_GET_BY_USERNAME_SUBSTR = SELECT_USER_QUERY +
            "WHERE LOWER(username) LIKE LOWER(?) ORDER BY role , last_time_online DESC ;";

    public static final String USERS_GET_BY_USERNAME_SUBSTR_AND_ROLE_USER = SELECT_USER_QUERY +
            "WHERE LOWER(username) LIKE LOWER(?) " +
            "AND roles.role_id = 1 and is_activated = true ORDER BY last_time_online DESC ;";

    public static final String USERS_GET_BY_EMAIL = SELECT_USER_QUERY +
            "WHERE email = ? ;";

    public static final String USERS_GET_BY_USERNAME = SELECT_USER_QUERY +
            "WHERE username = ? ;";

    public static final String USERS_GET_BY_ID = SELECT_USER_QUERY +
            "WHERE user_id = UUID(?) ;";

    public static final String USERS_GET_BY_RECOVERY_URL = SELECT_USER_QUERY +
            "WHERE recovery_url = ?";

    public static final String USERS_GET_BY_ACTIVATION_URL = SELECT_USER_QUERY +
            "WHERE activation_url = ?";

    public static final String USERS_SAVE_QUERY =
            "INSERT INTO users (user_id, username, email, password, is_activated," +
                    "is_verified, activation_url, date_acc_creation, last_time_online, role_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ;";

    public static final String USERS_DELETE_BY_ID = DELETE_USER_QUERY +
            "WHERE user_id = UUID(?) ;";

    public static final String USERS_DELETE_IF_LINK_EXPIRED = DELETE_USER_QUERY +
            "WHERE is_verified = 'false' AND current_timestamp - date_acc_creation >= '1 DAY' ;";

    public static final String USERS_GET_ALL_POPULAR_CREATORS = SELECT_USER_QUERY +
            "INNER JOIN " +
            "(SELECT creator_id, count(quiz_id) AS count FROM quizzes " +
            "WHERE validated = true and published = true GROUP BY creator_id) AS cic " +
            "ON creator_id = user_id WHERE roles.role_id = 1 AND is_activated = true ORDER BY count DESC LIMIT 20 ;";

    public static final String USERS_GET_ALL_PRIVILIGED = SELECT_USER_QUERY +
            "WHERE roles.role_id > 1 ODDER BY last_time_online DESC ;";

    public static final String USERS_REMOVE_FRIEND =
            "UPDATE friends SET accepted_datetime = null, parent_id = UUID(?), friend_id=UUID(?)" +
            "WHERE parent_id IN ( UUID(?), UUID(?)) AND friend_id IN ( UUID(?), UUID(?))";

    public static final String USERS_GET_FRIENDS_RELATIONS =
            "SELECT f.parent_id = uuid(?) AS outgoing ," +
                    "f.friend_id = uuid(?) AS incoming , f.accepted_datetime IS NOT NULL AS friend from friends f " +
                    "WHERE  (f.parent_id IN ( UUID(?), UUID(?)) and f.friend_id IN ( UUID(?), UUID(?))) ;";

    public static final String USERS_REJECT_FRIEND_INVITATION =
            "DELETE FROM friends " +
            "WHERE parent_id = uuid(?) AND friend_id = uuid(?) ;";

    public static final String USERS_ACCEPT_FRIEND_INIVITAION = "UPDATE friends " +
            "SET accepted_datetime = CURRENT_TIMESTAMP " +
            "WHERE parent_id = UUID(?) AND friend_id = UUID(?) ;";

    public static final String USERS_SAVE_OUTGOING_FRIEND_INVITATION =
            "INSERT INTO friends (parent_id, friend_id, invite_datetime) " +
            "VALUES (UUID(?), UUID(?), CURRENT_TIMESTAMP) ;";

    public static final String USERS_DELETE_OUTGOING_FRIEND_INVITATION =
            "DELETE FROM friends " +
            "WHERE friend_id IN " +
            "(UUID(?), UUID(?)) AND parent_id IN ( UUID(?), UUID(?)) ;";

    public static final String USERS_GET_FRIEND_OUTGOING_INVITATIONS_TOTAL_SIZE =
            "SELECT COUNT(*) AS total_size " +
            "FROM friends " +
            "WHERE parent_id = uuid(?) " +
            "AND accepted_datetime IS NULL;";

    public static final String USERS_GET_FRIEND_OUTGOING_INVITATIONS_BY_USER_ID =
            "SELECT user_id, username, invite_datetime, image AS image_content " +
            "FROM users INNER JOIN friends f ON users.user_id = f.friend_id " +
            "WHERE f.parent_id = uuid(?) " +
            "AND f.accepted_datetime IS NULL " +
            "LIMIT ? OFFSET ?;";

    public static final String USERS_GET_FRIEND_INCOMING_INVITATIONS_TOTAL_SIZE =
            "SELECT COUNT(*) AS total_size " +
            "FROM friends " +
            "WHERE friend_id = uuid(?) " +
            "AND accepted_datetime IS NULL;";

    public static final String USERS_GET_FRIEND_INCOMING_INVITATIONS_BY_USER_ID =
            "SELECT user_id, username, invite_datetime, image AS image_content " +
            "FROM users INNER JOIN friends f ON users.user_id = f.parent_id " +
            "WHERE f.friend_id = uuid(?) " +
            "AND f.accepted_datetime IS NULL " +
            "LIMIT ? OFFSET ?;";

    public static final String USERS_GET_FRIENDS_TOTAL_SIZE =
            "SELECT COUNT(*) AS total_size " +
            "FROM friends " +
            "WHERE (parent_id = uuid(?) " +
            "OR friend_id = uuid(?))" +
            "AND accepted_datetime IS NOT NULL;";

    public static final String USERS_GET_ALL_FRIENDS_BY_USER_ID =
            "SELECT user_id, username, last_time_online, image AS image_content " +
            "FROM users WHERE user_id IN (SELECT f.friend_id AS id " +
            "FROM friends f " +
            "WHERE f.parent_id = uuid(?) " +
            "AND f.accepted_datetime IS NOT NULL " +
                    "UNION " +
                    "SELECT f1.parent_id AS id " +
                    "FROM friends f1 " +
                    "WHERE f1.friend_id = uuid(?) " +
                    "AND f1.accepted_datetime IS NOT NULL) " +
                    "LIMIT ? OFFSET ?;";
}
