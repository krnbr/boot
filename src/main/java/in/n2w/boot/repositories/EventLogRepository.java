package in.n2w.boot.repositories;

import in.n2w.boot.entities.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Karanbir Singh on 4/7/2019.
 **/
public interface EventLogRepository extends JpaRepository<EventLog, Long> {



}
