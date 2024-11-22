-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: ocpp_16
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `charge_history`
--

DROP TABLE IF EXISTS `charge_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `charge_history` (
  `charge_history_id` int NOT NULL AUTO_INCREMENT,
  `start_time` timestamp NOT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `total_meter_value` decimal(10,2) NOT NULL COMMENT '하나의 트랜잭션의 총 Wh',
  `total_price` decimal(10,2) NOT NULL COMMENT '하나의 트랜잭션의 총 가격(충전 시점의 단가 * Wh)',
  `charge_step` enum('START_TRANSACTION','METER_VALUES','STOP_TRANSACTION') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `charger_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  PRIMARY KEY (`charge_history_id`),
  KEY `charge_history_ibfk_1` (`charger_id`),
  KEY `charge_history_member_FK` (`member_id`),
  CONSTRAINT `charge_history_ibfk_1` FOREIGN KEY (`charger_id`) REFERENCES `charger` (`charger_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `charge_history_member_FK` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `charge_history`
--

LOCK TABLES `charge_history` WRITE;
/*!40000 ALTER TABLE `charge_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `charge_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `charge_history_detail`
--

DROP TABLE IF EXISTS `charge_history_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `charge_history_detail` (
  `charge_history_detail_id` bigint NOT NULL AUTO_INCREMENT,
  `charge_step` enum('START_TRANSACTION','METER_VALUES','STOP_TRANSACTION') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `action_date` timestamp NULL DEFAULT NULL,
  `meter_value` decimal(10,2) NOT NULL COMMENT 'Wh',
  `unit_price` int NOT NULL COMMENT '충전 시점의 Wh 당 단가',
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `charge_history_id` int NOT NULL,
  PRIMARY KEY (`charge_history_detail_id`),
  KEY `charge_history_detail_ibfk_1` (`charge_history_id`),
  CONSTRAINT `charge_history_detail_charge_history_FK` FOREIGN KEY (`charge_history_id`) REFERENCES `charge_history` (`charge_history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `charge_history_detail`
--

LOCK TABLES `charge_history_detail` WRITE;
/*!40000 ALTER TABLE `charge_history_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `charge_history_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `charger`
--

DROP TABLE IF EXISTS `charger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `charger` (
  `charger_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `serial_number` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `model` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `vendor` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `firmware_version` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `connection_status` enum('CONNECTED','DISCONNECTED') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'DISCONNECTED',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `site_id` int DEFAULT NULL,
  PRIMARY KEY (`charger_id`),
  UNIQUE KEY `serial_number` (`serial_number`),
  KEY `charger_site_FK` (`site_id`),
  CONSTRAINT `charger_site_FK` FOREIGN KEY (`site_id`) REFERENCES `site` (`site_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `charger`
--

LOCK TABLES `charger` WRITE;
/*!40000 ALTER TABLE `charger` DISABLE KEYS */;
INSERT INTO `charger` VALUES (1,'TEST','1234','ModelXY','VendorXYZ','1.3','DISCONNECTED',1,'2024-10-31 09:13:22','2024-11-21 12:38:05',4);
/*!40000 ALTER TABLE `charger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `charger_connector`
--

DROP TABLE IF EXISTS `charger_connector`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `charger_connector` (
  `charger_connector_id` bigint NOT NULL AUTO_INCREMENT,
  `connector_id` int NOT NULL,
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '임시',
  `charge_point_status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `charger_id` bigint NOT NULL,
  PRIMARY KEY (`charger_connector_id`),
  UNIQUE KEY `charger_id` (`charger_id`,`connector_id`),
  CONSTRAINT `charger_connector_ibfk_1` FOREIGN KEY (`charger_id`) REFERENCES `charger` (`charger_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `charger_connector`
--

LOCK TABLES `charger_connector` WRITE;
/*!40000 ALTER TABLE `charger_connector` DISABLE KEYS */;
INSERT INTO `charger_connector` VALUES (1,1,NULL,'Available','2024-11-15 01:21:56','2024-11-22 12:21:58',1),(2,2,NULL,'Available','2024-11-15 01:21:56','2024-11-22 12:22:25',1);
/*!40000 ALTER TABLE `charger_connector` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `charger_error`
--

DROP TABLE IF EXISTS `charger_error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `charger_error` (
  `charger_error_id` int NOT NULL AUTO_INCREMENT,
  `error_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`charger_error_id`),
  UNIQUE KEY `code` (`error_code`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `charger_error`
--

LOCK TABLES `charger_error` WRITE;
/*!40000 ALTER TABLE `charger_error` DISABLE KEYS */;
INSERT INTO `charger_error` VALUES (1,'ConnectorLockFailure','커넥터 잠금 또는 잠금 해제 실패','2024-11-13 21:07:55','2024-11-13 21:07:55'),(2,'EVCommunicationError','차량과의 통신 실패','2024-11-13 21:07:55','2024-11-13 21:07:55'),(3,'GroundFailure','접지 차단 장치가 작동','2024-11-13 21:07:55','2024-11-13 21:07:55'),(4,'HighTemperature','내부 온도가 너무 높음','2024-11-13 21:07:55','2024-11-13 21:07:55'),(5,'InternalError','내부 하드웨어 또는 소프트웨어 구성요소 오류','2024-11-13 21:07:55','2024-11-13 21:07:55'),(6,'LocalListConflict','인증 정보 충돌','2024-11-13 21:07:55','2024-11-13 21:07:55'),(7,'NoError','오류 없음','2024-11-13 21:07:55','2024-11-13 21:07:55'),(8,'OtherError','기타 오류','2024-11-13 21:07:55','2024-11-13 21:07:55'),(9,'OverCurrentFailure','과전류 보호 장치 작동','2024-11-13 21:07:55','2024-11-13 21:07:55'),(10,'OverVoltage','전압 허용 수준 초과','2024-11-13 21:07:55','2024-11-13 21:07:55'),(11,'PowerMeterFailure','전력계 읽기 실패','2024-11-13 21:07:55','2024-11-13 21:07:55'),(12,'PowerSwitchFailure','전원 스위치 제어 실패','2024-11-13 21:07:55','2024-11-13 21:07:55'),(13,'ReaderFailure','idTag 리더기 오류','2024-11-13 21:07:55','2024-11-13 21:07:55'),(14,'ResetFailure','재설정 수행 불가','2024-11-13 21:07:55','2024-11-13 21:07:55'),(15,'UnderVoltage','전압이 허용 수준 아래로 떨어짐','2024-11-13 21:07:55','2024-11-13 21:07:55'),(16,'WeakSignal','무선 통신 장치 약한 신호','2024-11-13 21:07:55','2024-11-13 21:07:55');
/*!40000 ALTER TABLE `charger_error` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `charger_error_history`
--

DROP TABLE IF EXISTS `charger_error_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `charger_error_history` (
  `charger_error_history_id` int NOT NULL AUTO_INCREMENT,
  `info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `status` enum('active','resolved') DEFAULT 'active',
  `error_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `resolved_date` timestamp NULL DEFAULT NULL,
  `charger_connector_id` bigint NOT NULL,
  `charger_error_id` int NOT NULL,
  PRIMARY KEY (`charger_error_history_id`),
  KEY `charger_error_history_charger_connector_FK` (`charger_connector_id`),
  KEY `charger_error_history_charger_error_FK` (`charger_error_id`),
  CONSTRAINT `charger_error_history_charger_connector_FK` FOREIGN KEY (`charger_connector_id`) REFERENCES `charger_connector` (`charger_connector_id`),
  CONSTRAINT `charger_error_history_charger_error_FK` FOREIGN KEY (`charger_error_id`) REFERENCES `charger_error` (`charger_error_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `charger_error_history`
--

LOCK TABLES `charger_error_history` WRITE;
/*!40000 ALTER TABLE `charger_error_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `charger_error_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_token` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `roles` enum('ROLE_ADMIN','ROLE_USER') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ROLE_USER',
  `account_status` enum('ACTIVE','LOCKED','INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVE',
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `member_unique` (`id_token`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site`
--

DROP TABLE IF EXISTS `site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `site` (
  `site_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `city` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `street` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `zipcode` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site`
--

LOCK TABLES `site` WRITE;
/*!40000 ALTER TABLE `site` DISABLE KEYS */;
INSERT INTO `site` VALUES (4,'TEST',NULL,NULL,NULL,'2024-10-31 09:13:31','2024-10-31 09:13:54');
/*!40000 ALTER TABLE `site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'ocpp_16'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-22 21:23:26
