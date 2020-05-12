package net.dreamfteam.quiznet.configs;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Constants {

    //URLS
    public static final String QUIZ_URLS = "/api/quizzes";
    public static final String GAME_URLS = "/api/games";
    public static final String SIGN_UP_URLS = "/api";
    public static final String USER_URLS = "/api/profiles";
    public static final String TEST_URLS = "/api/test/**";
    public static final String RECOVER_URLS = "/api/recovery";
    public static final String ADMIN_URLS = "/api/admins";
    public static final String ANNOUNCEMENT_URLS = "/api/announcement";
    public static final String SETTING_URLS = "/api/settings";

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
    public static final String SECUR_SHORT_QUIZ_LIST_URLS = "/api/quizzes/short-list";

    public static final String SECRET_MD5 = "SecretQuiz";
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 300_000_000; //30 000 seconds
    public static final int AMOUNT_QUIZ_ON_PAGE = 16;
    public static final int AMOUNT_VALID_QUIZ_ON_PAGE = 6;
    public static final int AMOUNT_SUGGESTIONS_QUIZ_LIST = 3;

    //Registration messages
    public static final String REG_MAIL_SUBJECT = "Profile registration(QuizNet)";
    public static final String REG_MAIL_ARTICLE = "Your profile has been registered! Thank you!";
    public static final String REG_MAIL_MESSAGE = "To activate your profile visit next link: ";

    //Administration registration messages
    public static final String REG_ADMIN_MAIL_SUBJECT = "Administrative profile registration(QuizNet)";
    public static final String REG_ADMIN_MAIL_ARTICLE = "Your administrative profile has been registered! Thank you!";
    public static final String REG_ADMIN_MAIL_MESSAGE = "To activate your administrative profile visit next link: ";

    //Recovering password messages
    public static final String RECOVER_MAIL_SUBJECT = "Reset password(QuizNet)";
    public static final String RECOVER_MAIL_ARTICLE = "Changing password";
    public static final String RECOVER_MAIL_MESSAGE = "To change your password visit next link: ";
    public static final int AMOUNT_QUESTIONS_ON_PAGE = 3;

    public static List<Locale> SUPPORTED_LOCALES = Arrays.asList(new Locale("en"), new Locale("ru"));

}
