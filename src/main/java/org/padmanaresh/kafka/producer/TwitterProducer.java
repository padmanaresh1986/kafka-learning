package org.padmanaresh.kafka.producer;

import com.twitter.hbc.core.Client;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.padmanaresh.kafka.config.AppConfig;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterProducer {
    static Logger logger = LoggerFactory.getLogger(TwitterProducer.class);
    public static void main(String[] args) {
        //create twitter client
        /** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(1000);
        Client hosebirdClient = AppConfig.getTwitterClient(msgQueue, Arrays.asList("COVID-19"));

        //create kafka producer
        Properties producerProps = AppConfig.geProducerConfigs();
        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(producerProps);

       // connect to twitter
        hosebirdClient.connect();

        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            logger.info("stopping application");
            logger.info("shutting down twitter client..");
            hosebirdClient.stop();
            logger.info("closing kafka producer..");
            producer.close();
            logger.info("D O N E");
        }));

        // on a different thread, or multiple different threads....
        while (!hosebirdClient.isDone()) {
            String msg = null;
            try {
                msg = msgQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                hosebirdClient.stop();
            }
            if(Objects.nonNull(msg)){
                /*
                  create twitter_tweets topic in kafka before running this code using below command
                  kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic twitter_tweets --create --partitions 3 --replication-factor 1
                  kafka-topics.sh --zookeeper 127.0.0.1:2181 --list
                 */
                ProducerRecord<String,String> producerRecord =new ProducerRecord<String, String>("twitter_tweets", null,msg);
                //send tweets to producer
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
                    });
            }
        }
    }
}

