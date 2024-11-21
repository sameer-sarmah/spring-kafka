package northwind.event.publisher;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.KafkaContainer;

import com.fasterxml.jackson.databind.ObjectMapper;

import northwind.app.Runner;
import northwind.models.Order;

@ExtendWith(SpringExtension.class)
@Import(KafkaTestConfig.class)
@SpringBootTest(classes = {KafkaTestConfig.class,Runner.class})
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class OrderPublisherEmbeddedKafkaTest {
	
    private static KafkaContainer kafka;

    @Autowired
    private KafkaTemplate kafkaTemplate;
    
	@Autowired
	private OrderPublisher orderPublisher;

    
    @Test
    public void publishOrderTest(){
		ObjectMapper objectMapper = new ObjectMapper();
		InputStream in = Runner.class.getClassLoader().getResourceAsStream("order.json");
		String orderJson;
		try {
			orderJson = IOUtils.toString(in,Charset.defaultCharset());
			Order order = objectMapper.readValue(orderJson, Order.class);
			orderPublisher.publishOrder(order);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
