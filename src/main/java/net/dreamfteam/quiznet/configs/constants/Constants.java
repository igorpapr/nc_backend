package net.dreamfteam.quiznet.configs.constants;

public class Constants {

    //URLS
    public static final String QUIZ_URLS = "/api/quizzes";
    public static final String GAME_URLS = "/api/games";
    public static final String ACCOUNT_URLS = "api/account";
    public static final String SIGN_UP_URLS = "/api";
    public static final String USER_URLS = "/api/profiles";
    public static final String RECOVER_URLS = "/api/recovery";
    public static final String ADMIN_URLS = "/api/admins";
    public static final String ANNOUNCEMENT_URLS = "/api/announcement";
    public static final String SETTING_URLS = "/api/settings";
    public static final String NOTIFICATION_URLS = "/api/notifications";
    public static final String ACTIVITIES_URLS = "/api/activities";

    //Security Urls
    public static final String SECUR_SIGN_UP_URLS = "/api/sign-up";
    public static final String SECUR_ACTIVATION_URLS = "/api/activation";
    public static final String SECUR_LOG_IN_URLS = "/api/log-in";
    public static final String SECUR_RECOVER_URLS = "/api/recovery/**";
    public static final String SECURE_ANNOUNCEMENT_LIST_URLS = "/api/announcement/getall";
    public static final String SECURE_ANNOUNCEMENT_URLS = "/api/announcement/get/**";
    public static final String SECURE_ANNOUNCEMENT_SIZE = "/api/announcement/getamount";
    public static final String SECUR_QUIZ_QUESTION_LIST_URLS = "/api/quizzes/questions**";
    public static final String SECUR_QUIZ_TOTAL_SIZE_URLS = "/api/quizzes/totalsize";
    public static final String SECUR_QUIZ_TAGS_URLS = "/api/quizzes/tags";
    public static final String SECUR_QUIZ_CATEG_LIST_URLS = "/api/quizzes/categories";
    public static final String SECUR_QUIZ_URLS = "/api/quizzes**";
    public static final String SECUR_QUIZ_LIST_URLS = "/api/quizzes/quiz-list/**";
    public static final String SECUR_FILTER_QUIZ_LIST_URLS = "/api/quizzes/filter-quiz-list/**";
    public static final String SECUR_SHORT_QUIZ_LIST_URLS = "/api/quizzes/short-list";
    public static final String SECUR_NOTIFICATION_SSE_URLS = "/api/notifications/subscribe/**";
    public static final String SECUR_GAME_SSE_URLS = "/api/games/subscribe/**";
    public static final String SECUR_ANONYM = "/api/anonym**";

    //Security Swagger Urls
    public static final String SECUR_DOCS_URLS = "/v2/api-docs";
    public static final String SECUR_CONFIG_UI_URLS = "/configuration/ui";
    public static final String SECUR_SWAGGER_RESOURCES_URLS = "/swagger-resources/**";
    public static final String SECUR_CONFIG_SECURITY_URLS = "/configuration/security";
    public static final String SECUR_SWAGGER_UI_URLS = "/swagger-ui.html";
    public static final String SECUR_WEBJARS_URLS = "/webjars/**";


    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    public static final int AMOUNT_QUIZ_ON_PAGE = 16;
    public static final int AMOUNT_VALID_QUIZ_ON_PAGE = 6;
    public static final int AMOUNT_SUGGESTIONS_QUIZ_LIST = 3;
    public static final int AMOUNT_FRIENDS_ON_PAGE = 8;
    public static final int AMOUNT_INVITATIONS_ON_PAGE = 8;
    public static final int AMOUNT_QUESTIONS_ON_PAGE = 3;

    //Achievement ids
    public static final int ACHIEVEMENT_FIRST_BLOOD_ID = 1;
    public static final int ACHIEVEMENT_FRESHMAN_ID = 2;
    public static final int ACHIEVEMENT_CASUAL_ID = 3;
    public static final int ACHIEVEMENT_EXPERT_ID = 4;
    public static final int ACHIEVEMENT_UKRAINE_CATEGORY_ID = 5;
    public static final int ACHIEVEMENT_HISTORY_CATEGORY_ID = 6;
    public static final int ACHIEVEMENT_SCIENCE_CATEGORY_ID = 7;
    public static final int ACHIEVEMENT_GEOGRAPHY_CATEGORY_ID = 8;
    public static final int ACHIEVEMENT_OTHERS_CATEGORY_ID = 9;
    public static final int ACHIEVEMENT_SANDBOX_ID = 10;
    public static final int ACHIEVEMENT_SPECIALIST_CREATOR_ID = 11;
    public static final int ACHIEVEMENT_POPULAR_CREATOR_ID = 12;
    public static final int ACHIEVEMENT_EXTREMELY_POPULAR_ID = 13;
    public static final int ACHIEVEMENT_MASTERPIECE_CREATOR_ID = 14;

    //Language setting id
    public static final String SETTING_LANG_ID = "e8449301-6d6f-4376-8247-b7d1f8df6416";

    //Exceptions messages
    public static final String USER_NOT_FOUND_WITH_USERNAME = "User not found with such username : ";
    public static final String USER_NOT_FOUND_WITH_ID = "User not found with id : ";
    public static final String USER_NOT_FOUND_WITH_EMAIL = "User not found with email : ";
    public static final String USER_NOT_FOUND_WITH_RECOVER_URL = "User not found with recover URL : ";
    public static final String USER_NOT_FOUND_WITH_EMAIL_OR_USERNAME = "User not found with such username or email : ";
    public static final String USER_NOT_FOUND_WITH_ACTIVATION_URL = "User not found with activation URL : ";
    public static final String IMAGE_BROKEN = "Broken image";
    public static final String NOT_HAVE_CAPABILITIES = "You dont have such capabilities";
    public static final String USERNAME_TAKEN = "Such username has been taken : ";
    public static final String EMAIl_TAKEN = "Such username has been taken : ";
    public static final String PASSWORD_NOT_CORRECT = "Not correct password";
    public static final String RECOVER_LINK_EXPIRED = "Your recover link is expired. Try again";
    public static final String NOT_ACTIVATED = "Your profile is not activated";

    //Logging messages
    public static final String AUTHENTICATION_NOT_SET = "Could not set user authentication in security context";
    public static final String REG_MAIL_NOT_SENT = "Registration Mail not sent to user %s";
    public static final String RECOVERY_MAIL_NOT_SENT = "Recovery Mail not sent to user %s";
    public static final String DELETED_UNACCEPTED_USERS = "%d unaccepted Users was deleted ";

    //Swagger
    public static final String SWAGGER_TITLE = "QuizNet Documentation";
    public static final String SWAGGER_DESCRIPTION = "QuizNet Documentation";


}
