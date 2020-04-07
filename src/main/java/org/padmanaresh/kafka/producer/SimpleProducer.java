package org.padmanaresh.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.padmanaresh.kafka.config.ProducerConfiguration;

import java.util.Properties;

public class SimpleProducer {
    public static void main(String[] args) {
        //create producer configuration properties
        Properties producerProps = ProducerConfiguration.geProducerConfigs();
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
