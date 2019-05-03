package in.n2w.boot.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Karanbir Singh on 4/30/2019.
 **/
@Configuration
public class AwsConfig {

    @Value("${aws.access.key.id}")
    private String awsAccessKeyId;

    @Value("${aws.access.key.secret}")
    private String awsAccessKeySecret;

    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public BasicAWSCredentials awsCredentials(){
          return new BasicAWSCredentials(this.awsAccessKeyId, this.awsAccessKeySecret);
    }

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider(AWSCredentials awsCredentials){
        return new AWSStaticCredentialsProvider(awsCredentials);
    }

    @Bean
    public AmazonSQSAsync amazonSQS(AWSCredentialsProvider awsCredentialsProvider){


        AmazonSQSAsync amazonSQS = AmazonSQSAsyncClient.asyncBuilder().withRegion(awsRegion).withCredentials(awsCredentialsProvider).build();
        // or like the below also
        // AmazonSQSAsync amazonSQS = AmazonSQSAsyncClient.asyncBuilder().withRegion(Regions.AP_SOUTH_1).withCredentials(awsCredentialsProvider).build();
        return amazonSQS;
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync){
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Bean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSQSAsync) {
        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
        factory.setMaxNumberOfMessages(10);
        factory.setWaitTimeOut(5);
        factory.setAmazonSqs(amazonSQSAsync);
        return factory;
    }

    @Bean
    public QueueMessageHandler queueMessageHandler(AmazonSQSAsync amazonSQS) {
        QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
        factory.setAmazonSqs(amazonSQS);
        return factory.createQueueMessageHandler();
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory, QueueMessageHandler queueMessageHandler) {
        SimpleMessageListenerContainer container = simpleMessageListenerContainerFactory.createSimpleMessageListenerContainer();
        container.setMessageHandler(queueMessageHandler);
        container.setWaitTimeOut(20);
        return container;
    }

    /*@Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory(AmazonSQSAsync amazonSQSAsync) {
        QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
        factory.setAmazonSqs(amazonSQSAsync);
        return factory;
    }*/

}
