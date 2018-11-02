package com.global.servicebase.user.services;

import com.global.core.SsdcCrudService;
import com.global.jdbc.exceptions.EntityNotFoundException;
import com.global.jdbc.factories.RepositoryFactory;
import com.global.servicebase.user.model.Role;
import com.global.servicebase.user.model.UserSearchForm;
import com.global.servicebase.user.model.UserResponse;
import com.global.servicebase.user.model.User;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.global.servicebase.services.EmailTemplateService;
import com.global.servicebase.services.MailService;
import com.global.servicebase.globalexception.UserNotFoundException;
import java.util.*;
import org.apache.commons.lang3.StringUtils;

@Service
public class UserService extends SsdcCrudService<Long, User> {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public MailService mailService;

    @Autowired
    public RoleService roleService;

    @Autowired
    public EmailTemplateService emailTemplateService;

    @Autowired
    public UserService(RepositoryFactory repositoryFactory) {
        this.repository = repositoryFactory.create(User.class);
    }

    public Page<User> getPage(int page, int limit, String where) {
        return this.repository.search(where, new PageRequest(page, limit));
    }

    public List<User> findByQuery(String where) {
        return this.repository.search(where);
    }

    public User findByUserName(String username) {
        List<User> userList = this.repository.search("user_name = ?", username);
        if (userList.isEmpty()) {
            throw new EntityNotFoundException("users", username);
        }
        return userList.get(0);
    }

    public void sendForgotPassword(String redirectUrl, String username) throws UserNotFoundException {
        User user = findByUserName(username);
        if (user == null) {
            throw new UserNotFoundException("No user with username " + username);
        }
        user.forgotPwdToken = UUID.randomUUID().toString();
        user.forgotPwdTokenRequested = System.currentTimeMillis();
        update(user.id, user);

        //send email forgot password template: <p>Please click <a href="http://ump-dev/changeForgotPassword?userId=%d&token=%s">here</a> to recover your password</p>
        String mailContent = String.format(emailTemplateService.get("user.forgotPassword").value, user.id, user.forgotPwdToken, redirectUrl, user.forgotPwdToken);
        mailService.sendMail(user.email, "Forgot Password", mailContent, null, null);
    }

    public void sendForgotPasswordWithEmail(String redirectUrl, String email) throws UserNotFoundException {
        List<User> users = getAll();
        User user = null;
        for (User u : users) {
            if (email.toLowerCase().equals(u.email.toLowerCase())) {
                user = u;
                break;
            }
        }
        if (user == null) {
            throw new UserNotFoundException("No user with email " + email);
        }
        user.forgotPwdToken = UUID.randomUUID().toString();
        user.forgotPwdTokenRequested = System.currentTimeMillis();
        update(user.id, user);

        //send email forgot password template: <p>Please click <a href="http://ump-dev/changeForgotPassword?userId=%d&token=%s">here</a> to recover your password</p>
        String mailContent = String.format(emailTemplateService.get("user.forgotPassword").value, user.id, user.forgotPwdToken, redirectUrl, user.forgotPwdToken);
        mailService.sendMail(user.email, "Forgot Password", mailContent, null, null);
    }

    private void sendRandomPasswordEmail(User user) throws UserNotFoundException {
        String randomPassword = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        user.setEncryptedPassword(randomPassword);

        String mailContent = String.format(emailTemplateService.get("user.randomPassword").value, user.userName, randomPassword);
        mailService.sendMail(user.email, "Random Password", mailContent, null, null);
    }

    @Override
    public void beforeCreate(User user) {
        user = updateOperationIds(user);
        sendRandomPasswordEmail(user);
        super.beforeCreate(user);
    }

    @Override
    public void beforeUpdate(Long id, User user) {
        User userOld = get(id);
        // Send email to new email
        if (user.email != null && !userOld.email.equals(user.email)) {
            sendRandomPasswordEmail(user);
        }
        user = updateOperationIds(user);
        super.beforeUpdate(id, user);
    }

    public User resetPassword(Long id) {
        User user = get(id);
        if (user == null) {
            throw new UserNotFoundException("No user with id " + id);
        }

        String randomPassword = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        user.setEncryptedPassword(randomPassword);

        update(id, user);

        String mailContent = String.format(emailTemplateService.get("user.resetPassword").value, user.userName, randomPassword);
        mailService.sendMail(user.email, "Reset Password", mailContent, null, null);

        return user;
    }

    public User createPassword(Long id) {
        User user = get(id);
        if (user == null) {
            throw new UserNotFoundException("No user with id " + id);
        }

        String randomPassword = "global!@#123";
        user.setEncryptedPassword(randomPassword);

        update(id, user);

        String mailContent = String.format(emailTemplateService.get("user.resetPassword").value, user.userName, randomPassword);
        mailService.sendMail(user.email, "Reset Password", mailContent, null, null);

        return user;
    }

    public Boolean checkPassword(String username, String currentPassword) {
        User user = findByUserName(username);
        return BCrypt.checkpw(currentPassword, user.password);
    }

    public User changePassword(String username, String currentPassword, String newPassword) {
        if (checkPassword(username, currentPassword)) {
            User user = findByUserName(username);
            user.setEncryptedPassword(newPassword);
            update(user.id, user);
            return user;
        } else {
            return null;
        }
    }

    public Boolean checkToken(Long id, String token) {
        User user = get(id);
        return token.equals(user.forgotPwdToken);
    }

