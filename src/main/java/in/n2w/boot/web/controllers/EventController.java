package in.n2w.boot.web.controllers;

import in.n2w.boot.dtos.EventLogDto;
import in.n2w.boot.services.EventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public String test(){
        return "OK";
    }

}
