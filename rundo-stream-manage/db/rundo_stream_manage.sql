/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 80029 (8.0.29)
 Source Host           : localhost:3306
 Source Schema         : rundo_stream_manage

 Target Server Type    : MySQL
 Target Server Version : 80029 (8.0.29)
 File Encoding         : 65001

 Date: 16/02/2023 10:22:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rundo_dispatch
-- ----------------------------
DROP TABLE IF EXISTS `rundo_dispatch`;
CREATE TABLE `rundo_dispatch`  (
  `id` bigint NOT NULL,
  `serial_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `online_state` tinyint(1) NOT NULL COMMENT '0-离线 1-在线',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `port` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_dispatch_serial_num`(`serial_num` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_gateway_dispatch
-- ----------------------------
DROP TABLE IF EXISTS `rundo_gateway_dispatch`;
CREATE TABLE `rundo_gateway_dispatch`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gateway_id` bigint NOT NULL,
  `dispatch_id` bigint NOT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_gateway_id`(`gateway_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_media
-- ----------------------------
DROP TABLE IF EXISTS `rundo_media`;
CREATE TABLE `rundo_media`  (
  `id` bigint NOT NULL,
  `dispatch_id` bigint NOT NULL,
  `origin_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `port` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` tinyint NOT NULL COMMENT '1-zlm',
  `update_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_stream
-- ----------------------------
DROP TABLE IF EXISTS `rundo_stream`;
CREATE TABLE `rundo_stream`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gateway_id` bigint NOT NULL COMMENT '网关id',
  `channel_id` bigint NOT NULL COMMENT '通道id',
  `dispatch_id` bigint NOT NULL COMMENT '调度中心id',
  `stream_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流id LIVE-直播 RECORD-录播 DOWNLOAD-下载',
  `play_type` tinyint(1) NOT NULL COMMENT '1-直播 2-录播 3-下载',
  `record_state` tinyint(1) NOT NULL COMMENT '0-关闭 1-开启',
  `auto_close_state` tinyint(1) NOT NULL COMMENT '0-关闭 1-开启 是否触发无人观看',
  `stream_state` tinyint(1) NOT NULL COMMENT '0-准备中 1-播放中',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_stream_id`(`stream_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
