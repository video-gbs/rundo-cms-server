/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : rundo_device_control

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 08/02/2023 10:47:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rundo_channel
-- ----------------------------
DROP TABLE IF EXISTS `rundo_channel`;
CREATE TABLE `rundo_channel`  (
  `id` bigint(0) NOT NULL,
  `device_id` bigint(0) NOT NULL,
  `sign_state` tinyint(1) NOT NULL COMMENT '注册状态 0-已注册 1-待注册 2-待添加',
  `online_state` tinyint(1) NOT NULL COMMENT '在线状态 0-离线 1-在线',
  `channel_type` tinyint(1) NULL DEFAULT NULL COMMENT '通道类型 1-通道 2-设备',
  `stream_mode` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流模式: TCP & UCP',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_detail
-- ----------------------------
DROP TABLE IF EXISTS `rundo_detail`;
CREATE TABLE `rundo_detail`  (
  `id` bigint(0) NOT NULL,
  `dc_id` bigint(0) NOT NULL,
  `origin_id` varchar(0) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` tinyint(1) NOT NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `port` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `manufacturer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `model` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `firmware` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ptz_type` tinyint(1) NULL DEFAULT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `in_dc_id_type`(`dc_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_device
-- ----------------------------
DROP TABLE IF EXISTS `rundo_device`;
CREATE TABLE `rundo_device`  (
  `id` bigint(0) NOT NULL,
  `gateway_id` bigint(0) NOT NULL,
  `sign_state` tinyint(1) NOT NULL COMMENT '注册状态',
  `device_type` tinyint(1) NOT NULL COMMENT '设备类型',
  `online_state` tinyint(1) NULL DEFAULT NULL COMMENT '在线状态',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_gateway
-- ----------------------------
DROP TABLE IF EXISTS `rundo_gateway`;
CREATE TABLE `rundo_gateway`  (
  `id` bigint(0) NOT NULL,
  `serial_num` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sign_type` tinyint(0) NOT NULL,
  `gateway_type` tinyint(0) NOT NULL,
  `online_state` tinyint(1) NOT NULL,
  `protocol` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `port` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_gateway_serial_num`(`serial_num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
