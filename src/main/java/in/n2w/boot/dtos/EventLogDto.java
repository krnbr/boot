package in.n2w.boot.dtos;

import java.time.LocalDateTime;

/**
 * Created by Karanbir Singh on 4/7/2019.
 **/
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
}
