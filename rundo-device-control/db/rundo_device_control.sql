/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 80029 (8.0.29)
 Source Host           : localhost:3306
 Source Schema         : rundo_device_control

 Target Server Type    : MySQL
 Target Server Version : 80029 (8.0.29)
 File Encoding         : 65001

 Date: 16/02/2023 10:21:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rundo_channel
-- ----------------------------
DROP TABLE IF EXISTS `rundo_channel`;
CREATE TABLE `rundo_channel`  (
  `id` bigint UNSIGNED NOT NULL,
  `device_id` bigint NOT NULL COMMENT '设备id',
  `sign_state` tinyint NOT NULL COMMENT '0-已注册 1-待注册 2-待添加',
  `online_state` tinyint NOT NULL COMMENT '0-离线 1-在线',
  `channel_type` tinyint NOT NULL COMMENT '1-通道 2-设备',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_detail
-- ----------------------------
DROP TABLE IF EXISTS `rundo_detail`;
CREATE TABLE `rundo_detail`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `dc_id` bigint NOT NULL COMMENT '设备或者通道的id',
  `origin_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原始id',
  `type` tinyint NOT NULL COMMENT '数据类型 1-通道 2-设备',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `port` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '端口',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `manufacturer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备厂商',
  `model` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '型号',
  `firmware` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '固件版本',
  `ptz_type` tinyint NULL DEFAULT NULL COMMENT '云台类型',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dc_id_type`(`dc_id` ASC, `type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_device
-- ----------------------------
DROP TABLE IF EXISTS `rundo_device`;
CREATE TABLE `rundo_device`  (
  `id` bigint UNSIGNED NOT NULL,
  `gateway_id` bigint NOT NULL COMMENT '网关id',
  `sign_state` tinyint NOT NULL COMMENT '0- 已注册 1-待注册 2-待添加 ',
  `device_type` tinyint NULL DEFAULT NULL COMMENT '1-设备 2-NVR 3-DVR 4-CVR',
  `online_state` tinyint NOT NULL COMMENT '0-离线 1-在线',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rundo_gateway
-- ----------------------------
DROP TABLE IF EXISTS `rundo_gateway`;
CREATE TABLE `rundo_gateway`  (
  `id` bigint UNSIGNED NOT NULL,
  `serial_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网关ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网关名称',
  `sign_type` tinyint NOT NULL COMMENT '1-MQ  2-RETFUL',
  `online_state` tinyint NULL DEFAULT NULL COMMENT '状态：0-离线 1-在线',
  `gateway_type` tinyint NOT NULL COMMENT '网关类型 1-设备 2-NVR 3-DVR 4-CVR',
  `protocol` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网关协议',
  `ip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网关ip地址',
  `port` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '端口',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_gateway_serial_num`(`serial_num` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
