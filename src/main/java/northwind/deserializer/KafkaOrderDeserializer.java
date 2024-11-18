package northwind.deserializer;

import java.io.IOException;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import northwind.models.Order;

@Component
public class KafkaOrderDeserializer implements Deserializer<Order> {

	private static Logger logger = LoggerFactory.getLogger(KafkaOrderDeserializer.class);
	
	@Override
	public Order deserialize(String topic, byte[] data) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(data, Order.class);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

}
