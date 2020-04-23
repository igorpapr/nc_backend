package net.dreamfteam.quiznet.configs;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Constants {

    //URLS
    public static final String SIGN_UP_URLS = "/api/users/**";
    public static final String QUIZ_URLS = "/api/quiz/**";
    public static final String TEST_URLS = "/api/test/**";

    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 300_000_000; //30 000 seconds

    public static final int AMOUNT_QUIZ_ON_PAGE = 16;

    public static final String SERVER_NAME = "qznetbc.herokuapp.com";
    public static final int SERVER_PORT = 8081;

    public static final String MY_EMAIL = "QuizNetFromUk";
    public static final String MY_PASSWORD = "ukQuizNet!23141";

    //Registration messages
    public static final String REG_MAIL_SUBJECT = "Profile registration(QuizNet)";
    public static final String REG_MAIL_ARTICLE = "Your profile has been registered! Thank you!";
    public static final String REG_MAIL_MESSAGE = "To activate your profile visit next link: ";
    public static final String REG_URL_ACTIVATE = "https://" + SERVER_NAME +"/api/users/activate/";

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
