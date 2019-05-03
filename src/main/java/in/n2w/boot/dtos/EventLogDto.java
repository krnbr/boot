package in.n2w.boot.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import in.n2w.boot.dtos.deserializer.EventLogDtoDeSerializer;
import in.n2w.boot.dtos.serializer.EventLogDtoSerializer;

import java.time.LocalDateTime;

/**
 * Created by Karanbir Singh on 4/7/2019.
 **/
@JsonSerialize(using = EventLogDtoSerializer.class)
@JsonDeserialize(using = EventLogDtoDeSerializer.class)
public class EventLogDto {

    private Long id;

    private String log;

    private LocalDateTime dateTime;

    public EventLogDto(Long id, String log, LocalDateTime dateTime) {
        this.id = id;
        this.log = log;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "EventLogDto{" +
                "id=" + id +
                ", log='" + log + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
