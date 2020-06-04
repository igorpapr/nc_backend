--
-- PostgreSQL database dump
--

-- Dumped from database version 12.2 (Ubuntu 12.2-2.pgdg16.04+1)
-- Dumped by pg_dump version 12.2

-- Started on 2020-06-02 20:41:46

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4126 (class 1262 OID 11244317)
-- Name: dfscaf50gmsug9; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE dfscaf50gmsug9 WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8';


\connect dfscaf50gmsug9

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA public;


--
-- TOC entry 4127 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 247 (class 1255 OID 11246506)
-- Name: quiz_rating(uuid); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.quiz_rating(quizid uuid) RETURNS numeric
    LANGUAGE plpgsql
    AS $$
declare
    rating decimal;
BEGIN
SELECT sum(uqr.rating_points)::decimal/count(uqr.*) into rating FROM user_quiz_rating uqr where quiz_id=(quizId);
IF rating is null
    then
    rating = 0;
end if;
RETURN rating;
END;
$$;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 203 (class 1259 OID 11246516)
-- Name: achievements; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.achievements (
    achievement_id integer NOT NULL,
    title character varying(128) NOT NULL,
    description character varying(256) NOT NULL,
    image_content bytea,
    category_id uuid,
    title_uk character varying(128) NOT NULL,
    description_uk character varying(256) NOT NULL
);


--
-- TOC entry 204 (class 1259 OID 11246522)
-- Name: achievements_achievement_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.achievements_achievement_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4128 (class 0 OID 0)
-- Dependencies: 204
-- Name: achievements_achievement_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.achievements_achievement_id_seq OWNED BY public.achievements.achievement_id;


--
-- TOC entry 205 (class 1259 OID 11246531)
-- Name: activity_types; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.activity_types (
    type_id integer NOT NULL,
    type character varying(50) NOT NULL
);


--
-- TOC entry 206 (class 1259 OID 11246535)
-- Name: announcements; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.announcements (
    announcement_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    creator_id uuid,
    text_content character varying(2048) NOT NULL,
    image bytea,
    datetime_creation timestamp without time zone NOT NULL,
    is_published boolean NOT NULL,
    datetime_publication timestamp without time zone NOT NULL,
    title character varying(50)
);


--
-- TOC entry 207 (class 1259 OID 11246542)
-- Name: categories; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.categories (
    category_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    title character varying(50) NOT NULL,
    title_uk character varying(50) NOT NULL
);


--
-- TOC entry 208 (class 1259 OID 11246549)
-- Name: categs_quizzes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.categs_quizzes (
    quiz_id uuid NOT NULL,
    category_id uuid NOT NULL
);



--
-- TOC entry 210 (class 1259 OID 11246559)
-- Name: favourite_quizzes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.favourite_quizzes (
    user_id uuid NOT NULL,
    quiz_id uuid NOT NULL
);


--
-- TOC entry 211 (class 1259 OID 11246562)
-- Name: friends; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.friends (
    parent_id uuid NOT NULL,
    friend_id uuid NOT NULL,
    invite_datetime timestamp without time zone NOT NULL,
    accepted_datetime timestamp without time zone
);


--
-- TOC entry 212 (class 1259 OID 11246565)
-- Name: games; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.games (
    game_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    datetime_start timestamp without time zone NOT NULL,
    max_num_of_users integer NOT NULL,
    number_of_questions integer NOT NULL,
    round_duration integer NOT NULL,
    time_additional_points boolean NOT NULL,
    break_time integer NOT NULL,
    access_code character varying(300),
    quiz_id uuid NOT NULL
);


--
-- TOC entry 235 (class 1259 OID 11384304)
-- Name: notification_types; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.notification_types (
    description character varying(50) NOT NULL,
    type_id integer NOT NULL
);


