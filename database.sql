-- MySQL dump 10.16  Distrib 10.1.28-MariaDB, for Win32 (AMD64)
--
-- Host: localhost    Database: global
-- ------------------------------------------------------
-- Server version	10.1.28-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `global`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `global` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `global`;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `bill_type` int(1) DEFAULT NULL,
  `weight` bigint(20) DEFAULT NULL,
  `cost` bigint(20) DEFAULT NULL,
  `total_cost` bigint(20) DEFAULT NULL,
  `content` text COLLATE utf8_unicode_ci,
  `paid` int(1) DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  `updated` bigint(20) DEFAULT NULL,
  `is_cod` int(1) DEFAULT NULL,
  `cod_value` bigint(20) DEFAULT NULL,
  `bill_state` int(1) DEFAULT NULL,
  `who_pay` int(1) DEFAULT NULL,
  `user_create` bigint(20) DEFAULT NULL,
  `branch_create` bigint(10) DEFAULT NULL,
  `current_branch` bigint(10) DEFAULT NULL,
  `partner_id` bigint(10) DEFAULT NULL,
  `employee_send` bigint(10) DEFAULT NULL,
  `employee_receive` bigint(10) DEFAULT NULL,
  `pay_type` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `bill_no` (`bill_no`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
INSERT INTO `bill` VALUES (1,'123',1,2000,1,300000,'ghi chú',0,NULL,NULL,1,324,0,1,-3,0,0,NULL,1,0,1);
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill_log`
--

DROP TABLE IF EXISTS `bill_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NOT NULL,
  `bill_code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `content` text COLLATE utf8_unicode_ci,
  `user_id` bigint(20) DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_log`
--

LOCK TABLES `bill_log` WRITE;
/*!40000 ALTER TABLE `bill_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `bill_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill_receive`
--

DROP TABLE IF EXISTS `bill_receive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill_receive` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NOT NULL,
  `customer_id` bigint(10) DEFAULT NULL,
  `receive_name` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `receive_address` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `receive_mobile` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `receive_time` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `receive_date` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `receive_by` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_receive`
--

LOCK TABLES `bill_receive` WRITE;
/*!40000 ALTER TABLE `bill_receive` DISABLE KEYS */;
INSERT INTO `bill_receive` VALUES (1,1,1,'Le Thuyet','0x6f2fA571E2c20852B347154A295560cD133d1991','972013265','15:55','2019-03-24','nhận');
/*!40000 ALTER TABLE `bill_receive` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill_send`
--

DROP TABLE IF EXISTS `bill_send`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill_send` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NOT NULL,
  `customer_id` bigint(10) DEFAULT NULL,
  `send_name` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `send_address` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `send_mobile` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `send_time` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `send_date` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `send_by` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_send`
--

LOCK TABLES `bill_send` WRITE;
/*!40000 ALTER TABLE `bill_send` DISABLE KEYS */;
INSERT INTO `bill_send` VALUES (1,1,1,'Le Thuyet','0x6f2fA571E2c20852B347154A295560cD133d1991','972013265','15:55','2019-03-24','gửi');
/*!40000 ALTER TABLE `bill_send` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill_stock`
--

DROP TABLE IF EXISTS `bill_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `state` int(1) DEFAULT '0',
  `customer_id` bigint(10) DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  `used` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_stock`
--

LOCK TABLES `bill_stock` WRITE;
/*!40000 ALTER TABLE `bill_stock` DISABLE KEYS */;
/*!40000 ALTER TABLE `bill_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `branch`
--

DROP TABLE IF EXISTS `branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `branch` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `branch_name` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `branch_address` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `branch_hotline` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `branch_name` (`branch_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branch`
--

LOCK TABLES `branch` WRITE;
/*!40000 ALTER TABLE `branch` DISABLE KEYS */;
INSERT INTO `branch` VALUES (1,'tesst','Yen Trung','0977332341');
/*!40000 ALTER TABLE `branch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `tax_code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tax_address` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mobile` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `note` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_id` bigint(10) DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  `updated` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'THUYETLV','Le Thuyet','THUYETLV','0x6f2fA571E2c20852B3','0x6f2fA571E2c20852B347154A295560cD133d1991','972013265','app.htaviet@gmail.com','ghi chú',-3,NULL,NULL);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `mobile` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(1204) COLLATE utf8_unicode_ci DEFAULT NULL,
  `branch_id` bigint(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `full_name` (`full_name`),
  KEY `mobile` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'Le Thuyet','972013265','Yen Trung',1);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation`
--

DROP TABLE IF EXISTS `operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operation` (
  `id` varchar(128) NOT NULL,
  `name` varchar(256) DEFAULT NULL,
  `group_name` varchar(256) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  `updated` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation`
--

LOCK TABLES `operation` WRITE;
/*!40000 ALTER TABLE `operation` DISABLE KEYS */;
INSERT INTO `operation` VALUES ('GLOBAL:BILL:CREATE','Create','Bill',NULL,1539171457098,NULL),('GLOBAL:BILL:DELETE','Delete','Bill',NULL,1539171457098,NULL),('GLOBAL:BILL:EXPORT','Export','Bill',NULL,1539171457098,NULL),('GLOBAL:BILL:IMPORT','Import','Bill',NULL,1539171457098,NULL),('GLOBAL:BILL:READ','Read','Bill',NULL,1539171457098,NULL),('GLOBAL:BILL:UPDATE','Update','Bill',NULL,1539171457098,NULL),('GLOBAL:BRANCH:CREATE','Create','Branch',NULL,1539171457098,NULL),('GLOBAL:BRANCH:READ','Read','Branch',NULL,1539171457098,NULL),('GLOBAL:BRANCH:UPDATE','Update','Branch',NULL,1539171457098,NULL),('GLOBAL:CUSTOMER:CREATE','Create','Customer',NULL,1539171457098,NULL),('GLOBAL:CUSTOMER:DELETE','Delete','Customer',NULL,1539171457098,NULL),('GLOBAL:CUSTOMER:READ','Read','Customer',NULL,1539171457098,NULL),('GLOBAL:CUSTOMER:UPDATE','Update','Customer',NULL,1539171457098,NULL),('GLOBAL:EMPLOYEE:CREATE','Create','Employee',NULL,1539171457098,NULL),('GLOBAL:EMPLOYEE:DELETE','Delete','Employee',NULL,1539171457098,NULL),('GLOBAL:EMPLOYEE:READ','Read','Employee',NULL,1539171457098,NULL),('GLOBAL:EMPLOYEE:SAVE','Save','Employee',NULL,1539171457098,NULL),('GLOBAL:EMPLOYEE:UPDATE','Update','Employee',NULL,1539171457098,NULL),('GLOBAL:PARTNER:CREATE','Create','Partner',NULL,1539171457098,NULL),('GLOBAL:PARTNER:DELETE','Delete','Partner',NULL,1539171457098,NULL),('GLOBAL:PARTNER:READ','Read','Partner',NULL,1539171457098,NULL),('GLOBAL:PARTNER:UPDATE','Update','Partner',NULL,1539171457098,NULL),('ONE:PERMISSION:CREATE','Create','Permissions',NULL,1539171457147,NULL),('ONE:PERMISSION:DELETE','Delete','Permissions',NULL,1539171457156,NULL),('ONE:PERMISSION:READ','Read','Permissions',NULL,1539171457142,NULL),('ONE:PERMISSION:UPDATE','Update','Permissions',NULL,1539171457151,NULL),('ONE:ROLE:CREATE','Create','Roles',NULL,1539171457125,NULL),('ONE:ROLE:DELETE','Delete','Roles',NULL,1539171457136,NULL),('ONE:ROLE:READ','Read','Roles',NULL,1539171457119,NULL),('ONE:ROLE:UPDATE','Update','Roles',NULL,1539171457131,NULL),('ONE:USER:CREATE','Create','Users',NULL,1539171457092,NULL),('ONE:USER:DELETE','Delete','Users',NULL,1539171457104,NULL),('ONE:USER:FORGOT_PASSWORD','Forgot password','Users',NULL,1539171457114,NULL),('ONE:USER:READ','Read','Users',NULL,1539171457066,NULL),('ONE:USER:RESET_PASSWORD','Password','Users',NULL,1539171457109,NULL),('ONE:USER:UPDATE','Update','Users',NULL,1539171457098,NULL);
/*!40000 ALTER TABLE `operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partner`
--

DROP TABLE IF EXISTS `partner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `partner` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `part_name` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `part_hotline` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `part_address` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `part_name` (`part_name`),
  KEY `part_hotline` (`part_hotline`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partner`
--

LOCK TABLES `partner` WRITE;
/*!40000 ALTER TABLE `partner` DISABLE KEYS */;
INSERT INTO `partner` VALUES (1,'Lê Văn Thuyết','0977332341','Yen Trung - Y Yen - Nam Dinh');
/*!40000 ALTER TABLE `partner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `name` varchar(256) DEFAULT NULL,
  `group_name` varchar(256) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  `updated` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operation_ids` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES ('SuperAdmin',NULL,'SuperAdmin',NULL,1539747160525,24,'[\"ONE:PERMISSION:UPDATE\",\"ONE:USER:UPDATE\",\"GLOBAL:BRANCH:UPDATE\",\"GLOBAL:BILL:DELETE\",\"ONE:USER:FORGOT_PASSWORD\",\"ONE:PERMISSION:DELETE\",\"ONE:USER:RESET_PASSWORD\",\"GLOBAL:EMPLOYEE:READ\",\"ONE:ROLE:READ\",\"GLOBAL:BRANCH:CREATE\",\"ONE:USER:DELETE\",\"GLOBAL:BRANCH:READ\",\"GLOBAL:PARTNER:READ\",\"GLOBAL:PARTNER:UPDATE\",\"ONE:ROLE:DELETE\",\"GLOBAL:BILL:EXPORT\",\"ONE:USER:READ\",\"GLOBAL:CUSTOMER:CREATE\",\"GLOBAL:EMPLOYEE:UPDATE\",\"GLOBAL:BILL:CREATE\",\"ONE:PERMISSION:CREATE\",\"ONE:ROLE:UPDATE\",\"ONE:USER:CREATE\",\"GLOBAL:PARTNER:DELETE\",\"GLOBAL:BILL:READ\",\"GLOBAL:PARTNER:CREATE\",\"ONE:ROLE:CREATE\",\"ONE:PERMISSION:READ\",\"GLOBAL:CUSTOMER:DELETE\",\"GLOBAL:EMPLOYEE:DELETE\",\"GLOBAL:EMPLOYEE:SAVE\",\"GLOBAL:BILL:UPDATE\",\"GLOBAL:EMPLOYEE:CREATE\",\"GLOBAL:BILL:IMPORT\",\"GLOBAL:CUSTOMER:READ\",\"GLOBAL:CUSTOMER:UPDATE\"]'),('Develop',NULL,'Develop',NULL,1539327220980,25,'[\"OLT:LABEL:CREATE\",\"OLT:DEVICE:DELETE\",\"OLT:GROUP:UPDATE\",\"OLT:DEVICE:UPDATE\",\"OLT:GROUP:DELETE\",\"OLT:LABEL:UPDATE\",\"OLT:GROUP:READ\",\"OLT:DEVICE:CREATE\",\"OLT:GROUP:CHECK_USE\",\"OLT:DEVICE:READ\",\"OLT:LABEL:CHECK_USE\",\"OLT:LABEL:DELETE\",\"OLT:LABEL:READ\",\"OLT:GROUP:CREATE\"]'),('UserManagent',NULL,'UserManagent',1539748058538,NULL,26,'[\"ONE:PERMISSION:UPDATE\",\"ONE:USER:UPDATE\",\"ONE:ROLE:CREATE\",\"ONE:USER:FORGOT_PASSWORD\",\"ONE:PERMISSION:DELETE\",\"ONE:PERMISSION:READ\",\"ONE:USER:RESET_PASSWORD\",\"ONE:ROLE:READ\",\"ONE:USER:DELETE\",\"ONE:ROLE:DELETE\",\"ONE:USER:READ\",\"ONE:PERMISSION:CREATE\",\"ONE:ROLE:UPDATE\",\"ONE:USER:CREATE\"]'),('SinoManger',NULL,'SinoManger',NULL,1539748608807,27,'[\"OLT:GROUP:READ\",\"OLT:GROUP:CHECK_USE\",\"OLT:GROUP:UPDATE\",\"OLT:GROUP:CREATE\",\"OLT:GROUP:DELETE\"]'),('test23',NULL,'test23',NULL,1539748718692,28,'[\"OLT:LABEL:UPDATE\",\"OLT:LABEL:CREATE\",\"OLT:LABEL:TESST\",\"OLT:DEVICE:CREATE\",\"OLT:DEVICE:DELETE\",\"OLT:DEVICE:READ\",\"OLT:LABEL:CHECK_USE\",\"OLT:DEVICE:UPDATE\",\"OLT:LABEL:DELETE\",\"OLT:LABEL:READ\"]');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL,
  `permissions_ids` varchar(256) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `created` bigint(20) DEFAULT NULL,
  `updated` bigint(20) DEFAULT NULL,
  `operation_ids` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (31,'SuperAdmin','[24]','SuperAdmin',NULL,1539747196127,'[\"ONE:PERMISSION:UPDATE\",\"ONE:USER:UPDATE\",\"GLOBAL:BRANCH:UPDATE\",\"GLOBAL:BILL:DELETE\",\"ONE:USER:FORGOT_PASSWORD\",\"ONE:PERMISSION:DELETE\",\"ONE:USER:RESET_PASSWORD\",\"GLOBAL:EMPLOYEE:READ\",\"ONE:ROLE:READ\",\"GLOBAL:BRANCH:CREATE\",\"ONE:USER:DELETE\",\"GLOBAL:BRANCH:READ\",\"GLOBAL:PARTNER:READ\",\"GLOBAL:PARTNER:UPDATE\",\"ONE:ROLE:DELETE\",\"GLOBAL:BILL:EXPORT\",\"ONE:USER:READ\",\"GLOBAL:CUSTOMER:CREATE\",\"GLOBAL:EMPLOYEE:UPDATE\",\"GLOBAL:BILL:CREATE\",\"ONE:PERMISSION:CREATE\",\"ONE:ROLE:UPDATE\",\"ONE:USER:CREATE\",\"GLOBAL:PARTNER:DELETE\",\"GLOBAL:BILL:READ\",\"GLOBAL:PARTNER:CREATE\",\"ONE:ROLE:CREATE\",\"ONE:PERMISSION:READ\",\"GLOBAL:CUSTOMER:DELETE\",\"GLOBAL:EMPLOYEE:DELETE\",\"GLOBAL:EMPLOYEE:SAVE\",\"GLOBAL:BILL:UPDATE\",\"GLOBAL:EMPLOYEE:CREATE\",\"GLOBAL:BILL:IMPORT\",\"GLOBAL:CUSTOMER:READ\",\"GLOBAL:CUSTOMER:UPDATE\"]'),(32,'Develop','[25]','Develop',NULL,1539327221027,'[\"OLT:LABEL:CREATE\",\"OLT:DEVICE:DELETE\",\"OLT:GROUP:UPDATE\",\"OLT:DEVICE:UPDATE\",\"OLT:GROUP:DELETE\",\"OLT:LABEL:UPDATE\",\"OLT:GROUP:READ\",\"OLT:DEVICE:CREATE\",\"OLT:GROUP:CHECK_USE\",\"OLT:DEVICE:READ\",\"OLT:LABEL:CHECK_USE\",\"OLT:LABEL:DELETE\",\"OLT:LABEL:READ\",\"OLT:GROUP:CREATE\"]');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `branch_id` varchar(1024) DEFAULT NULL,
  `branch_name` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (-3,'superadmin','UMP','lethuyet.10.11.1990@gmail.com','$2a$10$Jg2277SUDODoaGcv/exlKeAOYurNy3kXL0R7fx0E/tqRIThG.KTyG','[\"31\"]','[\"SuperAdmin\"]',NULL,NULL,NULL,NULL,1539171457563,1553417405012,NULL,'[\"ONE:PERMISSION:UPDATE\",\"ONE:USER:UPDATE\",\"GLOBAL:BRANCH:UPDATE\",\"GLOBAL:BILL:DELETE\",\"ONE:USER:FORGOT_PASSWORD\",\"ONE:PERMISSION:DELETE\",\"ONE:USER:RESET_PASSWORD\",\"GLOBAL:EMPLOYEE:READ\",\"ONE:ROLE:READ\",\"GLOBAL:BRANCH:CREATE\",\"ONE:USER:DELETE\",\"GLOBAL:BRANCH:READ\",\"GLOBAL:PARTNER:READ\",\"GLOBAL:PARTNER:UPDATE\",\"ONE:ROLE:DELETE\",\"GLOBAL:BILL:EXPORT\",\"ONE:USER:READ\",\"GLOBAL:CUSTOMER:CREATE\",\"GLOBAL:EMPLOYEE:UPDATE\",\"GLOBAL:BILL:CREATE\",\"ONE:PERMISSION:CREATE\",\"ONE:ROLE:UPDATE\",\"ONE:USER:CREATE\",\"GLOBAL:PARTNER:DELETE\",\"GLOBAL:BILL:READ\",\"GLOBAL:PARTNER:CREATE\",\"ONE:ROLE:CREATE\",\"ONE:PERMISSION:READ\",\"GLOBAL:CUSTOMER:DELETE\",\"GLOBAL:EMPLOYEE:DELETE\",\"GLOBAL:EMPLOYEE:SAVE\",\"GLOBAL:BILL:UPDATE\",\"GLOBAL:EMPLOYEE:CREATE\",\"GLOBAL:BILL:IMPORT\",\"GLOBAL:CUSTOMER:READ\",\"GLOBAL:CUSTOMER:UPDATE\"]',NULL,NULL),(140,'ump','UMP','ump@vnpt-technology.vn','$2a$10$UIP/ZN6jSaQ25gAfoopp3eFr4HFxLbSota28560w0/XI97tmcMiYW','[\"31\"]','[\"SuperAdmin\"]',NULL,NULL,NULL,NULL,1539171457563,1539171457691,NULL,'[\"ONE:PERMISSION:UPDATE\",\"ONE:USER:UPDATE\",\"GLOBAL:BRANCH:UPDATE\",\"GLOBAL:BILL:DELETE\",\"ONE:USER:FORGOT_PASSWORD\",\"ONE:PERMISSION:DELETE\",\"ONE:USER:RESET_PASSWORD\",\"GLOBAL:EMPLOYEE:READ\",\"ONE:ROLE:READ\",\"GLOBAL:BRANCH:CREATE\",\"ONE:USER:DELETE\",\"GLOBAL:BRANCH:READ\",\"GLOBAL:PARTNER:READ\",\"GLOBAL:PARTNER:UPDATE\",\"ONE:ROLE:DELETE\",\"GLOBAL:BILL:EXPORT\",\"ONE:USER:READ\",\"GLOBAL:CUSTOMER:CREATE\",\"GLOBAL:EMPLOYEE:UPDATE\",\"GLOBAL:BILL:CREATE\",\"ONE:PERMISSION:CREATE\",\"ONE:ROLE:UPDATE\",\"ONE:USER:CREATE\",\"GLOBAL:PARTNER:DELETE\",\"GLOBAL:BILL:READ\",\"GLOBAL:PARTNER:CREATE\",\"ONE:ROLE:CREATE\",\"ONE:PERMISSION:READ\",\"GLOBAL:CUSTOMER:DELETE\",\"GLOBAL:EMPLOYEE:DELETE\",\"GLOBAL:EMPLOYEE:SAVE\",\"GLOBAL:BILL:UPDATE\",\"GLOBAL:EMPLOYEE:CREATE\",\"GLOBAL:BILL:IMPORT\",\"GLOBAL:CUSTOMER:READ\",\"GLOBAL:CUSTOMER:UPDATE\"]',NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-24 16:11:51
