--liquibase formatted sql
--changeset thanhlx:1.2

--
-- Dumping data for table `email_templates`
--

INSERT INTO `email_templates` (`id`, `value`, `description`, `created`, `updated`) VALUES
('user.forgotpassword', '<p>Please click <a href="http://master-ump-server:8081/changeForgotPassword?userId=%d&token=%s&redirect=%s">here</a> to recover your password with code:  <strong>%s</strong></p>', 'Forgot Password', NULL, NULL),
('user.randomPassword', '<p>Login to UMP Application with<br>Username: %s <br>Password: %s</p>', 'Reset Password', NULL, NULL),
('user.resetPassword', '<p>Login to UMP Application with new password<br>Username: %s <br>Password: %s</p>', 'Reset Password', NULL, NULL);