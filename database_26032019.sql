/*
SQLyog Ultimate v11.3 (64 bit)
MySQL - 10.0.36-MariaDB : Database - global
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `bill` */

DROP TABLE IF EXISTS `bill`;

CREATE TABLE `bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `bill_type` int(1) DEFAULT NULL,
  `weight` bigint(20) DEFAULT NULL,
  `cost` bigint(20) DEFAULT NULL,
  `total_cost` bigint(20) DEFAULT NULL,
  `content` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `paid` int(1) DEFAULT NULL COMMENT '0: chưa trả, 1: đã trả',
  `created` bigint(20) DEFAULT NULL,
  `updated` bigint(20) DEFAULT NULL,
  `is_cod` int(1) DEFAULT NULL COMMENT '0: 0 cod, 1: có cod',
  `cod_value` bigint(20) DEFAULT NULL COMMENT 'Tiền cod',
  `bill_state` int(1) DEFAULT NULL,
  `who_pay` int(1) DEFAULT NULL COMMENT '0: người gửi trả, 1: người nhận trả',
  `user_create` bigint(20) NOT NULL,
  `branch_create` bigint(20) NOT NULL,
  `current_branch` bigint(20) NOT NULL,
  `partner_id` bigint(20) DEFAULT NULL,
  `employee_send` bigint(20) DEFAULT NULL COMMENT 'nhân viên nhận hàng',
  `employee_receive` bigint(20) DEFAULT NULL COMMENT 'nhân viên giao hàng',
  `pay_type` int(1) DEFAULT '1' COMMENT 'Hình thức thanh toán (0: Nợ, 1: có)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `bill_no_idx` (`bill_no`) USING BTREE,
  KEY `bill_created_idx` (`created`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `bill` */

insert  into `bill`(`id`,`bill_no`,`bill_type`,`weight`,`cost`,`total_cost`,`content`,`paid`,`created`,`updated`,`is_cod`,`cod_value`,`bill_state`,`who_pay`,`user_create`,`branch_create`,`current_branch`,`partner_id`,`employee_send`,`employee_receive`,`pay_type`) values (3,'3651794',1,9,8,5,NULL,0,1542423934802,1542423934802,1,2,0,0,-3,5,0,0,1,2,0),(4,'3651795',1,2,3,434324,'ghi chu',0,NULL,NULL,1,NULL,1,0,-3,5,0,NULL,1,1,1),(5,'3651796',1,99,5,6,'ghi chus',0,NULL,NULL,1,45,2,1,-3,5,0,NULL,1,2,1);

/*Table structure for table `bill_log` */

DROP TABLE IF EXISTS `bill_log`;

CREATE TABLE `bill_log` (
  `id` bigint(20) NOT NULL,
  `bill_id` bigint(20) NOT NULL,
  `bill_code` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `content` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_BILL_LOG_USER` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `bill_log` */

/*Table structure for table `bill_receive` */

DROP TABLE IF EXISTS `bill_receive`;

CREATE TABLE `bill_receive` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NOT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `receive_name` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `receive_address` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `receive_mobile` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `receive_time` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `receive_date` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `receive_by` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'nguoi ky nhan',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_BILL_RECEIVE_IDX` (`bill_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `bill_receive` */

insert  into `bill_receive`(`id`,`bill_id`,`customer_id`,`receive_name`,`receive_address`,`receive_mobile`,`receive_time`,`receive_date`,`receive_by`) values (1,3,11,'Nguyễn Trọng Hưng','Minh Khai - Từ Liêm - Hà Nội','0989143302','09:10','2018-12-02','1'),(2,4,11,'Nguyễn Trọng Hưng','Minh Khai - Từ Liêm - Hà Nội','0989143302','14:33','2019-02-21',''),(3,5,11,'Nguyễn Trọng Hưng','Minh Khai - Từ Liêm - Hà Nội','0989143302','9:10','2018-12-02','');

/*Table structure for table `bill_send` */

DROP TABLE IF EXISTS `bill_send`;

CREATE TABLE `bill_send` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NOT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `send_name` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `send_address` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `send_mobile` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `send_time` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `send_date` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `send_by` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'nguoi ky gui',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_BILL_SEND_IDX` (`bill_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `bill_send` */

insert  into `bill_send`(`id`,`bill_id`,`customer_id`,`send_name`,`send_address`,`send_mobile`,`send_time`,`send_date`,`send_by`) values (1,3,11,'Lê Văn Thuyết','Yên Trung - Ý Yên - Nam Định','0972013265','09:10','2018-12-02','1'),(2,4,11,'Lê Văn Thuyết','Yên Trung - Ý Yên - Nam Định','0972013265','15:0','2019-02-01',''),(3,5,11,'Lê Văn Thuyết','Yên Trung - Ý Yên - Nam Định','0972013265','9:10','2018-12-02','');

/*Table structure for table `bill_stock` */

DROP TABLE IF EXISTS `bill_stock`;

CREATE TABLE `bill_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `state` int(1) NOT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `created` bigint(20) NOT NULL,
  `used` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `BILL_NO_STOCK_IDX` (`bill_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `bill_stock` */

/*Table structure for table `branch` */

DROP TABLE IF EXISTS `branch`;

CREATE TABLE `branch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_name` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `branch_address` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `branch_hotline` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `branch` */

insert  into `branch`(`id`,`branch_name`,`branch_address`,`branch_hotline`) values (5,'Global Post','Trường Chinh, Hà Nội','0989143302'),(6,'Chi nhánh Hải Phòng','Lê Chân, Hải Phòng','0914549933'),(7,'Chi nhánh test','124 Hoàng Quốc Việt','0977332341');

/*Table structure for table `customer` */

DROP TABLE IF EXISTS `customer`;

CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tax_code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tax_address` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mobile` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `note` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  `updated` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_code_idx` (`code`) USING BTREE,
  KEY `tax_code_idx` (`tax_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `customer` */

