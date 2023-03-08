CREATE TABLE `rundo_channel` (
                                 `id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                 `device_id` bigint NOT NULL,
                                 `origin_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网关上报原始通道id',
                                 `update_time` datetime DEFAULT NULL,
                                 `create_time` datetime DEFAULT NULL,
                                 PRIMARY KEY (`id`) USING BTREE,
                                 UNIQUE KEY `uni_device_origin_id` (`device_id`,`origin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC

CREATE TABLE `rundo_device` (
                                `id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                `gateway_id` bigint NOT NULL COMMENT '网关id',
                                `origin_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '映射设备id',
                                `update_time` datetime DEFAULT NULL,
                                `create_time` datetime DEFAULT NULL,
                                PRIMARY KEY (`id`) USING BTREE,
                                UNIQUE KEY `uni_gateway_device_id` (`gateway_id`,`origin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19970 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC

CREATE TABLE `rundo_dispatch` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `serial_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                  `sign_type` tinyint(1) DEFAULT NULL,
                                  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                  `port` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                  `create_time` datetime DEFAULT NULL,
                                  `update_time` datetime DEFAULT NULL,
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE KEY `uni_dispatch_serial_num` (`serial_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC

CREATE TABLE `rundo_gateway` (
                                 `id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                 `serial_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网关ID',
                                 `sign_type` tinyint NOT NULL COMMENT '1-MQ  2-RETFUL',
                                 `gateway_type` tinyint NOT NULL COMMENT '网关类型 1-设备 2-NVR 3-DVR 4-CVR 5-XVR',
                                 `protocol` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网关协议',
                                 `ip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '网关ip地址',
                                 `port` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '端口',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 UNIQUE KEY `uni_gateway_serial_num` (`serial_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC

CREATE TABLE `rundo_gateway_task` (
                                      `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '任务id',
                                      `gateway_id` bigint unsigned DEFAULT NULL COMMENT '网关id',
                                      `device_id` bigint unsigned DEFAULT NULL COMMENT '设备id',
                                      `channel_id` bigint DEFAULT NULL COMMENT '通道id',
                                      `mq_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息队列id',
                                      `msg_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息类型',
                                      `state` tinyint NOT NULL COMMENT '0->执行中 1->已完成 -1->异常',
                                      `detail` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '详情',
                                      `create_time` datetime DEFAULT NULL,
                                      `update_time` datetime DEFAULT NULL,
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2416 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC

CREATE TABLE `rundo_stream_task` (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `dispatch_id` bigint NOT NULL,
                                     `channel_id` bigint DEFAULT NULL,
                                     `stream_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                     `msg_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                     `state` tinyint(1) NOT NULL COMMENT '0->执行中 1->已完成 -1->异常',
                                     `detail` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '详情',
                                     `mq_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                     `create_time` datetime DEFAULT NULL,
                                     `update_time` datetime DEFAULT NULL,
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2659 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC

