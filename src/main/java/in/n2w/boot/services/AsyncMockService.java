package in.n2w.boot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created by Karanbir Singh on 6/16/2019.
 **/
@Service
public class AsyncMockService {

    private Logger logger = LoggerFactory.getLogger(AsyncMockService.class);

    @Async
    public void someMock(String message, Long wait){
        Long start = System.currentTimeMillis();
        logger.info(message+" started at "+start);

        if(wait <= 0){
            wait = 1000L;
        }else if(wait > 15000){
            wait = 15000L;
        }

        Long end = start + wait;

        while (System.currentTimeMillis() < end){
            // wait
        }

        // MODE_INHERITABLETHREADLOCAL was the concern
        // evaluate the following
        // org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()

        logger.info(message+" finished within "+(end-start));
    }

}
