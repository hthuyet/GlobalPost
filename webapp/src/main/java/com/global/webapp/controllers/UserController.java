package com.global.webapp.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.global.webapp.clients.RoleClient;
import com.global.webapp.clients.UserClient;
import com.global.webapp.models.Role;
import com.global.webapp.models.User;
import com.global.webapp.models.searchForm.UserSearchForm;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class UserController {

    private static final String USER_PAGE = "users/users";
    private static final String USER_PAGE_ADD = "users/user_add";
    private static final String USER_PAGE_EDIT = "users/user_edit";
    private static final String USER_INFO = "users/user_info";
    private static final String USER_CHANGE_PASSWORD = "users/user_change_password";
    private static final String CHANGE_FORGOT_PASSWORD_PAGE = "users/change_forgot_password";
    private static final String CHANGE_FORGOT_PASSWORD_CONFIRM_PAGE = "users/change_forgot_password_confirm";

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    //<editor-fold desc="Autowired">
    @Autowired
    UserClient userClient;

    @Autowired
    RoleClient roleClient;

    @Autowired
    protected HttpSession session;
    //</editor-fold>

    @Autowired
    HttpSession httpSession;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ONE:USER:READ')")
    public String index() {
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to users page", "", "");
        return USER_PAGE;
    }

    @GetMapping("/users/user-add")
    @PreAuthorize("hasAuthority('ONE:USER:CREATE')")
    public String indexAdd() {
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to create new user page", "", "");
        return USER_PAGE_ADD;
    }

    @GetMapping("/users/change-password/{userName}")
    @PreAuthorize("hasAuthority('ONE:USER:UPDATE')")
    public String indexChangePassword(Model model, @PathVariable String userName) {
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to user change password page", "", "");
        return USER_CHANGE_PASSWORD;
    }

    @GetMapping("/users/search")
    @PreAuthorize("hasAuthority('ONE:USER:READ')")
    @ResponseBody
    public Map<String, Object> getListUser(@RequestParam Map<String, String> params) {
        Map<String, Object> response = new HashMap<>();

        UserSearchForm userSearchForm = new UserSearchForm();
        userSearchForm.currentUserName = session.getAttribute("username").toString();
        userSearchForm.limit = Integer.valueOf(params.get("limit"));
        userSearchForm.page = Integer.valueOf(params.get("page"));
        if (params.get("userName") != null && !params.get("userName").isEmpty()) {
            userSearchForm.userName = params.get("userName");
        }

        if (params.get("fullName") != null && !params.get("fullName").isEmpty()) {
            userSearchForm.fullName = params.get("fullName");
        }

        if (params.get("phoneNumber") != null && !params.get("phoneNumber").isEmpty()) {
            userSearchForm.phoneNumber = params.get("phoneNumber");
        }

        if (params.get("email") != null && !params.get("email").isEmpty()) {
            userSearchForm.email = params.get("email");
        }

        if (params.get("description") != null && !params.get("description").isEmpty()) {
            userSearchForm.description = params.get("description");
        }

        if (params.get("role") != null && !params.get("role").isEmpty()) {
            Set<Integer> roles = new HashSet<Integer>();
            String[] roleAddIds = params.get("role").split(",");
            if (roleAddIds.length > 0) {
                for (String item : roleAddIds) {
                    roles.add(Integer.parseInt(item));
                }
            }
            userSearchForm.roles = roles;
        }

        User[] listUser = userClient.search(userSearchForm);
        int totalItem = userClient.count(userSearchForm);

        response.put("userList", listUser);
        response.put("totalItem", totalItem);
        response.put("userLogin", httpSession.getAttribute("username"));
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "search users on list users page", "", "");
        return response;
    }

    @GetMapping("/users/get-data-search")
    @PreAuthorize("hasAuthority('ONE:USER:READ')")
    @ResponseBody
    public Map<String, Object> getListSubscribers() {
        Map<String, Object> response = new HashMap<>();
        JsonArray roles = new JsonArray();

        Role[] listRoles = roleClient.search();
        for (Role role : listRoles) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", role.id);
            jsonObject.addProperty("name", role.name);
            roles.add(jsonObject);
        }

        response.put("roles", roles.toString());
        return response;
    }

    @GetMapping("/users/delete")
    @PreAuthorize("hasAuthority('ONE:USER:DELETE')")
    @ResponseBody
    public String delete(@RequestParam Map<String, String> params) {
        try {
            String userNames = params.get("userNames");
            JsonArray array = new Gson().fromJson(userNames, JsonArray.class);
            for (int i = 0; i < array.size(); i++) {
                if (!httpSession.getAttribute("username").equals(array.get(i).getAsString())) {
                    userClient.delete(array.get(i).getAsString());
                    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "delete user " + array.get(i).getAsString(), "", "");
                }
            }
            return "200";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "400";
        }
    }

    @GetMapping("/users/forgot-password")
    @PreAuthorize("hasAuthority('ONE:USER:RESET_PASSWORD')")
    @ResponseBody
    public String forgotPassword(@RequestParam Map<String, String> params) {
        try {
            UserSearchForm userSearchForm = new UserSearchForm();
            userSearchForm.userName = params.get("reUserName");
            userSearchForm.email = params.get("reEmail");
            userSearchForm.redirectUrl = params.get("redirectUrl");
            userClient.forgotPassword(userSearchForm);
            logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "forgot password username " + params.get("reUserName"), "", "");
            return "200";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "400";
        }
    }

    @GetMapping("/users/add-user")
    @PreAuthorize("hasAuthority('ONE:USER:CREATE')")
    @ResponseBody
    public Object addUser(@RequestParam Map<String, String> params) {

        try {

            UserSearchForm userSearchForm = new UserSearchForm();
            userSearchForm.currentUserName = session.getAttribute("username").toString();
            userSearchForm.limit = 20;
            userSearchForm.page = 1;
            userSearchForm.userName = params.get("userName");
            User[] listUser = userClient.search(userSearchForm);
            if (listUser != null && listUser.length > 0) {
                return "201";
            }

            userSearchForm.userName = "";
            userSearchForm.email = params.get("email");
            User[] listUser1 = userClient.search(userSearchForm);
            if (listUser1 != null && listUser1.length > 0) {
                return "202";
            }

            Set<Role> roles = new HashSet<Role>();

            User user = new User();
            user.userName = params.get("userName");
            user.fullName = params.get("fullName");
            user.email = params.get("email");
            user.description = params.get("description");
            user.phoneNumber = params.get("phoneNumber");

            JsonObject jsonObject = new Gson().fromJson(params.get("deviceGroup"), JsonObject.class);
            JsonArray deviceGroupAsJsonArray = jsonObject.get("role").getAsJsonArray();
            for (int x = 0; x < deviceGroupAsJsonArray.size(); x++) {
                String asString = deviceGroupAsJsonArray.get(x).getAsString();
                String[] split = asString.split("~");
                Role role = new Role();
                role.id = Long.valueOf(split[0]);
                role.name = split[1];
                role.operationIds = roleClient.get(split[0]).operationIds;
                roles.add(role);
            }
            user.roles = roles;

            user = userClient.create(user);
            logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "create new user", "", "");
            return user;
        } catch (Exception e) {
            return "400";
        }
    }

    @GetMapping("users/edit/{id}")
    @PreAuthorize("hasAuthority('ONE:USER:UPDATE')")
    public String indexGroupEdit(Model model, @PathVariable("id") String id) {
        UserSearchForm userSearchForm = new UserSearchForm();
        userSearchForm.userId = Long.valueOf(id);
        User[] users = userClient.search(userSearchForm);
        model.addAttribute("id", id);
        model.addAttribute("userName", users[0].userName);
        model.addAttribute("fullName", users[0].fullName);
        model.addAttribute("email", users[0].email);
        model.addAttribute("phoneNumber", users[0].phoneNumber);
        model.addAttribute("description", users[0].description);

        Set<Role> roles = users[0].roles;
        JsonArray jsonArray = new JsonArray();
        for (Role role : roles) {
            jsonArray.add(role.id + "~" + role.name);
        }
        model.addAttribute("role", jsonArray.toString());

        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to edit user page", "", "");
        return USER_PAGE_EDIT;
    }


    @GetMapping("/users/edit")
    @PreAuthorize("hasAuthority('ONE:USER:UPDATE')")
    @ResponseBody
    public String editUser(@RequestParam Map<String, String> params) {

        try {
            UserSearchForm userSearchForm = new UserSearchForm();
            userSearchForm.currentUserName = session.getAttribute("username").toString();
            userSearchForm.limit = 20;
            userSearchForm.page = 1;

            if(Boolean.valueOf(params.get("checkExistedUsername"))){
                userSearchForm.userName = params.get("userName");
                User[] listUser = userClient.search(userSearchForm);
                if (listUser != null && listUser.length > 0) {
                    return "201";
                }
            }

            if(Boolean.valueOf(params.get("checkExistedEmail"))){
                userSearchForm.userName = null;
                userSearchForm.email = params.get("email");
                User[] listUser1 = userClient.search(userSearchForm);
                if (listUser1 != null && listUser1.length > 0) {
                    return "202";
                }
            }

            Set<Role> roles = new HashSet<Role>();

            User user = new User();
            user.userId = Long.valueOf(params.get("userId"));
            user.userName = params.get("userName");
            user.fullName = params.get("fullName");
            user.email = params.get("email");
            user.description = params.get("description");
            user.phoneNumber = params.get("phoneNumber");

            JsonObject jsonObject = new Gson().fromJson(params.get("deviceGroup"), JsonObject.class);
            JsonArray deviceGroupAsJsonArray = jsonObject.get("role").getAsJsonArray();
            for (int x = 0; x < deviceGroupAsJsonArray.size(); x++) {
                String asString = deviceGroupAsJsonArray.get(x).getAsString();
                String[] split = asString.split("~");
                Role role = new Role();
                role.id = Long.valueOf(split[0]);
                role.name = split[1];
                role.operationIds = roleClient.get(split[0]).operationIds;
                roles.add(role);
            }
            user.roles = roles;

            userClient.update(params.get("userNameOld"), user);
            logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "update user " + user.userName, "", "");
            return "200";
        } catch (Exception e) {
            return "400";
        }
    }

    @GetMapping("/user-information/{userName}")
    @PreAuthorize("hasAuthority('ONE:USER:READ')")
    public String userInformation(Model model, @PathVariable String userName) {
        User byUsername = userClient.getByUsername(userName);
        model.addAttribute("userId", byUsername.userId);
        model.addAttribute("userName", byUsername.userName);
        model.addAttribute("fullName", byUsername.fullName);
        model.addAttribute("email", byUsername.email);
        model.addAttribute("phoneNumber", byUsername.phoneNumber);
        model.addAttribute("description", byUsername.description);

        Set<Role> roles = byUsername.roles;
        model.addAttribute("role", roles);
        return USER_INFO;
    }

    @GetMapping("/users/post-check-current-password")
    @PreAuthorize("hasAuthority('ONE:USER:READ')")
    @ResponseBody
    public Boolean postCheckCurrentPassword(@RequestParam Map<String, String> params) {
        String userName = params.containsKey("userName") ? params.get("userName") : "";
        String currentPassword = params.containsKey("currentPassword") ? params.get("currentPassword") : "";

        if (!userName.isEmpty() && !currentPassword.isEmpty()) {
            UserSearchForm userSearchForm = new UserSearchForm();
            userSearchForm.currentPassword = currentPassword;
            userSearchForm.userName = userName;
            try {
                userClient.checkCurrentPassword(userSearchForm);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @GetMapping("/users/change-password")
    @PreAuthorize("hasAuthority('ONE:USER:UPDATE')")
    @ResponseBody
    public boolean updateChangePassword(@RequestParam Map<String, String> request) {
        String userName = request.containsKey("userName") ? request.get("userName") : "";
        String newPassword = request.containsKey("newPassword") ? request.get("newPassword") : "";
        String currentPassword = request.containsKey("currentPassword") ? request.get("currentPassword") : "";

        if (!currentPassword.isEmpty() && !newPassword.isEmpty()) {
            UserSearchForm userSearchForm = new UserSearchForm();
            userSearchForm.currentPassword = currentPassword;
            userSearchForm.userName = userName;
            userSearchForm.newPassword = newPassword;
            try {
                User user = userClient.changePassword(userSearchForm);
                logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "change password username " + userName, "", "");
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @GetMapping("/users/get-existed-email")
    @ResponseBody
    public Boolean getExistedEmail(@RequestParam(value = "email", defaultValue = "") String email) {
        Boolean check = false;
        UserSearchForm userSearchForm = new UserSearchForm();
        userSearchForm.email = email;
        User[] users = userClient.search(userSearchForm);
        for (User user : users) {
            if (user.email.toLowerCase().equals(email.toLowerCase())) {
                check = true;
                break;
            }
        }

        return check;
    }

    @PostMapping("/users/forgot-password-with-email")
    @ResponseBody
    public Boolean updateForgotPasswordWithEmail(@RequestParam(value = "email", defaultValue = "") String email,
                                                 @RequestParam(value = "redirectUrl", defaultValue = "") String redirectUrl) {
        UserSearchForm userSearchForm = new UserSearchForm();
        userSearchForm.email = email;
        userSearchForm.redirectUrl = redirectUrl;
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "forgot password with email " + email, "", "");
        return userClient.forgotPasswordWithEmail(userSearchForm);
    }

    @GetMapping("/changeForgotPassword")
    public String changeForgotPassword(Model model,
                                       @RequestParam(value = "userId", defaultValue = "0") Long userId,
                                       @RequestParam(value = "token", defaultValue = "") String token) {
        UserSearchForm userSearchForm = new UserSearchForm();
        userSearchForm.userId = userId;
        User[] user = userClient.search(userSearchForm);
        if (user != null) {
            model.addAttribute("user", user[0]);
        }

        return CHANGE_FORGOT_PASSWORD_PAGE;
    }

    @GetMapping("/changeForgotPasswordConfirm")
    public String changeForgotPasswordConfirm(Model model,
                                              @RequestParam(value = "userId", defaultValue = "0") Long userId,
                                              @RequestParam(value = "token", defaultValue = "") String token) {
        UserSearchForm userSearchForm = new UserSearchForm();
        userSearchForm.userId = userId;
        User[] user = userClient.search(userSearchForm);
        if (user != null) {
            model.addAttribute("user", user[0]);
        }

        return CHANGE_FORGOT_PASSWORD_CONFIRM_PAGE;
    }

    @PostMapping("/change-password-with-token")
    @ResponseBody
    public Boolean changePassword(@RequestParam(value = "newPassword", defaultValue = "") String newPassword,
                                  @RequestParam(value = "userId", defaultValue = "0") Long userId,
                                  @RequestParam(value = "token", defaultValue = "") String token) {
        Boolean response = true;
        try {
            UserSearchForm userSearchForm = new UserSearchForm();
            userSearchForm.userId = userId;
            userSearchForm.token = token;
            userSearchForm.newPassword = newPassword;
            userClient.changePasswordWithToken(userSearchForm);

        } catch (Exception e) {
            e.printStackTrace();
            response = false;
        }

        return response;
    }

}
