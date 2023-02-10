package com.runjian.auth.server.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    /**
     * DateTime格式化字符串 yyyy-MM-dd HH:mm:ss
     */
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public MyLocalDateTimeSerializer() {
        super(LocalDateTime.class);

    }

    @Override
    public void serialize(final LocalDateTime value,
                          final JsonGenerator generator,
                          final SerializerProvider provider) throws IOException {
        if (value != null) {
            generator.writeString(value.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)));
        } else {
            generator.writeNull();
        }
    }

}