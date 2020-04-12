package kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import kafka.config.AppConfig;

import java.time.Duration;
import java.util.Properties;
import java.util.regex.Pattern;

public class SimpleConsumer {
    public static void main(String[] args) {
        Properties consumerConfigs = AppConfig.getConsumerConfigs();
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(consumerConfigs);
        consumer.subscribe(Pattern.compile("first_topic"));
        while(true){
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));
            records.forEach(System.out::println);
        }

    }
}
