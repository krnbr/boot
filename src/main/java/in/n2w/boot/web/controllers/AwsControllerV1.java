package in.n2w.boot.web.controllers;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import in.n2w.boot.cloud.sqs.DataTypes;
import in.n2w.boot.dtos.EventLogDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Karanbir Singh on 4/30/2019.
 **/
@RestController
@RequestMapping("/v1/aws/")
public class AwsControllerV1 {

    @Autowired
    private AmazonSQSAsync amazonSQSAsync;

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Value("${aws.sqs.boot.url}")
    private String boot_queue;

    @Value("${sqs.default.queue.name}")
    private String boot_queue_name;

    private Logger logger = LoggerFactory.getLogger(AwsControllerV1.class);

    // calls the aws sqs easily for strings and numbers etc.
    @GetMapping("test")
    public boolean test(@RequestParam final String message){
        SendMessageRequest sendMessageRequest = new SendMessageRequest();
        sendMessageRequest.setQueueUrl(boot_queue);
        sendMessageRequest.setMessageBody(message);

        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        attributes.put(
                "CREATED_TIMESTAMP",
                new MessageAttributeValue()
                    .withDataType(DataTypes.STRING.getDataType())
                        .withStringValue(String.valueOf(Calendar.getInstance().getTimeInMillis()))
        );

        MessageAttributeValue status = new MessageAttributeValue();
        status.setDataType(DataTypes.NUMBER.getDataType());
        status.setStringValue("1");

        attributes.put("status", status);

        sendMessageRequest.setMessageAttributes(attributes);
        sendMessageRequest.setMessageBody("test");

        sendMessageRequest.setDelaySeconds(2);

        SendMessageResult result = amazonSQSAsync.sendMessage(sendMessageRequest);
        logger.info("created message Id -> {} ",result);
        return result.getMessageId()!=null;
    }

    @GetMapping("event")
    public boolean event(@RequestParam String log){
        EventLogDto eventLogDto = new EventLogDto(new Random().nextLong(), log, LocalDateTime.now());

        queueMessagingTemplate.convertAndSend(boot_queue_name, eventLogDto);
        return true;
    }

}
