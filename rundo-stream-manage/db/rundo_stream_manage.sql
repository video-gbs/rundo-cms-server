CREATE TABLE `rundo_dispatch` (
                                  `id` bigint NOT NULL,
                                  `serial_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                  `online_state` tinyint(1) NOT NULL COMMENT '0-离线 1-在线',
                                  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                  `port` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                  `create_time` datetime DEFAULT NULL,
                                  `update_time` datetime DEFAULT NULL,
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE KEY `uni_dispatch_serial_num` (`serial_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC

CREATE TABLE `rundo_gateway_dispatch` (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `gateway_id` bigint NOT NULL,
                                          `dispatch_id` bigint NOT NULL,
                                          `update_time` datetime DEFAULT NULL,
                                          `create_time` datetime DEFAULT NULL,
                                          PRIMARY KEY (`id`) USING BTREE,
                                          UNIQUE KEY `uni_gateway_id` (`gateway_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC

CREATE TABLE `rundo_media` (
                               `id` bigint NOT NULL,
                               `dispatch_id` bigint NOT NULL,
                               `origin_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                               `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                               `port` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                               `type` tinyint NOT NULL COMMENT '1-zlm',
                               `update_time` datetime DEFAULT NULL,
                               `create_time` datetime DEFAULT NULL,
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC

CREATE TABLE `rundo_stream` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `gateway_id` bigint NOT NULL COMMENT '网关id',
                                `channel_id` bigint NOT NULL COMMENT '通道id',
                                `dispatch_id` bigint NOT NULL COMMENT '调度中心id',
                                `stream_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流id LIVE-直播 RECORD-录播 DOWNLOAD-下载',
                                `play_type` tinyint(1) NOT NULL COMMENT '1-直播 2-录播 3-下载',
                                `record_state` tinyint(1) NOT NULL COMMENT '0-关闭 1-开启',
                                `auto_close_state` tinyint(1) NOT NULL COMMENT '0-关闭 1-开启 是否触发无人观看',
                                `stream_state` tinyint(1) NOT NULL COMMENT '0-准备中 1-播放中',
                                `create_time` datetime DEFAULT NULL,
                                `update_time` datetime DEFAULT NULL,
                                PRIMARY KEY (`id`) USING BTREE,
                                UNIQUE KEY `uni_stream_id` (`stream_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=177 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC

