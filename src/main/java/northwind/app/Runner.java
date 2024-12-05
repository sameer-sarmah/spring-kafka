package northwind.app;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import northwind.config.KafkaConfig;
import northwind.event.publisher.OrderPublisher;
import northwind.event.subscriber.DlqSubscriber;

@EnableAutoConfiguration
@Import(KafkaConfig.class)
@SpringBootApplication
@Component
public class Runner implements ApplicationRunner {
	
	@Autowired
	private OrderPublisher orderPublisher;
	
	
	@Autowired
	private DlqSubscriber dlqSubscriber;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(Runner.class);
	}

	@Override
	public void run(ApplicationArguments args)  {
		dlqSubscriber.processError();
//		ObjectMapper objectMapper = new ObjectMapper();
//		InputStream in = Runner.class.getClassLoader().getResourceAsStream("order.json");
//		String orderJson;
//		try {
//			orderJson = IOUtils.toString(in,Charset.defaultCharset());
//			Order order = objectMapper.readValue(orderJson, Order.class);
//			orderPublisher.publishOrder(order);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		
	}



}
