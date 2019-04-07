package in.n2w.boot.services;

import in.n2w.boot.dtos.EventLogDto;
import in.n2w.boot.repositories.EventLogRepository;
import in.n2w.boot.services.mapper.EventLogDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Karanbir Singh on 4/7/2019.
 **/
@Service
public class EventLogService {

    @Autowired
    private EventLogRepository eventLogRepository;

    @Autowired
    private EventLogDtoMapper eventLogDtoMapper;

    public EventLogDto saveEventLog(EventLogDto eventLogDto){
        return eventLogDtoMapper.getDtoFromEntity(
            eventLogRepository.save(eventLogDtoMapper.getEntityFromDto(eventLogDto))
        );
    }

}
