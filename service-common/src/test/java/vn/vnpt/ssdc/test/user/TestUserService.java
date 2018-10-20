package vn.vnpt.ssdc.test.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import com.global.servicecommon.model.EmailTemplate;
import com.global.servicecommon.services.EmailTemplateService;
import com.global.servicecommon.services.MailService;
import vn.vnpt.ssdc.test.UmpTestConfiguration;
import com.global.servicecommon.user.model.Role;
import com.global.servicecommon.user.model.User;
import com.global.servicecommon.user.services.RoleService;
import com.global.servicecommon.user.services.UserService;
import com.global.servicecommon.jdbc.factories.RepositoryFactory;

import javax.activation.DataSource;

import java.util.HashSet;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UmpTestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class TestUserService {

    @Autowired
    RepositoryFactory repositoryFactory;

    private MailService mailService = mock(MailService.class);

    @Test
    public void getPage() {

        // Declare service
        UserService userService = new UserService(repositoryFactory);
        userService.emailTemplateService = new EmailTemplateService(repositoryFactory);
        // TODO emailService

        // Add template data
        EmailTemplate emailTemplate001 = new EmailTemplate();
        emailTemplate001.id = "user.forgotpassword";
        emailTemplate001.value = "<p>Please click <a href=\"http://ump-dev/changeForgotPassword?userId=%d&token=%s\">here</a> to recover your password with code:  <strong>%s</strong></p>";
        userService.emailTemplateService.create(emailTemplate001);

        EmailTemplate emailTemplate002 = new EmailTemplate();
        emailTemplate002.id = "user.randomPassword";
        emailTemplate002.value = "<p>Login to UMP Application with<br>Username: %s <br>Password: %s</p>";
        userService.emailTemplateService.create(emailTemplate002);

        EmailTemplate emailTemplate003 = new EmailTemplate();
        emailTemplate003.id = "user.resetPassword";
        emailTemplate003.value = "<p>Login to UMP Application with new password<br>Username: %s <br>Password: %s</p>";
        userService.emailTemplateService.create(emailTemplate003);

        User user001 = new User();
        user001.userName = "username001";
        user001.email = "email001@gmail.com";
        doNothing().doThrow(new IllegalArgumentException()).when(mailService).sendMail(anyString(), anyString(), anyString(), anyString(), any(DataSource.class));
        userService.mailService = mailService;
        userService.create(user001);

        // Do business
        int page = 0;
        int limit = 1;
        String whereStr = "1=1";
        Page<User> userPage = userService.getPage(page, limit, whereStr);
        User userCheck = userPage.getContent().get(0);

        Assert.assertEquals(user001.userName, userCheck.userName);
    }

    @Test
    public void beforeCreate() {
        UserService userService = new UserService(repositoryFactory);
        userService.emailTemplateService = new EmailTemplateService(repositoryFactory);
        userService.roleService = new RoleService(repositoryFactory);

        // Add template date
        EmailTemplate emailTemplate002 = new EmailTemplate();
        emailTemplate002.id = "user.randomPassword";
        emailTemplate002.value = "<p>Login to UMP Application with<br>Username: %s <br>Password: %s</p>";
        userService.emailTemplateService.create(emailTemplate002);

        Role role001 = new Role();
        role001.name = "roleName001";
        role001.operationIds = new HashSet<String>();
        role001.operationIds.add("ONE:USER:DELETE");
        role001 = userService.roleService.create(role001);

        // Do business
        User user001 = new User();
        user001.userName = "username001";
        user001.roleIds.add(String.valueOf(role001.id));
        doNothing().doThrow(new IllegalArgumentException()).when(mailService).sendMail(anyString(), anyString(), anyString(), anyString(), any(DataSource.class));
        userService.mailService = mailService;
        userService.beforeCreate(user001);
        Assert.assertNotNull(user001.password);
        Assert.assertEquals(user001.operationIds, role001.operationIds);
    }

    @Test
    public void resetPassword() {

        // Declare service
        UserService userService = new UserService(repositoryFactory);
        userService.emailTemplateService = new EmailTemplateService(repositoryFactory);

        // Add template data
        EmailTemplate emailTemplate001 = new EmailTemplate();
        emailTemplate001.id = "user.forgotpassword";
        emailTemplate001.value = "<p>Please click <a href=\"http://ump-dev/changeForgotPassword?userId=%d&token=%s\">here</a> to recover your password with code:  <strong>%s</strong></p>";
        userService.emailTemplateService.create(emailTemplate001);

        EmailTemplate emailTemplate002 = new EmailTemplate();
        emailTemplate002.id = "user.randomPassword";
        emailTemplate002.value = "<p>Login to UMP Application with<br>Username: %s <br>Password: %s</p>";
        userService.emailTemplateService.create(emailTemplate002);

        EmailTemplate emailTemplate003 = new EmailTemplate();
        emailTemplate003.id = "user.resetPassword";
        emailTemplate003.value = "<p>Login to UMP Application with new password<br>Username: %s <br>Password: %s</p>";
        userService.emailTemplateService.create(emailTemplate003);

        User user001 = new User();
        user001.userName = "username001";
        user001.email = "email001@gmail.com";
        doNothing().doThrow(new IllegalArgumentException()).when(mailService).sendMail(anyString(), anyString(), anyString(), anyString(), any(DataSource.class));
        userService.mailService = mailService;
        userService.create(user001);

        // Do business
        Long id = user001.id;
        doNothing().doThrow(new IllegalArgumentException()).when(mailService).sendMail(anyString(), anyString(), anyString(), anyString(), any(DataSource.class));
        userService.mailService = mailService;
        userService.resetPassword(id);
        User userCheck = userService.get(id);

        Assert.assertNotEquals(userCheck.password, user001.password);
    }

    @Test
    public void checkPassword() {

        // Declare service
        UserService userService = new UserService(repositoryFactory);
        userService.emailTemplateService = new EmailTemplateService(repositoryFactory);

        // Add template data
        EmailTemplate emailTemplate001 = new EmailTemplate();
        emailTemplate001.id = "user.forgotpassword";
        emailTemplate001.value = "<p>Please click <a href=\"http://ump-dev/changeForgotPassword?userId=%d&token=%s\">here</a> to recover your password with code:  <strong>%s</strong></p>";
        userService.emailTemplateService.create(emailTemplate001);

        EmailTemplate emailTemplate002 = new EmailTemplate();
        emailTemplate002.id = "user.randomPassword";
        emailTemplate002.value = "<p>Login to UMP Application with<br>Username: %s <br>Password: %s</p>";
        userService.emailTemplateService.create(emailTemplate002);

        EmailTemplate emailTemplate003 = new EmailTemplate();
        emailTemplate003.id = "user.resetPassword";
        emailTemplate003.value = "<p>Login to UMP Application with new password<br>Username: %s <br>Password: %s</p>";
        doNothing().doThrow(new IllegalArgumentException()).when(mailService).sendMail(anyString(), anyString(), anyString(), anyString(), any(DataSource.class));
        userService.mailService = mailService;
        userService.emailTemplateService.create(emailTemplate003);

        User user001 = new User();
        user001.userName = "username001";
        user001.email = "email001@gmail.com";
        userService.create(user001);
        user001.setEncryptedPassword("password001");
        user001 = userService.update(user001.id, user001);

        // Do business
        String username = user001.userName;
        String currentPassword = "password001";
        Boolean result = userService.checkPassword(username, currentPassword);
        Assert.assertTrue(result);
    }

    @Test
    public void changePassword() {

        // Declare service
        UserService userService = new UserService(repositoryFactory);
        userService.emailTemplateService = new EmailTemplateService(repositoryFactory);

        // Add template data
        EmailTemplate emailTemplate001 = new EmailTemplate();
        emailTemplate001.id = "user.forgotpassword";
        emailTemplate001.value = "<p>Please click <a href=\"http://ump-dev/changeForgotPassword?userId=%d&token=%s\">here</a> to recover your password with code:  <strong>%s</strong></p>";
        userService.emailTemplateService.create(emailTemplate001);

        EmailTemplate emailTemplate002 = new EmailTemplate();
        emailTemplate002.id = "user.randomPassword";
        emailTemplate002.value = "<p>Login to UMP Application with<br>Username: %s <br>Password: %s</p>";
        userService.emailTemplateService.create(emailTemplate002);

        EmailTemplate emailTemplate003 = new EmailTemplate();
        emailTemplate003.id = "user.resetPassword";
        emailTemplate003.value = "<p>Login to UMP Application with new password<br>Username: %s <br>Password: %s</p>";
        userService.emailTemplateService.create(emailTemplate003);

        User user001 = new User();
        user001.userName = "username001";
        user001.email = "email001@gmail.com";
        doNothing().doThrow(new IllegalArgumentException()).when(mailService).sendMail(anyString(), anyString(), anyString(), anyString(), any(DataSource.class));
        userService.mailService = mailService;
        userService.create(user001);
        user001.setEncryptedPassword("password001");
        user001 = userService.update(user001.id, user001);

        // Do business
        String username = user001.userName;
        String currentPassword = "password001";
        String newPassword = "password002";
        User userResult = userService.changePassword(username, currentPassword, newPassword);
        User userNew = new User();
        userNew.setEncryptedPassword(newPassword);

        Assert.assertNotEquals(userResult.password, userNew.password);
    }

    @Test
    public void checkToken() {
        // Declare service
        UserService userService = new UserService(repositoryFactory);
        userService.emailTemplateService = new EmailTemplateService(repositoryFactory);

        // Add template data
        EmailTemplate emailTemplate001 = new EmailTemplate();
        emailTemplate001.id = "user.forgotpassword";
        emailTemplate001.value = "<p>Please click <a href=\"http://ump-dev/changeForgotPassword?userId=%d&token=%s\">here</a> to recover your password with code:  <strong>%s</strong></p>";
        userService.emailTemplateService.create(emailTemplate001);

        EmailTemplate emailTemplate002 = new EmailTemplate();
        emailTemplate002.id = "user.randomPassword";
        emailTemplate002.value = "<p>Login to UMP Application with<br>Username: %s <br>Password: %s</p>";
        userService.emailTemplateService.create(emailTemplate002);

        EmailTemplate emailTemplate003 = new EmailTemplate();
        emailTemplate003.id = "user.resetPassword";
        emailTemplate003.value = "<p>Login to UMP Application with new password<br>Username: %s <br>Password: %s</p>";
        userService.emailTemplateService.create(emailTemplate003);

        User user001 = new User();
        user001.userName = "username001";
        user001.forgotPwdToken = "token001";
        doNothing().doThrow(new IllegalArgumentException()).when(mailService).sendMail(anyString(), anyString(), anyString(), anyString(), any(DataSource.class));
        userService.mailService = mailService;
        userService.create(user001);

        // Do business
        Long id = user001.id;
        String token = "token001";
        Boolean result = userService.checkToken(id, token);
        Assert.assertTrue(result);

    }

    @Test
    public void changePasswordWithToken() {
        // Declare service
        UserService userService = new UserService(repositoryFactory);
        userService.emailTemplateService = new EmailTemplateService(repositoryFactory);

        // Add template data
        EmailTemplate emailTemplate001 = new EmailTemplate();
        emailTemplate001.id = "user.forgotpassword";
        emailTemplate001.value = "<p>Please click <a href=\"http://ump-dev/changeForgotPassword?userId=%d&token=%s\">here</a> to recover your password with code:  <strong>%s</strong></p>";
        userService.emailTemplateService.create(emailTemplate001);

        EmailTemplate emailTemplate002 = new EmailTemplate();
        emailTemplate002.id = "user.randomPassword";
        emailTemplate002.value = "<p>Login to UMP Application with<br>Username: %s <br>Password: %s</p>";
        userService.emailTemplateService.create(emailTemplate002);

        EmailTemplate emailTemplate003 = new EmailTemplate();
        emailTemplate003.id = "user.resetPassword";
        emailTemplate003.value = "<p>Login to UMP Application with new password<br>Username: %s <br>Password: %s</p>";
        userService.emailTemplateService.create(emailTemplate003);

        User user001 = new User();
        user001.userName = "username001";
        user001.forgotPwdToken = "token001";
        doNothing().doThrow(new IllegalArgumentException()).when(mailService).sendMail(anyString(), anyString(), anyString(), anyString(), any(DataSource.class));
        userService.mailService = mailService;
        userService.create(user001);

        // Do business
        Long id = user001.id;
        String token = "token001";
        String newPassword = "password001edited";
        Boolean result = userService.changePasswordWithToken(id, token, newPassword);
        Assert.assertTrue(result);

    }

}
