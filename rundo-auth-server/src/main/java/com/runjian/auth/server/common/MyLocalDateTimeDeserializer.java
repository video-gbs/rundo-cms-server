package com.runjian.auth.server.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class MyLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
    /**
     * DateTime格式化字符串 yyyy-MM-dd HH:mm:ss
     */
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("UTC");
    private static final ZoneId SYSTEM_ZONE_ID = ZoneId.of("Asia/Shanghai");
    private static final String T = "T";
    private static final String Z = "Z";

    public MyLocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(final JsonParser parser,
                                     final DeserializationContext context) throws IOException {
        String value = parser.getValueAsString();
        // 处理标准格式的时间
        if (StringUtils.isNotBlank(value) && value.length() == DATETIME_FORMAT.length()) {
            if (!value.contains(T) || !value.endsWith(Z)) {
                StringBuilder str = new StringBuilder(value);
                str.replace(10, 11, T);
                str.append(Z);
                return LocalDateTime.ofInstant(Instant.parse(str.toString()), DEFAULT_ZONE_ID);
            }
        }
        // 处理UTC格式的时间
        return LocalDateTime.ofInstant(Instant.parse(value), DEFAULT_ZONE_ID)
                .atZone(DEFAULT_ZONE_ID).withZoneSameInstant(SYSTEM_ZONE_ID).toLocalDateTime();
    }
}