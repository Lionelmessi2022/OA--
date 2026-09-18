-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: oa_db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `dept`
--

DROP TABLE IF EXISTS `dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dept` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dept`
--

LOCK TABLES `dept` WRITE;
/*!40000 ALTER TABLE `dept` DISABLE KEYS */;
INSERT INTO `dept` VALUES (1,'技术部','负责产品研发与技术支持','2026-09-10 08:57:49'),(2,'人事部','负责招聘、考勤与员工关系','2026-09-10 08:57:49'),(3,'财务部','负责财务核算与资金管理','2026-09-10 08:57:49'),(4,'市场部','负责市场推广与品牌运营','2026-09-10 08:57:49'),(5,'运营部','负责产品运营与客户服务','2026-09-10 08:57:49'),(6,'行政部','负责行政后勤与办公支持','2026-09-10 08:57:49'),(7,'采购部','负责物资采购与供应商管理','2026-09-12 11:02:40'),(8,'法务部','负责合同审核与法律合规','2026-09-12 11:02:40'),(9,'客服部','负责客户咨询与投诉处理','2026-09-12 11:02:40'),(10,'设计部','负责视觉设计与UI/UX','2026-09-12 11:02:40'),(11,'产品部','负责产品规划与需求分析','2026-09-12 11:02:40'),(12,'数据分析部','负责数据挖掘与业务分析','2026-09-12 11:02:40'),(13,'质量保障部','负责软件测试与质量管控','2026-09-12 11:02:40'),(14,'公关部','负责媒体关系与品牌形象','2026-09-12 11:02:40');
/*!40000 ALTER TABLE `dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `emp`
--

DROP TABLE IF EXISTS `emp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emp` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `emp_no` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工编号',
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账号',
  `password` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'MD5密码',
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `gender` tinyint DEFAULT '1' COMMENT '1男 0女',
  `phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT '',
  `email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT '',
  `avatar` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '',
  `dept_id` bigint DEFAULT NULL,
  `job_id` bigint DEFAULT NULL,
  `hire_date` date DEFAULT NULL,
  `role` varchar(20) COLLATE utf8mb4_general_ci DEFAULT 'employee' COMMENT 'admin/employee',
  `status` tinyint DEFAULT '1' COMMENT '1启用 0禁用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `emp_no` (`emp_no`),
  UNIQUE KEY `username` (`username`),
  KEY `fk_emp_dept` (`dept_id`),
  KEY `fk_emp_job` (`job_id`),
  CONSTRAINT `fk_emp_dept` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`),
  CONSTRAINT `fk_emp_job` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='员工表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emp`
--

