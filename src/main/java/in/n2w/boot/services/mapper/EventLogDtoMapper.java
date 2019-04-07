package in.n2w.boot.services.mapper;

import in.n2w.boot.dtos.EventLogDto;
import in.n2w.boot.entities.EventLog;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * Created by Karanbir Singh on 4/7/2019.
 **/
@Service
public class EventLogDtoMapper {

    private Function<EventLog, EventLogDto> getDtoFromEntity = e -> new EventLogDto(e.getId(),e.getLog(), e.getDateTime());

    private Function<EventLogDto, EventLog> getEntityFromDto = d -> {
        EventLog e = new EventLog();
        e.setId(d.getId());
        e.setDateTime(d.getDateTime()!=null?d.getDateTime():LocalDateTime.now());
        e.setLog(d.getLog());
        return e;
    };

    public EventLogDto getDtoFromEntity(EventLog e){
        return getDtoFromEntity.apply(e);
    }

    public EventLog getEntityFromDto(EventLogDto d){
        return getEntityFromDto.apply(d);
    }

}
