package in.n2w.boot.messaging.listener;

import in.n2w.boot.dtos.EventLogDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Karanbir Singh on 5/1/2019.
 **/
@Component
@Lazy
public class BootQueueListener {

    private Logger logger = LoggerFactory.getLogger(BootQueueListener.class);

    // disabled it temporally
    /*@SqsListener("${sqs.default.queue.name}")*/
    public void receiveMessage(EventLogDto message, @Headers Map<String, String> headers) {
        logger.info("Received message: {}, having headers: {}", message, headers);
    }

}
