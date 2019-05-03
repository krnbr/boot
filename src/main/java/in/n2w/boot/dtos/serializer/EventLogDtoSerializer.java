package in.n2w.boot.dtos.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import in.n2w.boot.dtos.EventLogDto;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Created by Karanbir Singh on 5/1/2019.
 **/
public class EventLogDtoSerializer extends JsonSerializer<EventLogDto> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void serialize(EventLogDto value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("log", value.getLog());
        gen.writeStringField("time", value.getDateTime().format(formatter));
        gen.writeEndObject();
    }
}