--
-- TOC entry 236 (class 1259 OID 11384391)
-- Name: notification_types_type_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.notification_types_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4129 (class 0 OID 0)
-- Dependencies: 236
-- Name: notification_types_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.notification_types_type_id_seq OWNED BY public.notification_types.type_id;


--
-- TOC entry 214 (class 1259 OID 11246576)
-- Name: one_val_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.one_val_options (
    option_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    value character varying NOT NULL,
    question_id uuid NOT NULL
);


--
-- TOC entry 215 (class 1259 OID 11246583)
-- Name: options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.options (
    option_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    is_correct boolean NOT NULL,
    content character varying NOT NULL,
    image character varying,
    question_id uuid NOT NULL
);


--
-- TOC entry 216 (class 1259 OID 11246590)
-- Name: question_types; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.question_types (
    type_id integer NOT NULL,
    title character varying NOT NULL
);


--
-- TOC entry 217 (class 1259 OID 11246596)
-- Name: questions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.questions (
    question_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    quiz_id uuid NOT NULL,
    title character varying(250) NOT NULL,
    content character varying(250) NOT NULL,
    points integer NOT NULL,
    type_id integer NOT NULL,
    img bytea
);


--
-- TOC entry 218 (class 1259 OID 11246603)
-- Name: quetion_types_type_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.quetion_types_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4130 (class 0 OID 0)
-- Dependencies: 218
-- Name: quetion_types_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.quetion_types_type_id_seq OWNED BY public.question_types.type_id;


--
-- TOC entry 219 (class 1259 OID 11246605)
-- Name: quizzes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.quizzes (
    quiz_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    title character varying(250) NOT NULL,
    description character varying(250),
    ver_creation_datetime timestamp without time zone NOT NULL,
    creator_id uuid,
    activated boolean NOT NULL,
    validated boolean NOT NULL,
    quiz_lang character varying NOT NULL,
    admin_commentary character varying,
    published boolean NOT NULL,
    validator_id uuid,
    validation_date timestamp without time zone,
    image bytea
);


--
-- TOC entry 220 (class 1259 OID 11246612)
-- Name: quizzes_edit; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.quizzes_edit (
    prev_ver_id uuid NOT NULL,
    new_ver_id uuid NOT NULL,
    edit_datetime timestamp without time zone NOT NULL
);


--
-- TOC entry 221 (class 1259 OID 11246615)
-- Name: quizzes_tags; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.quizzes_tags (
    quiz_id uuid NOT NULL,
    tag_id uuid NOT NULL
);


--
-- TOC entry 222 (class 1259 OID 11246618)
-- Name: roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.roles (
    role_id integer NOT NULL,
    role character varying(50) NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 11246621)
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.roles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4131 (class 0 OID 0)
-- Dependencies: 223
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.role_id;


--
-- TOC entry 224 (class 1259 OID 11246623)
-- Name: seq_options; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.seq_options (
    seq_option_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    seq_pos smallint NOT NULL,
    content character varying NOT NULL,
    image character varying,
    question_id uuid NOT NULL
);


--
-- TOC entry 225 (class 1259 OID 11246630)
-- Name: settings; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.settings (
    setting_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    title character varying NOT NULL,
    description character varying NOT NULL,
    default_value character varying,
    privileged boolean DEFAULT false NOT NULL,
    activity_type_id integer,
    title_uk character varying NOT NULL,
    description_uk character varying NOT NULL
);


--
-- TOC entry 226 (class 1259 OID 11246638)
-- Name: tags; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tags (
    tag_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    description character varying(50) NOT NULL,
    description_uk character varying
);


--
-- TOC entry 234 (class 1259 OID 11266052)
-- Name: user_activities; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_activities (
    activity_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    content character varying(250) NOT NULL,
    content_uk character varying(250) NOT NULL,
    datetime timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    type_id integer NOT NULL,
    user_id uuid NOT NULL,
    link_info character varying(50)
);