insert  into `customer`(`id`,`code`,`name`,`tax_code`,`address`,`tax_address`,`mobile`,`email`,`note`,`created`,`updated`,`user_id`) values (11,'YENTRUNG','Le Thuyet','YENTRUNG','Yên Trung - Ý Yên - Nam Định','Yen Trung - Y Yen -','0972013265','lethuyet.10.11.1990@gmail.com','',NULL,NULL,0),(14,'HUNGNT01','Nguyễn Trọng Hưng','HUNGNT01','Bắc Từ Liêm - Hà Nội','Bắc Từ Liêm - Hà Nội','0989143302','hungnt291086@gmail.com','',NULL,NULL,NULL),(15,'KINHBAC','CTy CP Kinh bắc',NULL,'106 hoàng quóc việt',NULL,'0915241812',NULL,NULL,NULL,NULL,NULL),(16,'LVA','Lê Văn A',NULL,'Nam định',NULL,'0972013265',NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `email_template` */

DROP TABLE IF EXISTS `email_template`;

CREATE TABLE `email_template` (
  `id` varchar(128) NOT NULL,
  `value` text,
  `description` varchar(1024) DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  `updated` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `email_template` */

insert  into `email_template`(`id`,`value`,`description`,`created`,`updated`) values ('user.forgotpassword','<p>Please click <a href=\"http://master-ump-server:8081/changeForgotPassword?userId=%d&token=%s&redirect=%s\">here</a> to recover your password with code:  <strong>%s</strong></p>','Forgot Password',NULL,NULL),('user.randomPassword','<p>Login to UMP Application with<br>Username: %s <br>Password: %s</p>','Reset Password',NULL,NULL),('user.resetPassword','<p>Login to UMP Application with new password<br>Username: %s <br>Password: %s</p>','Reset Password',NULL,NULL);

/*Table structure for table `employee` */

DROP TABLE IF EXISTS `employee`;

CREATE TABLE `employee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `mobile` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `address` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `branch_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `employee` */

insert  into `employee`(`id`,`full_name`,`mobile`,`address`,`branch_id`) values (1,'Trọng Hưng','0972013265','Tesst address',5),(2,'Lê Thuyết','972013265','Yen Trung',5),(3,'Global','Global','Global',5);

/*Table structure for table `logging_users` */

DROP TABLE IF EXISTS `logging_users`;

CREATE TABLE `logging_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `session` text,
  `username` text,
  `time` text,
  `actions` text,
  `created` bigint(20) DEFAULT NULL,
  `updated` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `logging_users` */

/*Table structure for table `operation` */

DROP TABLE IF EXISTS `operation`;

CREATE TABLE `operation` (
  `id` varchar(128) NOT NULL,
  `name` varchar(256) DEFAULT NULL,
  `group_name` varchar(256) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  `updated` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `operation` */

insert  into `operation`(`id`,`name`,`group_name`,`description`,`created`,`updated`) values ('GLOBAL:BILL:CREATE','Create','Bill',NULL,1539171457098,NULL),('GLOBAL:BILL:DELETE','Delete','Bill',NULL,1539171457098,NULL),('GLOBAL:BILL:READ','Read','Bill',NULL,1539171457098,NULL),('GLOBAL:BILL:UPDATE','Update','Bill',NULL,1539171457098,NULL),('GLOBAL:BRANCH:CREATE','Create','Branch',NULL,1539171457098,NULL),('GLOBAL:BRANCH:DELETE','Delete','Branch',NULL,1539171457098,NULL),('GLOBAL:BRANCH:READ','Read','Branch',NULL,1539171457098,NULL),('GLOBAL:BRANCH:UPDATE','Update','Branch',NULL,1539171457098,NULL),('GLOBAL:CUSTOMER:CREATE','Create','Customer',NULL,1539171457098,NULL),('GLOBAL:CUSTOMER:DELETE','Delete','Customer',NULL,1539171457098,NULL),('GLOBAL:CUSTOMER:READ','Read','Customer',NULL,1539171457098,NULL),('GLOBAL:CUSTOMER:UPDATE','Update','Customer',NULL,1539171457098,NULL),('GLOBAL:EMPLOYEE:CREATE','Create','EMPLOYEE',NULL,NULL,NULL),('GLOBAL:EMPLOYEE:DELETE','Delete','EMPLOYEE',NULL,NULL,NULL),('GLOBAL:EMPLOYEE:READ','Read','EMPLOYEE',NULL,NULL,NULL),('GLOBAL:EMPLOYEE:SAVE','Save','EMPLOYEE',NULL,NULL,NULL),('GLOBAL:EMPLOYEE:UPDATE','Update','EMPLOYEE',NULL,NULL,NULL),('GLOBAL:PART:CREATE','Create','Part',NULL,1541596938610,NULL),('GLOBAL:PART:DELETE','Delete','Part',NULL,1541596938854,NULL),('GLOBAL:PART:READ','Read','Part',NULL,1541596938486,NULL),('GLOBAL:PART:UPDATE','Update','Part',NULL,1541596938730,NULL),('GLOBAL:PARTNER:CREATE','Create','Partner',NULL,1539171457098,NULL),('GLOBAL:PARTNER:DELETE','Delete','Partner',NULL,1539171457098,NULL),('GLOBAL:PARTNER:READ','Read','Partner',NULL,1539171457098,NULL),('GLOBAL:PARTNER:UPDATE','Update','Partner',NULL,1539171457098,NULL),('ONE:PERMISSION:CREATE','Create','Permissions',NULL,1539171457147,NULL),('ONE:PERMISSION:DELETE','Delete','Permissions',NULL,1539171457156,NULL),('ONE:PERMISSION:READ','Read','Permissions',NULL,1539171457142,NULL),('ONE:PERMISSION:UPDATE','Update','Permissions',NULL,1539171457151,NULL),('ONE:ROLE:CREATE','Create','Roles',NULL,1539171457125,NULL),('ONE:ROLE:DELETE','Delete','Roles',NULL,1539171457136,NULL),('ONE:ROLE:READ','Read','Roles',NULL,1539171457119,NULL),('ONE:ROLE:UPDATE','Update','Roles',NULL,1539171457131,NULL),('ONE:USER:CREATE','Create','Users',NULL,1539171457092,NULL),('ONE:USER:DELETE','Delete','Users',NULL,1539171457104,NULL),('ONE:USER:FORGOT_PASSWORD','Forgot password','Users',NULL,1539171457114,NULL),('ONE:USER:READ','Read','Users',NULL,1539171457066,NULL),('ONE:USER:RESET_PASSWORD','Password','Users',NULL,1539171457109,NULL),('ONE:USER:UPDATE','Update','Users',NULL,1539171457098,NULL);

/*Table structure for table `partner` */

DROP TABLE IF EXISTS `partner`;

CREATE TABLE `partner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `part_name` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `part_hotline` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `part_address` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `partner` */

insert  into `partner`(`id`,`part_name`,`part_hotline`,`part_address`) values (1,'Viettel','091231564','Viettel'),(2,'KerryExpress','0989143302','Hà Nội');

/*Table structure for table `permission` */

DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
  `name` varchar(256) DEFAULT NULL,
  `group_name` varchar(256) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  `updated` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operation_ids` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

/*Data for the table `permission` */

insert  into `permission`(`name`,`group_name`,`description`,`created`,`updated`,`id`,`operation_ids`) values ('SuperAdmin',NULL,'SuperAdmin',NULL,1542267459991,30,'[\"ONE:PERMISSION:UPDATE\",\"ONE:USER:UPDATE\",\"GLOBAL:BRANCH:UPDATE\",\"GLOBAL:BILL:DELETE\",\"ONE:USER:FORGOT_PASSWORD\",\"GLOBAL:PART:DELETE\",\"ONE:PERMISSION:DELETE\",\"ONE:USER:RESET_PASSWORD\",\"GLOBAL:BRANCH:CREATE\",\"ONE:ROLE:READ\",\"GLOBAL:BRANCH:READ\",\"ONE:USER:DELETE\",\"GLOBAL:PARTNER:READ\",\"GLOBAL:PART:UPDATE\",\"GLOBAL:PARTNER:UPDATE\",\"ONE:ROLE:DELETE\",\"ONE:USER:READ\",\"GLOBAL:CUSTOMER:CREATE\",\"GLOBAL:PART:READ\",\"GLOBAL:BILL:CREATE\",\"GLOBAL:BRANCH:DELETE\",\"ONE:PERMISSION:CREATE\",\"ONE:ROLE:UPDATE\",\"ONE:USER:CREATE\",\"GLOBAL:PARTNER:DELETE\",\"GLOBAL:BILL:READ\",\"GLOBAL:PARTNER:CREATE\",\"ONE:ROLE:CREATE\",\"GLOBAL:CUSTOMER:DELETE\",\"ONE:PERMISSION:READ\",\"GLOBAL:PART:CREATE\",\"GLOBAL:BILL:UPDATE\",\"GLOBAL:CUSTOMER:READ\",\"GLOBAL:CUSTOMER:UPDATE\",\"GLOBAL:EMPLOYEE:CREATE\",\"GLOBAL:EMPLOYEE:DELETE\",\"GLOBAL:EMPLOYEE:READ\",\"GLOBAL:EMPLOYEE:SAVE\",\"GLOBAL:EMPLOYEE:UPDATE\"]'),('NhanVien',NULL,'QuyenChoNhanVien',1552143648925,NULL,45,'[\"GLOBAL:BILL:READ\",\"GLOBAL:BILL:DELETE\",\"ONE:USER:READ\",\"GLOBAL:PART:READ\",\"GLOBAL:EMPLOYEE:READ\",\"GLOBAL:BILL:CREATE\",\"GLOBAL:BILL:UPDATE\",\"GLOBAL:BRANCH:READ\",\"GLOBAL:PARTNER:READ\",\"GLOBAL:CUSTOMER:READ\"]');

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL,
  `permissions_ids` varchar(256) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  `updated` bigint(20) DEFAULT NULL,
  `operation_ids` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

/*Data for the table `role` */

insert  into `role`(`id`,`name`,`permissions_ids`,`description`,`created`,`updated`,`operation_ids`) values (31,'SuperAdmin','[30]','SuperAdmin',NULL,1542267419485,'[\"ONE:PERMISSION:UPDATE\",\"ONE:USER:UPDATE\",\"GLOBAL:BRANCH:UPDATE\",\"GLOBAL:BILL:DELETE\",\"ONE:USER:FORGOT_PASSWORD\",\"GLOBAL:PART:DELETE\",\"ONE:PERMISSION:DELETE\",\"ONE:USER:RESET_PASSWORD\",\"GLOBAL:BRANCH:CREATE\",\"ONE:ROLE:READ\",\"GLOBAL:BRANCH:READ\",\"ONE:USER:DELETE\",\"GLOBAL:PARTNER:READ\",\"GLOBAL:PART:UPDATE\",\"GLOBAL:PARTNER:UPDATE\",\"ONE:ROLE:DELETE\",\"ONE:USER:READ\",\"GLOBAL:CUSTOMER:CREATE\",\"GLOBAL:PART:READ\",\"GLOBAL:BILL:CREATE\",\"GLOBAL:BRANCH:DELETE\",\"ONE:PERMISSION:CREATE\",\"ONE:ROLE:UPDATE\",\"ONE:USER:CREATE\",\"GLOBAL:PARTNER:DELETE\",\"GLOBAL:BILL:READ\",\"GLOBAL:PARTNER:CREATE\",\"ONE:ROLE:CREATE\",\"GLOBAL:CUSTOMER:DELETE\",\"ONE:PERMISSION:READ\",\"GLOBAL:PART:CREATE\",\"GLOBAL:BILL:UPDATE\",\"GLOBAL:CUSTOMER:READ\",\"GLOBAL:CUSTOMER:UPDATE\",\"GLOBAL:EMPLOYEE:CREATE\",\"GLOBAL:EMPLOYEE:DELETE\",\"GLOBAL:EMPLOYEE:READ\",\"GLOBAL:EMPLOYEE:SAVE\",\"GLOBAL:EMPLOYEE:UPDATE\"]'),(37,'NhanVien','[45]','VaiTroNhanVien',NULL,1552143700711,'[\"GLOBAL:BILL:READ\",\"GLOBAL:BILL:DELETE\",\"ONE:USER:READ\",\"GLOBAL:PART:READ\",\"GLOBAL:EMPLOYEE:READ\",\"GLOBAL:BILL:CREATE\",\"GLOBAL:BILL:UPDATE\",\"GLOBAL:BRANCH:READ\",\"GLOBAL:PARTNER:READ\",\"GLOBAL:CUSTOMER:READ\"]');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(32) DEFAULT NULL,
  `full_name` varchar(64) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `password` varchar(256) DEFAULT NULL,
  `role_ids` varchar(256) DEFAULT NULL,
  `role_names` varchar(1024) DEFAULT NULL,
  `avatar_url` varchar(1024) DEFAULT NULL,
  `phone` varchar(16) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `forgot_pwd_token` varchar(256) DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  `updated` bigint(20) DEFAULT NULL,
  `forgot_pwd_token_requested` bigint(20) DEFAULT NULL,
  `operation_ids` text,
  `branch_id` bigint(20) DEFAULT NULL,
  `branch_name` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`user_name`,`full_name`,`email`,`password`,`role_ids`,`role_names`,`avatar_url`,`phone`,`description`,`forgot_pwd_token`,`created`,`updated`,`forgot_pwd_token_requested`,`operation_ids`,`branch_id`,`branch_name`) values (-3,'superadmin','superadmin','admin@globalpost.com','$2a$10$PXHdlcmePX9.cWljmanH8eKcAinMuc.PN7j5ShEU2BRBGUxNzzmdy','[\"31\"]','[\"SuperAdmin\"]',NULL,NULL,NULL,NULL,1539171457563,1542203623462,NULL,'[\"ONE:PERMISSION:UPDATE\",\"ONE:USER:UPDATE\",\"GLOBAL:BRANCH:UPDATE\",\"GLOBAL:BILL:DELETE\",\"ONE:USER:FORGOT_PASSWORD\",\"GLOBAL:PART:DELETE\",\"ONE:PERMISSION:DELETE\",\"ONE:USER:RESET_PASSWORD\",\"GLOBAL:BRANCH:CREATE\",\"ONE:ROLE:READ\",\"GLOBAL:BRANCH:READ\",\"ONE:USER:DELETE\",\"GLOBAL:PARTNER:READ\",\"GLOBAL:PART:UPDATE\",\"GLOBAL:PARTNER:UPDATE\",\"ONE:ROLE:DELETE\",\"ONE:USER:READ\",\"GLOBAL:CUSTOMER:CREATE\",\"GLOBAL:PART:READ\",\"GLOBAL:BILL:CREATE\",\"GLOBAL:BRANCH:DELETE\",\"ONE:PERMISSION:CREATE\",\"ONE:ROLE:UPDATE\",\"ONE:USER:CREATE\",\"GLOBAL:PARTNER:DELETE\",\"GLOBAL:BILL:READ\",\"GLOBAL:PARTNER:CREATE\",\"ONE:ROLE:CREATE\",\"GLOBAL:CUSTOMER:DELETE\",\"ONE:PERMISSION:READ\",\"GLOBAL:PART:CREATE\",\"GLOBAL:BILL:UPDATE\",\"GLOBAL:CUSTOMER:READ\",\"GLOBAL:CUSTOMER:UPDATE\",\"GLOBAL:EMPLOYEE:CREATE\",\"GLOBAL:EMPLOYEE:DELETE\",\"GLOBAL:EMPLOYEE:READ\",\"GLOBAL:EMPLOYEE:SAVE\",\"GLOBAL:EMPLOYEE:UPDATE\"]',5,'Global Post'),(145,'nhanvien','Le Van Thuyet','app.htaviet@gmail.com','$2a$10$5lJXrQr/8s4qkOFHmL4d2ueqbWMwpnJrNabsyfsLWsQFRcBo56vUe','[\"37\"]','[\"NhanVien\"]',NULL,'972013265','',NULL,1552144338097,NULL,NULL,'[\"GLOBAL:BILL:READ\",\"GLOBAL:BILL:DELETE\",\"ONE:USER:READ\",\"GLOBAL:PART:READ\",\"GLOBAL:EMPLOYEE:READ\",\"GLOBAL:BILL:CREATE\",\"GLOBAL:BILL:UPDATE\",\"GLOBAL:BRANCH:READ\",\"GLOBAL:PARTNER:READ\",\"GLOBAL:CUSTOMER:READ\"]',5,'Global Post'),(146,'nhanvien2','Le Van Thuyet','lethuyet.10.11.1990@gmail.com','$2a$10$r4O/1w960WVCFwi50bLlIeK/jv4EYJkmoDPx0Dthvi4TEPYUm.9zO','[\"37\"]','[\"NhanVien\"]',NULL,'972013265',NULL,NULL,NULL,1552147099164,NULL,'[\"GLOBAL:BILL:READ\",\"GLOBAL:BILL:DELETE\",\"ONE:USER:READ\",\"GLOBAL:PART:READ\",\"GLOBAL:EMPLOYEE:READ\",\"GLOBAL:BILL:CREATE\",\"GLOBAL:BILL:UPDATE\",\"GLOBAL:BRANCH:READ\",\"GLOBAL:PARTNER:READ\",\"GLOBAL:CUSTOMER:READ\"]',7,'Chi nhánh test');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
