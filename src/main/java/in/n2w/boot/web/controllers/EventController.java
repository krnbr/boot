package in.n2w.boot.web.controllers;

import in.n2w.boot.dtos.EventLogDto;
import in.n2w.boot.services.EventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Karanbir Singh on 4/7/2019.
 **/
@RestController
@RequestMapping("/v1/events")
public class EventController {

    @Autowired
    private EventLogService eventLogService;

    @PostMapping
    public EventLogDto saveEvent(@RequestBody EventLogDto dto){
        return eventLogService.saveEventLog(dto);
    }

}