--
-- TOC entry 227 (class 1259 OID 11246642)
-- Name: user_notifications; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_notifications (
    notif_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    content character varying(250) NOT NULL,
    date_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    user_id uuid NOT NULL,
    seen boolean DEFAULT false NOT NULL,
    content_uk character varying(250) NOT NULL,
    link character varying(300) NOT NULL,
    type_id integer
);


--
-- TOC entry 228 (class 1259 OID 11246651)
-- Name: user_quiz_rating; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_quiz_rating (
    quiz_id uuid,
    user_id uuid,
    rating_points integer NOT NULL
);


--
-- TOC entry 229 (class 1259 OID 11246654)
-- Name: user_settings; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_settings (
    user_id uuid NOT NULL,
    setting_id uuid NOT NULL,
    value character varying NOT NULL
);


--
-- TOC entry 230 (class 1259 OID 11246660)
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    user_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    email character varying(250) NOT NULL,
    password character varying(250),
    username character varying(250) NOT NULL,
    is_activated boolean NOT NULL,
    is_verified boolean NOT NULL,
    last_time_online timestamp without time zone NOT NULL,
    image bytea,
    about_me character varying(300),
    recovery_url character varying(300),
    recovery_sent_time timestamp without time zone,
    activation_url character varying(500),
    date_acc_creation timestamp without time zone NOT NULL,
    role_id integer NOT NULL
);


--
-- TOC entry 231 (class 1259 OID 11246667)
-- Name: users_achievements; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users_achievements (
    user_id uuid NOT NULL,
    achievement_id integer NOT NULL,
    datetime_gained timestamp without time zone NOT NULL,
    times_gained integer DEFAULT 1 NOT NULL
);


--
-- TOC entry 233 (class 1259 OID 11246676)
-- Name: users_games; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users_games (
    game_session_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    user_id uuid,
    game_id uuid NOT NULL,
    score integer NOT NULL,
    is_winner boolean,
    is_creator boolean NOT NULL,
    saved_by_user boolean NOT NULL,
    duration_time integer NOT NULL,
    finished boolean DEFAULT false NOT NULL,
    username character varying(250)
);


--
-- TOC entry 3854 (class 2604 OID 11246681)
-- Name: achievements achievement_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.achievements ALTER COLUMN achievement_id SET DEFAULT nextval('public.achievements_achievement_id_seq'::regclass);


--
-- TOC entry 3882 (class 2604 OID 11384393)
-- Name: notification_types type_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notification_types ALTER COLUMN type_id SET DEFAULT nextval('public.notification_types_type_id_seq'::regclass);


--
-- TOC entry 3863 (class 2604 OID 11246682)
-- Name: question_types type_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.question_types ALTER COLUMN type_id SET DEFAULT nextval('public.quetion_types_type_id_seq'::regclass);


--
-- TOC entry 3866 (class 2604 OID 11246683)
-- Name: roles role_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles ALTER COLUMN role_id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- TOC entry 3884 (class 2606 OID 11246699)
-- Name: achievements achievement_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.achievements
    ADD CONSTRAINT achievement_pk PRIMARY KEY (achievement_id);


--
-- TOC entry 3886 (class 2606 OID 11246701)
-- Name: activity_types activity_types_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.activity_types
    ADD CONSTRAINT activity_types_pkey PRIMARY KEY (type_id);


--
-- TOC entry 3888 (class 2606 OID 11246703)
-- Name: activity_types activity_types_role_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.activity_types
    ADD CONSTRAINT activity_types_role_key UNIQUE (type);


--
-- TOC entry 3890 (class 2606 OID 11246705)
-- Name: announcements announcements_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.announcements
    ADD CONSTRAINT announcements_pkey PRIMARY KEY (announcement_id);


--
-- TOC entry 3892 (class 2606 OID 11246707)
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (category_id);


--
-- TOC entry 3894 (class 2606 OID 11246709)
-- Name: categories categories_title_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_title_key UNIQUE (title);