    public Boolean changePasswordWithToken(Long userId, String token, String newPassword) {
        if (checkToken(userId, token)) {
            User user = get(userId);
            if (user != null) {
                user.setEncryptedPassword(newPassword);
                user.forgotPwdToken = null;
                user.forgotPwdTokenRequested = null;
                update(userId, user);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean getByName(String name) {
        int count = this.repository.search("user_name=?", name).size();
        if (count == 0) {
            return false;
        }
        return true;
    }

    private User updateOperationIds(User user) {
        Set<String> operationIds = new HashSet<>();
        for (String roleId : user.roleIds) {
            if (!roleId.isEmpty()) {
                Role role = roleService.get(Long.valueOf(roleId));
                if (role != null) {
                    operationIds.addAll(role.operationIds);
                }
            }
        }
        user.operationIds = operationIds;

        return user;
    }

    public List<User> checkByRoleId(String roleId) {
        if (!StringUtils.startsWith(roleId, "\"")) {
            roleId = "\"" + roleId;
        }
        if (!StringUtils.endsWith(roleId, "\"")) {
            roleId += "\"";
        }
        return this.repository.search("role_ids LIKE '%" + roleId + "%'");
    }

    public int updateByRoleId(String roleId) {
        List<User> list = checkByRoleId(roleId);
        if (list == null || list.isEmpty()) {
            return 0;
        }
        for (User user : list) {
            updateOperationIds(user);
        }
        return list.size();
    }

    public long countByQuery(String query) {
        if (Strings.isNullOrEmpty(query)) {
            return repository.count(query);
        } else {
            return repository.count();
        }
    }

    @Override
    public void afterDelete(User entity) {
        super.afterDelete(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void afterCreate(User entity) {
        super.afterCreate(entity); //To change body of generated methods, choose Tools | Templates.
    }

    public String generateQuery(UserSearchForm scUserSearchForm) {

        Set<String> parameters = new HashSet<>();
        if (!org.elasticsearch.common.Strings.isNullOrEmpty(scUserSearchForm.fullName)) {
            parameters.add(" full_name LIKE '%" + scUserSearchForm.fullName + "%'");
        }
        if (!org.elasticsearch.common.Strings.isNullOrEmpty(scUserSearchForm.description)) {
            parameters.add(" description LIKE '%" + scUserSearchForm.description + "%'");
        }
        if (!org.elasticsearch.common.Strings.isNullOrEmpty(scUserSearchForm.userName)) {
            parameters.add(" user_name LIKE '%" + scUserSearchForm.userName + "%'");
        }
        if (!org.elasticsearch.common.Strings.isNullOrEmpty(scUserSearchForm.email)) {
            parameters.add(" email LIKE '%" + scUserSearchForm.email + "%'");
        }
        if (!org.elasticsearch.common.Strings.isNullOrEmpty(scUserSearchForm.phoneNumber)) {
            parameters.add(" phone LIKE '%" + scUserSearchForm.phoneNumber + "%'");
        }

        if (scUserSearchForm.userId != null) {
            parameters.add(" id = " + scUserSearchForm.userId);
        }

        if (scUserSearchForm.roles != null && !scUserSearchForm.roles.isEmpty()) {
            Set<String> tmpParameters = new HashSet<>();
            for (Integer roleId : scUserSearchForm.roles) {
                tmpParameters.add(" role_ids LIKE '%\"" + roleId + "\"%'");
            }
            parameters.add("(" + String.join(" OR ", tmpParameters) + ")");
        }

        parameters.add(" id > 0 ");
        String query = String.join(" AND ", parameters);
        return query;
    }

    public List<UserResponse> search(UserSearchForm scUserSearchForm) {
        List<UserResponse> userResponses = new ArrayList<>();
        String query = generateQuery(scUserSearchForm);
        List<User> users = new ArrayList<>();

        if (scUserSearchForm.limit != null && scUserSearchForm.page != null) {
            users = getPage(scUserSearchForm.page - 1, scUserSearchForm.limit, query).getContent();
        } else {
            users = findByQuery(query);
        }
        // convert to UserResponse
        for (User user : users) {
            userResponses.add(convertFromUserToSCUser(user));
        }
        return userResponses;
    }

    public long count(UserSearchForm scUserSearchForm) {
        String query = generateQuery(scUserSearchForm);
        return countByQuery(query);
    }

    public User convertFromSCUserToUser(UserResponse userResponse) {

        User user = new User();
        user.userName = userResponse.userName;
        user.email = userResponse.email;
        user.phone = userResponse.phoneNumber;
        user.password = userResponse.password;
        user.fullName = userResponse.fullName;
        user.description = userResponse.description;
        user.id = userResponse.userId;
        user.branchId = userResponse.branchId;
        user.branchName = userResponse.branchName;

        // Set role data
        for (Role role : userResponse.roles) {
            user.roleIds.add(String.valueOf(role.id));
            user.roleNames.add(role.name);
            user.operationIds.addAll(role.operationIds);
        }

        return user;
    }

    public UserResponse convertFromUserToSCUser(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.userName = user.userName;
        userResponse.fullName = user.fullName;
        userResponse.phoneNumber = user.phone;
        userResponse.email = user.email;
        userResponse.description = user.description;
        userResponse.password = user.password;
        userResponse.fullName = user.fullName;
        userResponse.operationIds = user.operationIds;
        userResponse.branchId = user.branchId;
        userResponse.branchName = user.branchName;
        try {
            userResponse.userId = user.id;
        } catch (Exception ex) {
            logger.error(String.format("ERROR convertFromUserToSCUser {%s}: ", new Gson().toJson(user)), ex);
            userResponse.userId = Long.parseLong(String.valueOf(user.id));
        }

        Set<Role> roleResponses = new HashSet<>();
        for (String roleId : user.roleIds) {
            roleResponses.add(roleService.get(Long.valueOf(roleId)));
        }
        userResponse.roles = roleResponses;
        return userResponse;
    }
}
