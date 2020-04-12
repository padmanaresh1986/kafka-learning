package kafka.producer;

import kafka.config.AppConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class SimpleProducer {
    public static void main(String[] args) {
        //create producer configuration properties
        Properties producerProps = AppConfig.geProducerConfigs();
        // create producer
        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(producerProps);
        // create producer record
        ProducerRecord<String,String> producerRecord =new ProducerRecord<String, String>("first_topic", "hello world");
        // send asynchronus
        producer.send(producerRecord);
        //flush
        producer.flush();
        //flush and close
        producer.close();
    }
}
