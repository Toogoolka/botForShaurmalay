-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: shaurmalay_db
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `good_types`
--

DROP TABLE IF EXISTS `good_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `good_types` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `good_types`
--

LOCK TABLES `good_types` WRITE;
/*!40000 ALTER TABLE `good_types` DISABLE KEYS */;
INSERT INTO `good_types` VALUES (1,'Shaurma'),(2,'Drinks'),(3,'Starters');
/*!40000 ALTER TABLE `good_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods`
--

DROP TABLE IF EXISTS `goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `callback_data` varchar(255) DEFAULT NULL,
  `emoji` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` int NOT NULL,
  `type_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlp8f9dotbcgh9gvkvdk66fy04` (`type_id`),
  CONSTRAINT `FKlp8f9dotbcgh9gvkvdk66fy04` FOREIGN KEY (`type_id`) REFERENCES `good_types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods`
--

LOCK TABLES `goods` WRITE;
/*!40000 ALTER TABLE `goods` DISABLE KEYS */;
INSERT INTO `goods` VALUES (1,'CLASSIC',':burrito:','Классика',200,1),(2,'GORGEOUS',':drooling_face:','Балдёжная',210,1),(3,'LUKAS',':burrito:','Лукас',220,1),(4,'CHEESY',':cheese:','Сырная',220,1),(5,'HOT',':hot_pepper:','Острая',220,1),(6,'FREAK',':fries:','Фрик',220,1),(7,'ERICH',':potato:','Эрик',220,1),(8,'MUSHROOM',':burrito:','Грибная',220,1),(9,'ABZY',':dumpling:','Абзы',220,1),(10,'PIGGY',':bacon:','Свиниамин',220,1),(11,'COUNTRY',':house_with_garden:','Деревня',230,1),(14,'TERIYAKI',':burrito:','Терияки',220,1),(15,'PRETTY',':dancer:','Красотка',220,1),(16,'EXECUTIONER',':crown:','Палач',250,1),(17,'KAIF',':drooling_face:','Каеф',220,1);
/*!40000 ALTER TABLE `goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingridients`
--

DROP TABLE IF EXISTS `ingridients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingridients` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `price` int NOT NULL,
  `good_id` bigint DEFAULT NULL,
  `callback_data` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKi6g4fk076s7bw57c5qql1nt9y` (`good_id`),
  CONSTRAINT `FKi6g4fk076s7bw57c5qql1nt9y` FOREIGN KEY (`good_id`) REFERENCES `goods` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingridients`
--

LOCK TABLES `ingridients` WRITE;
/*!40000 ALTER TABLE `ingridients` DISABLE KEYS */;
INSERT INTO `ingridients` VALUES (1,'Терияки',30,NULL,'TERIYAKI_BUFF'),(2,'Сырный соус',30,NULL,'CHEESY_SOUSE_BUFF'),(3,'Мар. огурец',40,NULL,'PICKLES_BUFF'),(4,'Мар. лучок',40,NULL,'ONION_BUFF'),(5,'Сыр Чеддер',50,NULL,'CHEDDAR_BUFF'),(6,'Халапеньо',35,NULL,'JALAPENO_BUFF'),(7,'Карртоха',45,NULL,'FRIES_BUFF'),(8,'Грибы',45,NULL,'MUSHROOMS_BUFF'),(9,'Жареный бекон',65,NULL,'BACON_BUFF'),(10,'Двойное мясо',100,NULL,'DOUBLE_MEAT_BUFF');
/*!40000 ALTER TABLE `ingridients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount_items` int NOT NULL,
  `order_sum` int NOT NULL,
  `ordered_at` datetime(6) DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `positions` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsjfs85qf6vmcurlx43cnc16gy` (`customer_id`),
  CONSTRAINT `FKsjfs85qf6vmcurlx43cnc16gy` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (1,'USER'),(2,'DELIVERYMAN'),(3,'ADMIN');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status_users`
--

DROP TABLE IF EXISTS `status_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status_users` (
  `user_chat_id` bigint NOT NULL,
  `status_id` bigint NOT NULL,
  KEY `FKj9obyp05mm9s49xa7btb02pk9` (`status_id`),
  KEY `FK8983ys9q95t15kmicxkhittjc` (`user_chat_id`),
  CONSTRAINT `FK8983ys9q95t15kmicxkhittjc` FOREIGN KEY (`user_chat_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKj9obyp05mm9s49xa7btb02pk9` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status_users`
--

LOCK TABLES `status_users` WRITE;
/*!40000 ALTER TABLE `status_users` DISABLE KEYS */;
INSERT INTO `status_users` VALUES (1,1);
/*!40000 ALTER TABLE `status_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `chat_id` bigint DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `registered_at` datetime(6) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,537130248,'Vladislav','T','2023-10-25 13:07:16.013000','Fruktoviy_goose');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_seq`
--

DROP TABLE IF EXISTS `users_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_seq`
--

LOCK TABLES `users_seq` WRITE;
/*!40000 ALTER TABLE `users_seq` DISABLE KEYS */;
INSERT INTO `users_seq` VALUES (101);
/*!40000 ALTER TABLE `users_seq` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-26  1:56:00
