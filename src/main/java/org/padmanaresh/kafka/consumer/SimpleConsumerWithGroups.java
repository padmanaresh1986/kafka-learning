package org.padmanaresh.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.padmanaresh.kafka.config.AppConfig;

import java.time.Duration;
import java.util.Properties;
import java.util.regex.Pattern;

public class SimpleConsumerWithGroups {
    public static void main(String[] args) {
        Properties consumerConfigs = AppConfig.getConsumerConfigs();
        /**
         * since we set the property
         * properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "my-application");
         * multiple instances of this consumer work as group
         *
         * run 3 instances of this consumer and you can notice each instance is reading form 1 partition
         * verify logs and see which consumer is reading from which partition
         *
         */

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(consumerConfigs);
        consumer.subscribe(Pattern.compile("first_topic"));
        while(true){
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));
            records.forEach(System.out::println);
        }

    }
}
