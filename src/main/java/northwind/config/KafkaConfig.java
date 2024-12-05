package northwind.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import northwind.deserializer.JsonDeserializer;
import northwind.deserializer.KafkaOrderDeserializer;
import northwind.serializer.KafkaJsonSerializer;

@EnableKafka
@PropertySource("kafka.properties")
@Configuration
@ComponentScan(basePackages = {"northwind"})
public class KafkaConfig {
    
    @Value(value = "${kafka.bootstrap-server}")
    private String bootstrapAddress;

    @Value(value = "${kafka.order.consumer.group}")
    private String orderConsumerGroup;
	
    @Bean
    public ProducerFactory<Long, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Long, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    public Map<String, Object> commonKafkaConsumerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "100000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 100000);
        props.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 100000);
        return Collections.unmodifiableMap(props);
    }

    @Bean
    public ConsumerFactory<Long, Object> orderConsumerFactory() {
        Map<String, Object> clonedMap = new HashMap<>(commonKafkaConsumerConfig());
        clonedMap.put(ConsumerConfig.GROUP_ID_CONFIG, orderConsumerGroup);
        clonedMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        clonedMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaOrderDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(clonedMap);
    }

    @Bean
    @DependsOn({"orderConsumerFactory"})
    public ConcurrentKafkaListenerContainerFactory<Long, Object> kafkaEventListenerContainerFactory(@Qualifier("orderConsumerFactory")  ConsumerFactory<Long, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Long, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(1);
        return factory;
    }

    @Bean
    public ConsumerFactory<Long, Object> dlqConsumerFactory() {
        Map<String, Object> clonedMap = new HashMap<>(commonKafkaConsumerConfig());
        clonedMap.put(ConsumerConfig.GROUP_ID_CONFIG, orderConsumerGroup);
        clonedMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        clonedMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        clonedMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(clonedMap);
    }

    @Bean
    @DependsOn({"dlqConsumerFactory"})
    public ConcurrentKafkaListenerContainerFactory<Long, Object> dlqKafkaEventListenerContainerFactory(@Qualifier("dlqConsumerFactory")  ConsumerFactory<Long, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Long, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(1);
        return factory;
    }

}
