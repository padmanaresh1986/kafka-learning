package org.padmanaresh.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class AppConfig {
// Below link is the documentation for producer configs
// https://kafka.apache.org/documentation.html#producerconfigs

    private static final String bootstrapServers = "127.0.0.1:9092";
    public static Properties geProducerConfigs(){
        Properties properties = new Properties();
        // basic properties
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());



        return properties;
    }
}
