package org.padmanaresh.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.padmanaresh.kafka.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

public class SimpleProducerWithMsgKey {

   private static final Logger logger = LoggerFactory.getLogger(SimpleProducerWithMsgKey.class);
   /*
     we can send key to the message in the producer record.
     msg with same key always goes to same partition in the topic
     this helps ordering of same key messages in the topics
     messages are only ordered in the partition , not on topic.

     run this example multiple times and see in the log
     always same key goes to same partition

    */

    public static void main(String[] args) {
        //create producer configuration properties
        Properties producerProps = AppConfig.geProducerConfigs();
        // create producer
        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(producerProps);
        // create producer records and send
        IntStream.range(0,10).forEach(index -> {
            String key = "id_"+index;
            String value = "hello world "+index;
            logger.info("sending with message key " +key);
            logger.info("sending with message value " +value);
        ProducerRecord<String,String> producerRecord =new ProducerRecord<String, String>("first_topic", key,value);
        // send asynchronous and call back will have meta data about inserted record
            try {
                producer.send(producerRecord, (recordMetadata, e) -> {
                    //no exception
                    if(Objects.isNull(e)){
                        logger.info("Received metadata. \n"+
                                "Topic :"+recordMetadata.topic()+"\n"+
                                "Partition :"+recordMetadata.partition()+"\n"+
                                "Offset :"+recordMetadata.offset()+"\n"+
                                "Timestamp :"+recordMetadata.timestamp());
                    }else{
                        logger.error("error occurred for "+recordMetadata,e);
                    }
                }).get();   // this changes from asynchronous to synchronous, don't do this in production
            } catch (InterruptedException | ExecutionException  e) {
                logger.error("error occurred",e);
            }
        });
        //flush
        producer.flush();
        //flush and close
        producer.close();
    }
}
