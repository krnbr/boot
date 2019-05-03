package in.n2w.boot.dtos.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import in.n2w.boot.dtos.EventLogDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Karanbir Singh on 5/2/2019.
 **/
public class EventLogDtoDeSerializer extends JsonDeserializer<EventLogDto> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public EventLogDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = p.getCodec();
        JsonNode node = oc.readTree(p);
        LocalDateTime dateTime = LocalDateTime.parse(node.get("time").asText(), formatter);
        return new EventLogDto(node.get("id").asLong(), node.get("log").asText(), dateTime);
    }
}
