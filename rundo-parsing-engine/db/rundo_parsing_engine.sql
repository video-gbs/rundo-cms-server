/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : rundo_parsing_engine

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 08/02/2023 10:48:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rundo_channel
-- ----------------------------
DROP TABLE IF EXISTS `rundo_channel`;
CREATE TABLE `rundo_channel`  (
  `id` bigint(0) NOT NULL,
  `device_id` bigint(0) NOT NULL COMMENT '设备id',
  `origin_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原始id',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `in_device_origin_id`(`device_id`, `origin_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_device
-- ----------------------------
DROP TABLE IF EXISTS `rundo_device`;
CREATE TABLE `rundo_device`  (
  `id` bigint(0) NOT NULL,
  `gateway_id` bigint(0) NOT NULL,
  `origin_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原始id',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `in_gateway_origin_id`(`gateway_id`, `origin_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_dispatch
-- ----------------------------
DROP TABLE IF EXISTS `rundo_dispatch`;
CREATE TABLE `rundo_dispatch`  (
  `id` bigint(0) NOT NULL,
  `serial_num` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sign_type` tinyint(0) NOT NULL,
  `gateway_type` tinyint(0) NOT NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `port` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_dispatch_serial_num`(`serial_num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_gateway
-- ----------------------------
DROP TABLE IF EXISTS `rundo_gateway`;
CREATE TABLE `rundo_gateway`  (
  `id` bigint(0) NOT NULL,
  `serial_num` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sign_type` tinyint(0) NOT NULL,
  `gateway_type` tinyint(0) NOT NULL,
  `protocol` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `port` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_gateway_serial_num`(`serial_num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_task
-- ----------------------------
DROP TABLE IF EXISTS `rundo_task`;
CREATE TABLE `rundo_task`  (
  `id` bigint(0) NOT NULL,
  `gateway_id` bigint(0) NOT NULL,
  `device_id` bigint(0) NULL DEFAULT NULL,
  `channel_id` bigint(0) NULL DEFAULT NULL,
  `mq_id` bigint(0) NULL DEFAULT NULL,
  `msg_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `state` tinyint(0) NULL DEFAULT NULL,
  `detail` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
