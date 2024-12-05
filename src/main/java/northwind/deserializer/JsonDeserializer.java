package northwind.deserializer;

import java.io.IOException;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonDeserializer implements Deserializer<String>{

	private static Logger logger = LoggerFactory.getLogger(JsonDeserializer.class);
	
	@Override
	public String deserialize(String topic, byte[] data) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(data, String.class);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
