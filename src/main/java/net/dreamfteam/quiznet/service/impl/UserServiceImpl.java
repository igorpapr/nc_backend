package net.dreamfteam.quiznet.service.impl;


import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.dao.UserDao;
import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.MailService;
import net.dreamfteam.quiznet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Value("${reg.url.activate}")
    private String REG_URL_ACTIVATE;

    private MailService mailService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserDao userDao;

    @Autowired
    public UserServiceImpl(MailService mailService, BCryptPasswordEncoder bCryptPasswordEncoder, UserDao userDao) {
        this.mailService = mailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDao = userDao;
    }

    @Override
    public User save(User newUser) throws ValidationException {

        if (userDao.getByUsername(newUser.getUsername()) != null) {
            throw new ValidationException("User with such username already exist");
        }

        if (userDao.getByEmail(newUser.getEmail()) != null) {
            throw new ValidationException("User with such email already exist");
        }

        User user = User.builder()
                .password(bCryptPasswordEncoder.encode(newUser.getPassword()))
                .email(newUser.getEmail())
                .role(Role.ROLE_USER)
                .creationDate(Calendar.getInstance().getTime())
                .username(newUser.getUsername())
                .activationUrl(bCryptPasswordEncoder.encode(newUser.getUsername() + newUser.getEmail()))
                .build();

        User savedUser = userDao.save(user);

        mailService.sendMail(savedUser.getEmail(), Constants.REG_MAIL_SUBJECT, Constants.REG_MAIL_ARTICLE,
                Constants.REG_MAIL_MESSAGE + REG_URL_ACTIVATE + savedUser.getActivationUrl());

        return savedUser;
    }

    @Override
    public User saveAdmin(User user) {

        User newUser = User.builder()
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .role(user.getRole())
                .creationDate(Calendar.getInstance().getTime())
                .username(user.getUsername())
                .activationUrl(bCryptPasswordEncoder.encode(user.getUsername() + user.getEmail()))
                .build();

        User savedUser = userDao.save(newUser);

        mailService.sendMail(savedUser.getEmail(), Constants.REG_ADMIN_MAIL_SUBJECT, Constants.REG_ADMIN_MAIL_ARTICLE,
                Constants.REG_ADMIN_MAIL_MESSAGE + REG_URL_ACTIVATE + savedUser.getActivationUrl());

        return savedUser;
    }

    @Override
    public User getById(String id) {
        return userDao.getById(id);
    }

    @Override
    public User getByActivationUrl(String activationUrl) {
        return userDao.getByActivationUrl(activationUrl);
    }

    @Override
    public User getByRecoverUrl(String recoverUrl) {
        return userDao.getByRecoverUrl(recoverUrl);
    }

    @Override
    public List<User> getAllByRole(Role role, String currentUserId) {
        if (role.equals(Role.ROLE_USER)) {
            return userDao.getAllByRoleUser(currentUserId);
        } else return userDao.getAll(currentUserId);
    }

    @Override
    public User getByUsername(String username) {
        return userDao.getByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return userDao.getByEmail(email);
    }

    @Override
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void checkCorrectPassword(User user, String password) {
        boolean matches = bCryptPasswordEncoder.matches(password, user.getPassword());
        if (!matches) {
            throw new ValidationException("Not correct password");
        }
    }

    @Override
    public List<User> getBySubStr(String substr, Role role, String currentUserId) {
        if (role.equals(Role.ROLE_USER)) {
            return userDao.getBySubStrAndRoleUser(substr, currentUserId);
        } else return userDao.getBySubStr(substr, currentUserId);
    }
}

