package net.dreamfteam.quiznet.service.impl;


import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.constants.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.dao.UserDao;
import net.dreamfteam.quiznet.data.entities.ActivityType;
import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.data.entities.UserFriendInvitation;
import net.dreamfteam.quiznet.data.entities.UserView;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ActivitiesService;
import net.dreamfteam.quiznet.service.EmailService;
import net.dreamfteam.quiznet.service.NotificationService;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.DtoActivity;
import net.dreamfteam.quiznet.web.dto.DtoNotification;
import net.dreamfteam.quiznet.web.dto.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Slf4j
@Service
@PropertySource("classpath:application.properties")
public class UserServiceImpl implements UserService {

    @Value("${admin.reg.template}")
    private String adminRegTemplate;

    @Value("${user.reg.template}")
    private String userRegTemplate;

    private final EmailService mailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDao userDao;
    private final ActivitiesService activitiesService;
    private final IAuthenticationFacade authenticationFacade;
    private final NotificationService notificationService;

    @Autowired
    public UserServiceImpl(EmailService mailService, BCryptPasswordEncoder bCryptPasswordEncoder,
                           UserDao userDao, ActivitiesService activitiesService,
                           IAuthenticationFacade authenticationFacade, NotificationService notificationService) {
        this.mailService = mailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDao = userDao;
        this.activitiesService = activitiesService;
        this.authenticationFacade = authenticationFacade;
        this.notificationService = notificationService;
    }

    @Override
    public User save(User newUser) throws ValidationException {

        if (userDao.getByUsername(newUser.getUsername()) != null) {
            throw new ValidationException(Constants.USERNAME_TAKEN);
        }

        if (userDao.getByEmail(newUser.getEmail()) != null) {
            throw new ValidationException(Constants.EMAIl_TAKEN);
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

        try {
            mailService.sendMailMessage(mailService.createBasicRegMail(savedUser), userRegTemplate);
        } catch (MessagingException e) {
            log.error(String.format(Constants.REG_MAIL_NOT_SENT, user.getUsername()), e);
        }

        return savedUser;
    }


    @Override
    public User saveAdmin(String currentUser, User user) {

        User newUser = User.builder()
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .role(user.getRole())
                .creationDate(Calendar.getInstance().getTime())
                .username(user.getUsername())
                .activationUrl(bCryptPasswordEncoder.encode(user.getUsername() + user.getEmail()))
                .build();

        User savedUser = userDao.save(newUser);

        Mail mail = mailService.createBasicRegMail(savedUser);
        Map<String, Object> model = mail.getModel();
        model.put(Constants.MAIL_MODEL_CREATOR, currentUser);
        model.put(Constants.MAIL_MODEL_ROLE, savedUser.getRole().toString()
                .substring(Constants.ROLE_PREFIX_LENGTH).toLowerCase());

        try {
            mailService.sendMailMessage(mail, adminRegTemplate);
        } catch (MessagingException e) {
            log.error(String.format(Constants.REG_MAIL_NOT_SENT, user.getUsername()), e);
        }

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
            throw new ValidationException(Constants.PASSWORD_NOT_CORRECT);
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

    /* Returns friend invitations list by user id.
     * Parameter "isIncoming":
     * - true - when the request is aimed on the incoming invitations list;
     * - false - when the request is aimed on the outgoing invitations list.
     * */
    @Override
    public List<UserFriendInvitation> getFriendInvitationsByUserId(int startIndex, int amount, String userId,
                                                                   boolean isIncoming) {
        if (isIncoming) {
            return userDao.getFriendInvitationsIncomingByUserId(startIndex, amount, userId);
        }
        return userDao.getFriendInvitationsOutgoingByUserId(startIndex, amount, userId);
    }

    /* Returns the size of invitations list by user id.
     * Parameter "isIncoming":
     * - true - when the request is aimed on the incoming invitations list size;
     * - false - when the request is aimed on the outgoing invitations list size.
     * */
    @Override
    public int getFriendInvitationsTotalSize(String userId, boolean isIncoming) {
        if (isIncoming) {
            return userDao.getFriendInvitationsIncomingTotalSize(userId);
        }
        return userDao.getFriendInvitationsOutgoingTotalSize(userId);
    }

    @Override
    public void inviteToBecomeFriends(String parentId, String targetId, boolean toInvite) throws ValidationException {
        if (parentId.equals(targetId)) {
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
        if (userDao.processOutgoingFriendInvitation(parentId, targetId, toInvite)) {
            String parentUsername = getById(parentId).getUsername();
            //adding notification
            notificationService.insert(DtoNotification.builder()
                    .link("")
                    .typeId(1)
                    .content("You've got a friend request from " + parentUsername)
                    .contentUk("Ви отримали запит на дружбу від " + parentUsername)
                    .userId(targetId)
                    .build());
        }


    }

    @Override
    public void proceedInvitation(String parentId, String targetId, boolean toAccept) {
        User parent = getById(parentId);
        if (isNull(parent)) {
            throw new ValidationException("Parent user doesn't exist");
        }
        if (toAccept) {
            if (userDao.acceptInvitation(parentId, targetId) > 0) {
                DtoActivity activity = DtoActivity
                        .builder()
                        .content("Added new friend: " + parent.getUsername())
                        .contentUk("Додав/ла нового друга: " + parent.getUsername())
                        .userId(targetId)
                        .activityType(ActivityType.FRIENDS_RELATED)
                        .linkInfo(parent.getUsername())
                        .build();
                activitiesService.addActivityForUser(activity);

                activity.setContent("Added new friend: " + authenticationFacade.getUsername());
                activity.setContentUk("Додав/ла нового друга: " + authenticationFacade.getUsername());
                activity.setUserId(parentId);
                activity.setLinkInfo(authenticationFacade.getUsername());
                activitiesService.addActivityForUser(activity);
            }
        } else {
            userDao.rejectInvitation(parentId, targetId);
        }
    }

    @Override
    public void removeFriend(String targetId, String thisId) {
        userDao.removeFriend(targetId, thisId);
    }

    @Override
    public List<User> getPopularCreators() {
        return userDao.getPopularCreators();
    }

    @Override
    public List<User> getPrivilegedUsers() {
        return userDao.getPrivilegedUsers();
    }


}

