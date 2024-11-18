package northwind.event.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import northwind.models.Order;

@Component
public class OrderPublisher {
    @Value(value = "${kafka.order.topic}")
    private String orderTopic;

    @Autowired
    private KafkaTemplate<Long, Object> kafkaTemplate;
    
    private static Logger logger = LoggerFactory.getLogger(OrderPublisher.class);
    
    public void publishOrder(Order order) {
        kafkaTemplate.send(orderTopic, order);
        kafkaTemplate.flush();
        logger.info("Order published");
    }
}
