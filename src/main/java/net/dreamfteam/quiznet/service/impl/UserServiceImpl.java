package net.dreamfteam.quiznet.service.impl;



import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.dao.UserDao;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.MailService;
import net.dreamfteam.quiznet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

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
                .creationDate(Calendar.getInstance().getTime())
                .username(newUser.getUsername())
                .activationUrl(bCryptPasswordEncoder.encode(newUser.getUsername() + newUser.getEmail()))
                .build();

        User dtoUser = userDao.save(user);

        mailService.sendMail(dtoUser.getEmail(), Constants.REG_MAIL_SUBJECT, Constants.REG_MAIL_ARTICLE,
                Constants.REG_MAIL_MESSAGE + Constants.REG_URL_ACTIVATE + dtoUser.getActivationUrl());

        newUser.setActivationUrl(dtoUser.getActivationUrl());
        newUser.setCreationDate(dtoUser.getCreationDate());
        newUser.setId(dtoUser.getId());
        newUser.setEmail(user.getEmail());
        newUser.setPassword("******");

        return newUser;
    }

    @Override
    public User getById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public User getByActivationUrl(String activationUrl) {
        return userDao.getByActivationUrl(activationUrl);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
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
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

}

