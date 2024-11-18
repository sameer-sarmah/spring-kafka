package northwind.event.subscriber;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import northwind.models.Order;


@Component
public class OrderEventSubscriber {

    private static Logger logger = LoggerFactory.getLogger(OrderEventSubscriber.class);

    @KafkaListener(topics = "#{'${kafka.order.topic}'}",
            groupId = "#{'${kafka.order.consumer.group}'}" , containerFactory = "kafkaEventListenerContainerFactory")
    public void handle(@Payload ConsumerRecord message, Consumer<Long,Object> consumer){
        if(message.value() != null && message.value() instanceof Order){
        	 Order order = (Order) message.value() ;
        	 logger.info("Order received "+order);
        }

    }
}