--
-- TOC entry 3896 (class 2606 OID 11246711)
-- Name: categs_quizzes categs_quizzes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categs_quizzes
    ADD CONSTRAINT categs_quizzes_pkey PRIMARY KEY (category_id, quiz_id);

--
-- TOC entry 3900 (class 2606 OID 11246715)
-- Name: friends friends_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friends_pkey PRIMARY KEY (parent_id, friend_id);


--
-- TOC entry 3902 (class 2606 OID 11246717)
-- Name: games games_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.games
    ADD CONSTRAINT games_pk PRIMARY KEY (game_id);

--
-- TOC entry 3956 (class 2606 OID 11384395)
-- Name: notification_types notification_types_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notification_types
    ADD CONSTRAINT notification_types_pkey PRIMARY KEY (type_id);


--
-- TOC entry 3958 (class 2606 OID 11384310)
-- Name: notification_types notification_types_role_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notification_types
    ADD CONSTRAINT notification_types_role_key UNIQUE (description);


--
-- TOC entry 3906 (class 2606 OID 11246721)
-- Name: one_val_options one_val_options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.one_val_options
    ADD CONSTRAINT one_val_options_pkey PRIMARY KEY (option_id);


--
-- TOC entry 3908 (class 2606 OID 11246723)
-- Name: options options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.options
    ADD CONSTRAINT options_pkey PRIMARY KEY (option_id);


--
-- TOC entry 3914 (class 2606 OID 11246725)
-- Name: questions questions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.questions
    ADD CONSTRAINT questions_pkey PRIMARY KEY (question_id);


--
-- TOC entry 3910 (class 2606 OID 11246727)
-- Name: question_types quetion_types_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.question_types
    ADD CONSTRAINT quetion_types_pkey PRIMARY KEY (type_id);


--
-- TOC entry 3912 (class 2606 OID 11246729)
-- Name: question_types quetion_types_title_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.question_types
    ADD CONSTRAINT quetion_types_title_key UNIQUE (title);


--
-- TOC entry 3938 (class 2606 OID 11246731)
-- Name: user_quiz_rating quiz_rating_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_quiz_rating
    ADD CONSTRAINT quiz_rating_pk UNIQUE (quiz_id, user_id);


--
-- TOC entry 3918 (class 2606 OID 11246733)
-- Name: quizzes_edit quizzes_edit_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quizzes_edit
    ADD CONSTRAINT quizzes_edit_pkey PRIMARY KEY (prev_ver_id, new_ver_id);


--
-- TOC entry 3916 (class 2606 OID 11246735)
-- Name: quizzes quizzes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quizzes
    ADD CONSTRAINT quizzes_pkey PRIMARY KEY (quiz_id);


--
-- TOC entry 3920 (class 2606 OID 11246737)
-- Name: quizzes_tags quizzes_tags_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quizzes_tags
    ADD CONSTRAINT quizzes_tags_pkey PRIMARY KEY (tag_id, quiz_id);


--
-- TOC entry 3922 (class 2606 OID 11246739)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (role_id);


--
-- TOC entry 3924 (class 2606 OID 11246741)
-- Name: roles roles_role_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_role_key UNIQUE (role);


--
-- TOC entry 3926 (class 2606 OID 11246743)
-- Name: seq_options seq_options_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.seq_options
    ADD CONSTRAINT seq_options_pkey PRIMARY KEY (seq_option_id);


--
-- TOC entry 3928 (class 2606 OID 11246745)
-- Name: settings settings_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.settings
    ADD CONSTRAINT settings_pkey PRIMARY KEY (setting_id);


--
-- TOC entry 3930 (class 2606 OID 11246747)
-- Name: settings settings_title_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.settings
    ADD CONSTRAINT settings_title_key UNIQUE (title);


--
-- TOC entry 3932 (class 2606 OID 11246749)
-- Name: tags tags_description_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_description_key UNIQUE (description);


