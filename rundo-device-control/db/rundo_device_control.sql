CREATE TABLE `rundo_channel` (
                                 `id` bigint unsigned NOT NULL,
                                 `device_id` bigint NOT NULL COMMENT '设备id',
                                 `sign_state` tinyint NOT NULL COMMENT '0-已注册 1-待注册 2-待添加',
                                 `online_state` tinyint NOT NULL COMMENT '0-离线 1-在线',
                                 `channel_type` tinyint NOT NULL COMMENT '1-通道 2-设备',
                                 `create_time` datetime DEFAULT NULL,
                                 `update_time` datetime DEFAULT NULL,
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC

CREATE TABLE `rundo_detail` (
                                `id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                `dc_id` bigint NOT NULL COMMENT '设备或者通道的id',
                                `origin_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原始id',
                                `type` tinyint NOT NULL COMMENT '数据类型 1-通道 2-设备',
                                `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                `port` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '端口',
                                `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备名称',
                                `manufacturer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备厂商',
                                `model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '型号',
                                `firmware` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '固件版本',
                                `ptz_type` tinyint DEFAULT NULL COMMENT '云台类型',
                                `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                `create_time` datetime DEFAULT NULL,
                                `update_time` datetime DEFAULT NULL,
                                PRIMARY KEY (`id`) USING BTREE,
                                KEY `idx_dc_id_type` (`dc_id`,`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20066 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC

CREATE TABLE `rundo_device` (
                                `id` bigint unsigned NOT NULL,
                                `gateway_id` bigint NOT NULL COMMENT '网关id',
                                `sign_state` tinyint NOT NULL COMMENT '0- 已注册 1-待注册 2-待添加 ',
                                `device_type` tinyint DEFAULT NULL COMMENT '1-设备 2-NVR 3-DVR 4-CVR',
                                `online_state` tinyint NOT NULL COMMENT '0-离线 1-在线',
                                `create_time` datetime DEFAULT NULL,
                                `update_time` datetime DEFAULT NULL,
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC

CREATE TABLE `rundo_gateway` (
                                 `id` bigint unsigned NOT NULL,
                                 `serial_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网关ID',
                                 `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '网关名称',
                                 `sign_type` tinyint NOT NULL COMMENT '1-MQ  2-RETFUL',
                                 `online_state` tinyint DEFAULT NULL COMMENT '状态：0-离线 1-在线',
                                 `gateway_type` tinyint NOT NULL COMMENT '网关类型 1-设备 2-NVR 3-DVR 4-CVR',
                                 `protocol` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网关协议',
                                 `ip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网关ip地址',
                                 `port` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '端口',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 UNIQUE KEY `uni_gateway_serial_num` (`serial_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC

