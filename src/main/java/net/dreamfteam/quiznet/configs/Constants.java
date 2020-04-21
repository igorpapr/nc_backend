package net.dreamfteam.quiznet.configs;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Constants {

    //URLS
    public static final String SIGN_UP_URLS = "/api";
    public static final String USER_URLS = "/api/profiles";
    public static final String TEST_URLS = "/api/test/**";
    public static final String RECOVER_URLS = "/api/recovery";
    public static final String ADMIN_URLS = "/api/admins";

    //Security Urls
    public static final String SECUR_SIGN_UP_URLS = "/api/sign-up";
    public static final String SECUR_ACTIVATION_URLS = "/api/activation";
    public static final String SECUR_LOG_IN_URLS = "/api/log-in";
    public static final String SECUR_RECOVER_URLS = "/api/recovery/**";
    public static final String SECUR_ADMIN_URLS = "/api/admins/**";

    public static final String SECRET_MD5 = "SecretQuiz";
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 300_000_000; //30 000 seconds

    public static final String SERVER_NAME = "qznetbc.herokuapp.com";
    public static final int SERVER_PORT = 8081;

    public static final String MY_EMAIL = "QuizNetFromUk";
    public static final String MY_PASSWORD = "ukQuizNet!23141";

    //Registration messages
    public static final String REG_MAIL_SUBJECT = "Profile registration(QuizNet)";
    public static final String REG_MAIL_ARTICLE = "Your profile has been registered! Thank you!";
    public static final String REG_MAIL_MESSAGE = "To activate your profile visit next link: ";
    public static final String REG_URL_ACTIVATE = "http://" + SERVER_NAME + "/api/activation?key=";

    //Administration registration messages
    public static final String REG_ADMIN_MAIL_SUBJECT = "Administrative profile registration(QuizNet)";
    public static final String REG_ADMIN_MAIL_ARTICLE = "Your administrative profile has been registered! Thank you!";
    public static final String REG_ADMIN_MAIL_MESSAGE = "To activate your administrative profile visit next link: ";

    //Recovering password messages
    public static final String RECOVER_MAIL_SUBJECT = "Reset password(QuizNet)";
    public static final String RECOVER_MAIL_ARTICLE = "Changing password";
    public static final String RECOVER_MAIL_MESSAGE = "To change your password visit next link: ";
    public static final String RECOVER_URL = "http://" + SERVER_NAME + "/api/recovery/confirm?key=";


    //Achievement messages
    public static final String ACHIEVE_NOTIFIC_MAIL_SUBJECT = "New Achievement(GRAMPUS)";
    public static final String ACHIEVE_NOTIFIC_MAIL_ARTICLE = "Congratulation!";
    public static final String ACHIEVE_NOTIFIC_MAIL_MESSAGE = "You got new achievement";

    //Tempcode fo change password
    public static final String TEMPCODE_NOTIFIC_MAIL_SUBJECT = "Verification code";
    public static final String TEMPCODE_NOTIFIC_MAIL_ARTICLE = "To change password enter this code";

    public static final int DEFAULT_SIZE_MESSAGE_HISTORY = 20;

    public static List<Locale> SUPPORTED_LOCALES = Arrays.asList(new Locale("en"), new Locale("ru"));

}
