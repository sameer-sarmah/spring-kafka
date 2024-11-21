package northwind.event.publisher;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;

import northwind.app.Runner;
import northwind.models.Order;

@ExtendWith(SpringExtension.class)
@Import(KafkaTestConfig.class)
@SpringBootTest(classes = {KafkaTestConfig.class,Runner.class})
public class OrderPublisherTestContainer {
	
    private static KafkaContainer kafka;

    @Autowired
    private KafkaTemplate kafkaTemplate;
    
	@Autowired
	private OrderPublisher orderPublisher;


    @BeforeAll
    public static void beforeAll() {
        kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));
        kafka.start();
        String bootstrapServers = kafka.getBootstrapServers();
        System.out.println("Bootstrap servers " + bootstrapServers);
        System.setProperty("kafka.bootstrap-server", bootstrapServers);
    }
    
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
