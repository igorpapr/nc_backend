package net.dreamfteam.quiznet.service.impl;


import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.dao.UserDao;
import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.data.entities.UserFriendInvitation;
import net.dreamfteam.quiznet.data.entities.UserView;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.MailService;
import net.dreamfteam.quiznet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Value("${reg.url.activate}")
    private String REG_URL_ACTIVATE;

    final private MailService mailService;
    final private BCryptPasswordEncoder bCryptPasswordEncoder;
    final private UserDao userDao;

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
    public List<User> getAllByRole(Role role) {
        if (role.equals(Role.ROLE_USER)) {
            return userDao.getAllByRoleUser();
        } else return userDao.getAll();
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
    public User getFriendsRelations(User targetUser, String thiUserId) {
        return userDao.getFriendsRelations(targetUser, thiUserId);
    }

    @Override
    public void checkCorrectPassword(User user, String password) {
        boolean matches = bCryptPasswordEncoder.matches(password, user.getPassword());
        if (!matches) {
            throw new ValidationException("Not correct password");
        }
    }

    @Override
    public List<User> getBySubStr(String substr, Role role) {
        if (role.equals(Role.ROLE_USER)) {
            return userDao.getBySubStrAndRoleUser(substr);
        } else return userDao.getBySubStr(substr);
    }

    @Override
    public int getFriendsTotalSize(String userId) {
        return userDao.getFriendsTotalSize(userId);
    }

    @Override
    public List<UserView> getFriendsListByUserId(int startIndex, int amount, String userId) {
        return userDao.getFriendsByUserId(startIndex, amount, userId);
    }

    @Override
    public List<UserFriendInvitation> getFriendInvitationsListByUserId(int startIndex, int amount, String userId) {
        return userDao.getFriendInvitationsByUserId(startIndex, amount, userId);
    }

    @Override
    public int getFriendInvitationsTotalSize(String userId) {
        return userDao.getFriendInvitationsTotalSize(userId);
    }

    @Override
    public void inviteToBecomeFriends(String parentId, String targetId, boolean toInvite) throws ValidationException{
        if(parentId.equals(targetId)){
            throw new ValidationException("Can't invite to friends yourself");
        }
        User target = getById(targetId);
        if (isNull(target)) {
            System.out.println("Friend invitation target doesn't exist: " + targetId);
            throw new ValidationException("Target user doesn't exist");
        }
        if (target.getRole() != Role.ROLE_USER) {
            System.out.println("Friend invitation target has bad role: " + target.getRole());
            throw new ValidationException("Can't perform this action with user of such role: " + target.getRole());
        }
        userDao.addFriendInvitation(parentId, targetId, toInvite);
    }

    @Override
    public void proceedInvitation(String parentId, String targetId, boolean toAccept) {
        User target = getById(targetId);
        if(isNull(target)){
            throw new ValidationException("Target user doesn't exist");
        }
        if (target.getRole() != Role.ROLE_USER){
            throw new ValidationException("Can't perform this action with user of such role: " + target.getRole());
        }
        if(toAccept){
            userDao.acceptInvitation(parentId, targetId);
        }
        else{
            userDao.rejectInvitation(parentId, targetId);
        }
    }

    @Override
    public void removeFriend(String targetId, String thisId) {
        userDao.removeFriend(targetId, thisId);
    }


}

