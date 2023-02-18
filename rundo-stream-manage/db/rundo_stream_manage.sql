/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : rundo_stream_manage

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 08/02/2023 10:48:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rundo_dispatch
-- ----------------------------
DROP TABLE IF EXISTS `rundo_dispatch`;
CREATE TABLE `rundo_dispatch`  (
  `id` bigint(0) NOT NULL,
  `serial_num` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `online_state` tinyint(1) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `port` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `url` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_dispatch_serial_num`(`serial_num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_gateway_dispatch
-- ----------------------------
DROP TABLE IF EXISTS `rundo_gateway_dispatch`;
CREATE TABLE `rundo_gateway_dispatch`  (
  `id` bigint(0) NOT NULL,
  `gateway_id` bigint(0) NOT NULL,
  `dispatch_id` bigint(0) NOT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_gateway_id`(`gateway_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_media
-- ----------------------------
DROP TABLE IF EXISTS `rundo_media`;
CREATE TABLE `rundo_media`  (
  `id` bigint(0) NOT NULL,
  `dispatch_id` bigint(0) NOT NULL,
  `type` tinyint(1) NOT NULL COMMENT '1-zlm',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `port` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `origin_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_stream
-- ----------------------------
DROP TABLE IF EXISTS `rundo_stream`;
CREATE TABLE `rundo_stream`  (
  `id` bigint(0) NOT NULL,
  `gateway_id` bigint(0) NOT NULL,
  `channel_id` bigint(0) NOT NULL,
  `stream_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `dispatch_id` bigint(0) NOT NULL,
  `play_type` tinyint(1) NOT NULL COMMENT '1-直播 2-录播 3-下载',
  `record_state` tinyint(1) NOT NULL COMMENT '录像状态 0-关闭 1-开启',
  `auto_close_state` tinyint(1) NOT NULL COMMENT '是否无人观看时自动关闭 0-关闭 1-开启',
  `stream_state` tinyint(1) NOT NULL COMMENT '流状态 0-准备中 1-播放中',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_stream_id`(`stream_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
