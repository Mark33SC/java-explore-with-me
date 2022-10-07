package ru.practicum.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonComponent
public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    public LocalDateTimeSerializer(Class<LocalDateTime> type) {
        super(type);
    }

    public LocalDateTimeSerializer() {
        this(null);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        generator.writeString(formatter.format(value));
    }
}
