/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : library_management_system

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 14/12/2024 16:11:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book_categories
-- ----------------------------
DROP TABLE IF EXISTS `book_categories`;
CREATE TABLE `book_categories`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_categories
-- ----------------------------
INSERT INTO `book_categories` VALUES (10, 'Arts');
INSERT INTO `book_categories` VALUES (9, 'Business');
INSERT INTO `book_categories` VALUES (4, 'History');
INSERT INTO `book_categories` VALUES (7, 'Literature');
INSERT INTO `book_categories` VALUES (6, 'Medicine');
INSERT INTO `book_categories` VALUES (3, 'Mystery');
INSERT INTO `book_categories` VALUES (5, 'Programming');
INSERT INTO `book_categories` VALUES (2, 'Romance');
INSERT INTO `book_categories` VALUES (1, 'Science Fiction');
INSERT INTO `book_categories` VALUES (8, 'Travel');

-- ----------------------------
-- Table structure for books
-- ----------------------------
DROP TABLE IF EXISTS `books`;
CREATE TABLE `books`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `publication_date` date NOT NULL,
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `category_id` bigint NOT NULL,
  `attribute` json NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `isbn`(`isbn` ASC) USING BTREE,
  INDEX `idx_isbn`(`isbn` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of books
-- ----------------------------
INSERT INTO `books` VALUES (26, '三体', '刘慈欣', '2008-05-01', '9787229100511', 1, '{\"researchLevel\": \"结合了硬科幻与哲学思考，具有较高的科学研究水平\", \"scientificField\": \"宇宙学，物理学，天文学\"}');
INSERT INTO `books` VALUES (27, '步步惊心', '桐华', '2005-09-01', '9787506362072', 2, '{\"romanceType\": \"现代浪漫爱情，包含校园爱情\", \"settingPeriod\": \"在当代，故事围绕学生之间\"}');
INSERT INTO `books` VALUES (28, '解忧杂货店', '东野圭吾', '2012-03-01', '9787544253559', 3, '{\"subgenre\": \"悬疑\", \"detectiveType\": \"法医侦探，侧重于法医科学与推理的结合，侦探角色可能是法医专家，案件通常涉及复杂的尸检和法医证据\"}');
INSERT INTO `books` VALUES (29, '史记', '司马迁', '2012-03-01', '9787020018798', 4, '{\"historicalPeriod\": \"历史时期涵盖了从黄帝时期（约公元前2697年）到汉武帝时期（公元前87年）\", \"geographicalRegion\": \"涵盖了中国古代的多个重要地区\"}');
INSERT INTO `books` VALUES (30, '深入理解计算机系统', 'Randal E. Bryant', '2009-03-01', '9787111154018', 5, '{\"technicalField\": \"操作系统\", \"programmingLanguage\": \"c,c++\"}');
INSERT INTO `books` VALUES (31, '医学的逻辑', '刘永灼', '2016-05-01', '9787509173957', 6, '{\"clinicalFocus\": null, \"targetAudience\": null, \"researchContent\": null, \"medicalSpecialty\": null}');
INSERT INTO `books` VALUES (32, '艺术的故事', 'E.H. Gombrich', '2004-03-01', '9787108017527', 10, '{\"artPeriod\": \"中世纪艺术\"}');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` enum('user','admin') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'alice', 'password123', 'user');
INSERT INTO `users` VALUES (2, 'bob', 'admin456', 'admin');
INSERT INTO `users` VALUES (3, 'charlie', 'mypassword', 'user');
INSERT INTO `users` VALUES (4, 'david', 'secure789', 'admin');
INSERT INTO `users` VALUES (5, 'admin', 'admin123', 'user');
INSERT INTO `users` VALUES (6, 'root', 'root123', 'user');
INSERT INTO `users` VALUES (7, 'rootadmin', 'rootadmin', 'admin');
INSERT INTO `users` VALUES (8, 'test', 'testtest', 'user');
INSERT INTO `users` VALUES (9, 'testtestt', 'testtest', 'admin');
INSERT INTO `users` VALUES (10, '1111111', '222222222222', 'user');
INSERT INTO `users` VALUES (11, '222222222', '3333333', 'admin');
INSERT INTO `users` VALUES (12, 'aaaaa', 'bbbbbbbb', 'user');
INSERT INTO `users` VALUES (13, '11111111111111111', '1111111111111111', 'user');

SET FOREIGN_KEY_CHECKS = 1;