--
-- TOC entry 3934 (class 2606 OID 11246751)
-- Name: tags tags_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_pkey PRIMARY KEY (tag_id);


--
-- TOC entry 3954 (class 2606 OID 11266061)
-- Name: user_activities user_activities_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_activities
    ADD CONSTRAINT user_activities_pk PRIMARY KEY (activity_id);


--
-- TOC entry 3936 (class 2606 OID 11246755)
-- Name: user_notifications user_notifications_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_notifications
    ADD CONSTRAINT user_notifications_pk PRIMARY KEY (notif_id);


--
-- TOC entry 3940 (class 2606 OID 11246757)
-- Name: user_settings user_settings_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_settings
    ADD CONSTRAINT user_settings_pkey PRIMARY KEY (user_id, setting_id);


--
-- TOC entry 3948 (class 2606 OID 11246759)
-- Name: users_achievements users_achievements_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users_achievements
    ADD CONSTRAINT users_achievements_pkey PRIMARY KEY (user_id, achievement_id);


--
-- TOC entry 3942 (class 2606 OID 11246763)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 3952 (class 2606 OID 11246770)
-- Name: users_games users_games_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users_games
    ADD CONSTRAINT users_games_pk PRIMARY KEY (game_session_id);


--
-- TOC entry 3944 (class 2606 OID 11246777)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3946 (class 2606 OID 11246779)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 3987 (class 2606 OID 11246780)
-- Name: users_achievements achievement_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users_achievements
    ADD CONSTRAINT achievement_pk FOREIGN KEY (achievement_id) REFERENCES public.achievements(achievement_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3979 (class 2606 OID 11246785)
-- Name: settings activity_type_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.settings
    ADD CONSTRAINT activity_type_fk FOREIGN KEY (activity_type_id) REFERENCES public.activity_types(type_id) ON UPDATE CASCADE;


--
-- TOC entry 3960 (class 2606 OID 11246790)
-- Name: announcements announcements_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.announcements
    ADD CONSTRAINT announcements_fk FOREIGN KEY (creator_id) REFERENCES public.users(user_id);


--
-- TOC entry 3959 (class 2606 OID 11246795)
-- Name: achievements category_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.achievements
    ADD CONSTRAINT category_id_fk FOREIGN KEY (category_id) REFERENCES public.categories(category_id) ON UPDATE CASCADE;


--
-- TOC entry 3961 (class 2606 OID 11246800)
-- Name: categs_quizzes category_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categs_quizzes
    ADD CONSTRAINT category_pk FOREIGN KEY (category_id) REFERENCES public.categories(category_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3972 (class 2606 OID 11246815)
-- Name: quizzes creator_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quizzes
    ADD CONSTRAINT creator_pk FOREIGN KEY (creator_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3963 (class 2606 OID 11246820)
-- Name: friends friend_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT friend_pk FOREIGN KEY (friend_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3991 (class 2606 OID 11246825)
-- Name: users_games game_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users_games
    ADD CONSTRAINT game_id_fk FOREIGN KEY (game_id) REFERENCES public.games(game_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3974 (class 2606 OID 11246830)
-- Name: quizzes_edit new_ver_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quizzes_edit
    ADD CONSTRAINT new_ver_pk FOREIGN KEY (new_ver_id) REFERENCES public.quizzes(quiz_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3964 (class 2606 OID 11246835)
-- Name: friends parent_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.friends
    ADD CONSTRAINT parent_pk FOREIGN KEY (parent_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3975 (class 2606 OID 11246840)
-- Name: quizzes_edit prev_ver_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quizzes_edit
    ADD CONSTRAINT prev_ver_pk FOREIGN KEY (prev_ver_id) REFERENCES public.quizzes(quiz_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3968 (class 2606 OID 11246845)
-- Name: one_val_options question_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.one_val_options
    ADD CONSTRAINT question_pk FOREIGN KEY (question_id) REFERENCES public.questions(question_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3969 (class 2606 OID 11246850)
-- Name: options question_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.options
    ADD CONSTRAINT question_pk FOREIGN KEY (question_id) REFERENCES public.questions(question_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3978 (class 2606 OID 11246855)
-- Name: seq_options question_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.seq_options
    ADD CONSTRAINT question_pk FOREIGN KEY (question_id) REFERENCES public.questions(question_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3965 (class 2606 OID 11246860)
-- Name: games quiz_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.games
    ADD CONSTRAINT quiz_id_fk FOREIGN KEY (quiz_id) REFERENCES public.quizzes(quiz_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3970 (class 2606 OID 11246865)
-- Name: questions quiz_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.questions
    ADD CONSTRAINT quiz_pk FOREIGN KEY (quiz_id) REFERENCES public.quizzes(quiz_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3976 (class 2606 OID 11246870)
-- Name: quizzes_tags quiz_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quizzes_tags
    ADD CONSTRAINT quiz_pk FOREIGN KEY (quiz_id) REFERENCES public.quizzes(quiz_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3962 (class 2606 OID 11246875)
-- Name: categs_quizzes quiz_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categs_quizzes
    ADD CONSTRAINT quiz_pk FOREIGN KEY (quiz_id) REFERENCES public.quizzes(quiz_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3983 (class 2606 OID 11575212)
-- Name: user_quiz_rating quiz_rating___fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_quiz_rating
    ADD CONSTRAINT quiz_rating___fk FOREIGN KEY (quiz_id) REFERENCES public.quizzes(quiz_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3986 (class 2606 OID 11246885)
-- Name: users role_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT role_pk FOREIGN KEY (role_id) REFERENCES public.roles(role_id);


--
-- TOC entry 3984 (class 2606 OID 11246890)
-- Name: user_settings setting_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_settings
    ADD CONSTRAINT setting_pk FOREIGN KEY (setting_id) REFERENCES public.settings(setting_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3977 (class 2606 OID 11246895)
-- Name: quizzes_tags tag_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quizzes_tags
    ADD CONSTRAINT tag_pk FOREIGN KEY (tag_id) REFERENCES public.tags(tag_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3993 (class 2606 OID 11266062)
-- Name: user_activities type_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_activities
    ADD CONSTRAINT type_fk FOREIGN KEY (type_id) REFERENCES public.activity_types(type_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3981 (class 2606 OID 11385980)
-- Name: user_notifications type_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_notifications
    ADD CONSTRAINT type_fk FOREIGN KEY (type_id) REFERENCES public.notification_types(type_id) ON UPDATE CASCADE;


--
-- TOC entry 3971 (class 2606 OID 11246905)
-- Name: questions type_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.questions
    ADD CONSTRAINT type_pk FOREIGN KEY (type_id) REFERENCES public.question_types(type_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3980 (class 2606 OID 11246910)
-- Name: user_notifications user_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_notifications
    ADD CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3994 (class 2606 OID 11266067)
-- Name: user_activities user_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_activities
    ADD CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3992 (class 2606 OID 11246920)
-- Name: users_games user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users_games
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3985 (class 2606 OID 11246930)
-- Name: user_settings user_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_settings
    ADD CONSTRAINT user_pk FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3988 (class 2606 OID 11246935)
-- Name: users_achievements user_pk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users_achievements
    ADD CONSTRAINT user_pk FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3982 (class 2606 OID 11246945)
-- Name: user_quiz_rating user_quiz_rating_users_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_quiz_rating
    ADD CONSTRAINT user_quiz_rating_users_user_id_fk FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE;


--
-- TOC entry 3973 (class 2606 OID 11246950)
-- Name: quizzes validators_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quizzes
    ADD CONSTRAINT validators_fk FOREIGN KEY (validator_id) REFERENCES public.users(user_id) ON UPDATE CASCADE;


-- Completed on 2020-06-02 20:42:04

--
-- PostgreSQL database dump complete
--

