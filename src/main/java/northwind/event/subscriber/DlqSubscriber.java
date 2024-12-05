package northwind.event.subscriber;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DlqSubscriber {
    private static Logger logger = LoggerFactory.getLogger(DlqSubscriber.class);
    
    @Autowired
    private KafkaTemplate<Long, Object> kafkaTemplate;
    
    @Autowired
    @Qualifier("dlqConsumerFactory")  
    private ConsumerFactory<Long, Object> consumerFactory;
    
    @Value("${deadletterqueue.topic}")
    private String dlqTopic ;
    
    public void processError() {
    	kafkaTemplate.setConsumerFactory(consumerFactory);
    	ConsumerRecord record = kafkaTemplate.receive(dlqTopic, 0, 1);
    	Headers headers = record.headers();
    	headers.forEach(header -> {
    		logger.info(header.key()+"=>"+ new String (header.value()));
    	});

    }
}
