/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 80029 (8.0.29)
 Source Host           : localhost:3306
 Source Schema         : rundo_parsing_engine

 Target Server Type    : MySQL
 Target Server Version : 80029 (8.0.29)
 File Encoding         : 65001

 Date: 16/02/2023 10:21:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rundo_channel
-- ----------------------------
DROP TABLE IF EXISTS `rundo_channel`;
CREATE TABLE `rundo_channel`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `device_id` bigint NOT NULL,
  `origin_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网关上报原始通道id',
  `update_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_device_origin_id`(`device_id` ASC, `origin_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_device
-- ----------------------------
DROP TABLE IF EXISTS `rundo_device`;
CREATE TABLE `rundo_device`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `gateway_id` bigint NOT NULL COMMENT '网关id',
  `origin_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '映射设备id',
  `update_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_gateway_origin_id`(`gateway_id` ASC, `origin_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_dispatch
-- ----------------------------
DROP TABLE IF EXISTS `rundo_dispatch`;
CREATE TABLE `rundo_dispatch`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `serial_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sign_type` tinyint(1) NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `port` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_dispatch_serial_num`(`serial_num` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_gateway
-- ----------------------------
DROP TABLE IF EXISTS `rundo_gateway`;
CREATE TABLE `rundo_gateway`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `serial_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网关ID',
  `sign_type` tinyint NOT NULL COMMENT '1-MQ  2-RETFUL',
  `gateway_type` tinyint NOT NULL COMMENT '网关类型 1-设备 2-NVR 3-DVR 4-CVR 5-XVR',
  `protocol` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网关协议',
  `ip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网关ip地址',
  `port` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '端口',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_gateway_serial_num`(`serial_num` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_gateway_task
-- ----------------------------
DROP TABLE IF EXISTS `rundo_gateway_task`;
CREATE TABLE `rundo_gateway_task`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `gateway_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '网关id',
  `device_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '设备id',
  `channel_id` bigint NULL DEFAULT NULL COMMENT '通道id',
  `mq_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息队列id',
  `msg_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息类型',
  `state` tinyint NOT NULL COMMENT '0->执行中 1->已完成 -1->异常',
  `detail` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详情',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 237 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_stream_task
-- ----------------------------
DROP TABLE IF EXISTS `rundo_stream_task`;
CREATE TABLE `rundo_stream_task`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dispatch_id` bigint NOT NULL,
  `channel_id` bigint NULL DEFAULT NULL,
  `stream_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `msg_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `state` tinyint(1) NOT NULL COMMENT '0->执行中 1->已完成 -1->异常',
  `detail` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详情',
  `mq_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
