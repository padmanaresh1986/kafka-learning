package kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import kafka.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

public class SimpleConsumerWithThreads {
    /**
     * This program demo for running kafka consumer in separate threads
     */
    private static  Logger logger = LoggerFactory.getLogger(SimpleConsumerWithThreads.class);
    public static void main(String[] args) {
          new ConsumerWithThreads().runProcess();
    }
}


class ConsumerWithThreads{
    private static  Logger logger = LoggerFactory.getLogger(ConsumerWithThreads.class);

    void runProcess(){
        Properties consumerConfigs = AppConfig.getConsumerConfigs();
        String topic_name = "first_topic";
        //latch for dealing with multiple threads
        CountDownLatch latch = new CountDownLatch(1);
        // create new thread and start
        ConsumerRunnable consumerRunnable = new ConsumerRunnable(latch,consumerConfigs,topic_name);
        // below will start new thread in fork join pool
         CompletableFuture.runAsync(consumerRunnable);
       // Thread myThread = new Thread(consumerRunnable);
       // myThread.start();

        logger.info("started async process ...");

        //add shutdown hook for graceful termination of consumer
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            logger.info("caught shutdown hook..");
            consumerRunnable.shutdown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.info("Application shutting down..");
            }
        }));

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error("Application got inturrupted ..");
        }finally{
            logger.info("Application is closing..");
        }
    }

}


 class ConsumerRunnable implements Runnable{
    private Logger logger = LoggerFactory.getLogger(ConsumerRunnable.class);
    private CountDownLatch latch;
    Properties consumerConfigs;
    KafkaConsumer<String,String> consumer;

    public ConsumerRunnable(CountDownLatch latch,Properties consumerConfigs, String topic_name){
        this.latch = latch;
        this.consumerConfigs = consumerConfigs;
        // create consumer and subscribe consumer, but polling in run method
        consumer = new KafkaConsumer<String, String>(consumerConfigs);
        consumer.subscribe(Pattern.compile(topic_name));
        logger.info("Consumer is subscribed...");
    }

    @Override
    public void run() {
        try {
            logger.info("started polling data from consumer..");
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                records.forEach(System.out::println);
            }
        }catch(WakeupException exception){
            logger.info("Polling stopped...received shutdown signal...");
        }finally{
            //super important
            logger.info("Stopping consumer ...");
            consumer.close();
            //inform main thread to exit the process
            logger.info("Counting down latch ...");
            latch.countDown();
            logger.info("current latch count is .... " + latch.getCount());

        }

    }

    public void shutdown(){
        // method to inturrupt polling and raise wake up exception
        consumer.wakeup();
    }
}
