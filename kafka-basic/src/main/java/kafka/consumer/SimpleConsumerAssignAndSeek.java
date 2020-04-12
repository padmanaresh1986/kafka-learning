package kafka.consumer;

import kafka.config.AppConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleConsumerAssignAndSeek {
    public static void main(String[] args) {
        /**
         * Assign and seek is a mechanism for getting data from specific partition and from specific offset
         * this is mostly used for replying data for any specific case
         */
        Properties consumerConfigs = AppConfig.getConsumerConfigs();
        // no need to have group id for assign and seek
        consumerConfigs.remove(ConsumerConfig.GROUP_ID_CONFIG);
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(consumerConfigs);
        // assign any topic partition to read data from
        TopicPartition partition = new TopicPartition("first_topic",0);
        consumer.assign(Arrays.asList(partition));
        // seek data from specific offset
        consumer.seek(partition,15);
        /**
         * This example get the five messages form first_topic partition 0 starting from offset 15
         */
        AtomicInteger numberOfMsgsToRead = new AtomicInteger(5);
        AtomicInteger numOfMsgsReadSofar = new AtomicInteger(0);
        AtomicBoolean stillReading = new AtomicBoolean(true);
        while(stillReading.get()){
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record);
                numOfMsgsReadSofar.incrementAndGet();
                if(numOfMsgsReadSofar.get() > numberOfMsgsToRead.get()){
                    stillReading.set(false);
                    break;
                }
            }

        }

    }
}
