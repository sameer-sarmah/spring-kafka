package northwind.serializer;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class KafkaJsonSerializer implements Serializer {
	
	private static Logger logger = LoggerFactory.getLogger(KafkaJsonSerializer.class);
	
    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Object data) {
        byte[] serializedBytes = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            serializedBytes = objectMapper.writeValueAsString(data).getBytes();
        } catch (Exception e) {
        	logger.error(e.getMessage());
        }
        return serializedBytes;
    }

    @Override
    public void close() {

    }
}