LOCK TABLES `emp` WRITE;
/*!40000 ALTER TABLE `emp` DISABLE KEYS */;
INSERT INTO `emp` VALUES (1,'EMP001','admin','e10adc3949ba59abbe56e057f20f883e','系统管理员',1,'13900000000','admin@oa.com','http://localhost:8083/uploads/0b1ee384d6d2491ea881b2f6b78b8587.jpg',1,4,'2022-01-01','admin',1),(2,'EMP002','zhangsan','96e79218965eb72c92a549dd5a330112','张三',1,'13911112222','zhangsan@oa.com','http://localhost:8083/uploads/cb2df8194f2343f59ab0e2f1b0309fa4.jpg',1,1,'2023-07-01','employee',1),(3,'EMP003','user3','e10adc3949ba59abbe56e057f20f883e','王芳',0,'13800000003','user3@oa.com','',2,6,'2023-04-12','employee',1),(4,'EMP004','user4','e10adc3949ba59abbe56e057f20f883e','李娜',0,'13800000004','user4@oa.com','',3,7,'2022-09-01','employee',1),(5,'EMP005','user5','e10adc3949ba59abbe56e057f20f883e','刘洋',1,'13800000005','user5@oa.com','',4,8,'2024-02-19','employee',1),(6,'EMP006','user6','e10adc3949ba59abbe56e057f20f883e','陈静',0,'13800000006','user6@oa.com','',5,4,'2021-06-08','employee',0),(7,'EMP007','user7','e10adc3949ba59abbe56e057f20f883e','杨勇',1,'13800000007','user7@oa.com','',6,5,'2023-11-23','employee',1),(8,'EMP008','user8','e10adc3949ba59abbe56e057f20f883e','黄敏',0,'13800000008','user8@oa.com','',1,2,'2022-03-15','employee',1),(9,'EMP009','user9','e10adc3949ba59abbe56e057f20f883e','周杰',1,'13800000009','user9@oa.com','',2,6,'2024-05-06','employee',1),(10,'EMP010','user10','e10adc3949ba59abbe56e057f20f883e','吴婷',0,'13800000010','user10@oa.com','',3,7,'2021-12-01','employee',1),(11,'EMP011','user11','e10adc3949ba59abbe56e057f20f883e','徐磊',1,'13800000011','user11@oa.com','',4,8,'2023-08-14','employee',1),(12,'EMP012','user12','e10adc3949ba59abbe56e057f20f883e','孙丽',0,'13800000012','user12@oa.com','',5,3,'2022-07-25','employee',1),(13,'EMP013','user13','e10adc3949ba59abbe56e057f20f883e','马超',1,'13800000013','user13@oa.com','',6,5,'2024-01-08','employee',1),(14,'EMP014','user14','e10adc3949ba59abbe56e057f20f883e','朱琳',0,'13800000014','user14@oa.com','',1,1,'2023-02-27','employee',1),(15,'EMP015','user15','e10adc3949ba59abbe56e057f20f883e','胡军',1,'13800000015','user15@oa.com','',2,6,'2021-10-11','employee',1),(16,'EMP016','user16','e10adc3949ba59abbe56e057f20f883e','郭涛',1,'13800000016','user16@oa.com','',3,7,'2022-11-30','employee',1),(17,'EMP017','user17','e10adc3949ba59abbe56e057f20f883e','高霞',0,'13800000017','user17@oa.com','',4,8,'2024-03-18','employee',1),(18,'EMP018','user18','e10adc3949ba59abbe56e057f20f883e','林峰',1,'13800000018','user18@oa.com','',5,2,'2023-06-05','employee',1),(19,'EMP019','user19','e10adc3949ba59abbe56e057f20f883e','罗丹',0,'13800000019','user19@oa.com','',6,5,'2022-05-20','employee',1),(20,'EMP020','user20','e10adc3949ba59abbe56e057f20f883e','梁宇',1,'13800000020','user20@oa.com','',1,3,'2024-04-09','employee',1),(21,'EMP021','user21','e10adc3949ba59abbe56e057f20f883e','宋佳',0,'13800000021','user21@oa.com','',2,6,'2021-08-16','employee',1),(22,'EMP022','user22','e10adc3949ba59abbe56e057f20f883e','唐亮',1,'13800000022','user22@oa.com','',3,2,'2023-09-28','employee',1),(23,'EMP023','user23','e10adc3949ba59abbe56e057f20f883e','许晴',0,'13800000023','user23@oa.com','',4,8,'2022-12-12','employee',1),(24,'EMP024','EMP024','e10adc3949ba59abbe56e057f20f883e','阳立',1,'13800000023','user24@oa.com','http://localhost:8083/uploads/3dc4c6f850f049cdaed8b5627a095a0f.jpg',1,1,'2026-09-14','admin',1),(25,'EMP025','user25','e10adc3949ba59abbe56e057f20f883e','张伟',1,'13800000025','user25@oa.com',NULL,1,2,'2023-05-12','employee',1),(26,'EMP026','user26','e10adc3949ba59abbe56e057f20f883e','李娜',0,'13800000026','user26@oa.com',NULL,2,6,'2022-11-01','employee',1),(27,'EMP027','user27','e10adc3949ba59abbe56e057f20f883e','王强',1,'13800000027','user27@oa.com',NULL,3,7,'2021-08-20','employee',1),(28,'EMP028','user28','e10adc3949ba59abbe56e057f20f883e','刘洋',0,'13800000028','user28@oa.com',NULL,4,8,'2024-01-15','employee',1),(29,'EMP029','user29','e10adc3949ba59abbe56e057f20f883e','陈晨',1,'13800000029','user29@oa.com',NULL,5,12,'2023-03-10','employee',1),(30,'EMP030','user30','e10adc3949ba59abbe56e057f20f883e','杨帆',0,'13800000030','user30@oa.com',NULL,6,14,'2022-07-05','employee',1),(31,'EMP031','user31','e10adc3949ba59abbe56e057f20f883e','赵磊',1,'13800000031','user31@oa.com',NULL,7,13,'2021-12-11','employee',1),(32,'EMP032','user32','e10adc3949ba59abbe56e057f20f883e','黄丽',0,'13800000032','user32@oa.com',NULL,8,14,'2024-04-22','employee',1),(33,'EMP033','user33','e10adc3949ba59abbe56e057f20f883e','周涛',1,'13800000033','user33@oa.com',NULL,9,15,'2023-09-18','employee',1),(34,'EMP034','user34','e10adc3949ba59abbe56e057f20f883e','吴静',0,'13800000034','user34@oa.com',NULL,10,5,'2022-02-28','employee',1),(35,'EMP035','user35','e10adc3949ba59abbe56e057f20f883e','徐明',1,'13800000035','user35@oa.com',NULL,11,4,'2021-06-14','employee',1),(36,'EMP036','user36','e10adc3949ba59abbe56e057f20f883e','孙丽',0,'13800000036','user36@oa.com',NULL,12,11,'2024-08-08','employee',1),(37,'EMP037','user37','e10adc3949ba59abbe56e057f20f883e','马超',1,'13800000037','user37@oa.com',NULL,13,3,'2023-10-30','employee',1),(38,'EMP038','user38','e10adc3949ba59abbe56e057f20f883e','朱琳',0,'13800000038','user38@oa.com',NULL,14,12,'2022-05-16','employee',1),(39,'EMP039','user39','e10adc3949ba59abbe56e057f20f883e','胡军',1,'13800000039','user39@oa.com',NULL,1,1,'2021-11-03','employee',1),(40,'EMP040','user40','e10adc3949ba59abbe56e057f20f883e','郭婷',0,'13800000040','user40@oa.com',NULL,2,16,'2024-03-25','employee',1),(41,'EMP041','user41','e10adc3949ba59abbe56e057f20f883e','高翔',1,'13800000041','user41@oa.com',NULL,3,7,'2023-07-12','employee',1),(42,'EMP042','user42','e10adc3949ba59abbe56e057f20f883e','林燕',0,'13800000042','user42@oa.com',NULL,4,8,'2022-09-09','employee',1),(43,'EMP043','user43','e10adc3949ba59abbe56e057f20f883e','罗鹏',1,'13800000043','user43@oa.com',NULL,5,9,'2024-06-18','employee',1),(44,'EMP044','user44','e10adc3949ba59abbe56e057f20f883e','梁红',0,'13800000044','user44@oa.com',NULL,6,14,'2021-04-01','employee',1),(47,'EMP045','EMP045','e10adc3949ba59abbe56e057f20f883e','赵阳',0,'15463925465','zhangsan@oa.com','',14,5,'2026-09-28','employee',1);
/*!40000 ALTER TABLE `emp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '职位名称',
  `sort` int DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='职位表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (1,'前端工程师',1),(2,'后端工程师',2),(3,'测试工程师',3),(4,'产品经理',4),(5,'UI设计师',5),(6,'人事专员',6),(7,'财务专员',7),(8,'市场专员',8),(9,'运维工程师',9),(10,'算法工程师',10),(11,'数据分析师',11),(12,'新媒体运营',12),(13,'项目经理',13),(14,'行政专员',14),(15,'销售代表',15),(16,'客户成功经理',16);
/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-18 17:11:30
