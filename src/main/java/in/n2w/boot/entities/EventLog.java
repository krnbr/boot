package in.n2w.boot.entities;

import org.springframework.data.mapping.PersistentEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Karanbir Singh on 4/7/2019.
 **/
@Entity
@Table(name = "event_log")
public class EventLog {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "log_details", length = 4096)
    private String log;

    @Column(name = "created_date")
    private LocalDateTime dateTime;

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
