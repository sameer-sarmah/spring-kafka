package northwind.app;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import northwind.config.KafkaConfig;
import northwind.models.Order;

@Import(KafkaConfig.class)
@SpringBootApplication
@Component
public class Runner implements ApplicationRunner {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(Runner.class);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		InputStream in = Runner.class.getClassLoader().getResourceAsStream("order.json");
		String orderJson = IOUtils.toString(in,Charset.defaultCharset());
		Order order = objectMapper.readValue(orderJson, Order.class);
		System.out.println(order);
		
	}



}
