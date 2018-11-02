/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.servicebase.user.endpoints;

import com.global.jdbc.exceptions.EntityNotFoundException;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import com.global.servicebase.user.model.UserResponse;
import com.global.servicebase.user.model.UserSearchForm;
import com.global.servicebase.globalexception.UserNotFoundException;
import com.global.servicebase.user.model.User;
import com.global.servicebase.user.services.UserService;

/**
 *
 * Created by ThuyetLV
 */
@Component
@Path("/users")
@Produces(APPLICATION_JSON)
@Consumes({APPLICATION_JSON, TEXT_PLAIN})
@Api("Users")
public class UserEndPoint {

    @Autowired
    public UserService userService;

    @POST
    @Path("/search")
    @ApiOperation(value = "Search user by usersearchForm")
    public List<UserResponse> search(@RequestBody UserSearchForm scUserSearchForm) {
        return userService.search(scUserSearchForm);
    }

    @POST
    @Path("/count")
    @ApiOperation(value = "Count user by usersearchForm")
    public long count(@RequestBody UserSearchForm scUserSearchForm) {
        return userService.count(scUserSearchForm);
    }

    @GET
    @Path("/{userName}")
    @ApiOperation(value = "Get user by username")
    public UserResponse getByUsername(@PathParam("userName") String userName) throws EntityNotFoundException {
        try {
            User user = userService.findByUserName(userName);
            if (user != null) {
                return userService.convertFromUserToSCUser(user);
            }
        } catch (EntityNotFoundException e) {
            throw e;
        }
        return null;
    }

    @PUT
    @Path("/{userName}")
    @ApiOperation(value = "update username")
    public UserResponse update(@PathParam("userName") String userName, @RequestBody UserResponse userResponse) {
        User currentUser = userService.findByUserName(userName);

        User user = userService.convertFromSCUserToUser(userResponse);
        user.password = currentUser.password;
        user.forgotPwdToken = currentUser.forgotPwdToken;
        user.forgotPwdTokenRequested = currentUser.forgotPwdTokenRequested;

        return userService.convertFromUserToSCUser(userService.update(currentUser.id, user));
    }

    @DELETE
    @Path("/{userName}")
    @ApiOperation(value = "delete username")
    public void delete(@PathParam("userName") String userName) {
        userService.delete(userService.findByUserName(userName).id);
    }

    @POST
    @ApiOperation(value = "create username")
    public UserResponse create(@RequestBody UserResponse userResponse) {
        return userService.convertFromUserToSCUser(userService.create(userService.convertFromSCUserToUser(userResponse)));
    }

    @POST
    @Path("/forgot-password")
    @ApiOperation(value = "forgot password")
    public void forgotPassword(@RequestBody UserSearchForm scUserSearchForm) throws UserNotFoundException, Exception {
        try {
            if (!Strings.isNullOrEmpty(scUserSearchForm.email)) {
                userService.sendForgotPasswordWithEmail(scUserSearchForm.redirectUrl, scUserSearchForm.email);
            } else if (!Strings.isNullOrEmpty(scUserSearchForm.userName)) {
                userService.sendForgotPassword(scUserSearchForm.redirectUrl, scUserSearchForm.userName);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @POST
    @Path("/check-current-password")
    @ApiOperation(value = "check current password")
    public void checkCurrentPassword(@RequestBody UserSearchForm scUserSearchForm) throws Exception {
        try {
            if (!userService.checkPassword(scUserSearchForm.userName, scUserSearchForm.currentPassword)) {
                throw new Exception("Wrong Pass!");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @POST
    @Path("/change-password")
    @ApiOperation(value = "change password")
    public UserResponse changePass(@RequestBody UserSearchForm scUserSearchForm) throws Exception {
        return userService.convertFromUserToSCUser(userService.changePassword(scUserSearchForm.userName, scUserSearchForm.currentPassword, scUserSearchForm.newPassword));
    }

    @POST
    @Path("/change-password-with-token")
    @ApiOperation(value = "change pass with token")
    public void changePassWithToken(@RequestBody UserSearchForm scUserSearchForm) throws Exception {
        if (!userService.changePasswordWithToken(scUserSearchForm.userId, scUserSearchForm.token, scUserSearchForm.newPassword)) {
            throw new Exception("Change Pass Failed!");
        }
    }

    @POST
    @Path("/thuyetlv")
    @ApiOperation(value = "change pass with token")
    public User thuyetlv() throws Exception {
        return userService.createPassword(-3L);
    }
}
