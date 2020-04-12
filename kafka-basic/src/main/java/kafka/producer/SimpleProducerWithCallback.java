package kafka.producer;

import kafka.config.AppConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;
import java.util.stream.IntStream;

public class SimpleProducerWithCallback {

   private static final Logger logger = LoggerFactory.getLogger(SimpleProducerWithCallback.class);
    public static void main(String[] args) {
        //create producer configuration properties
        Properties producerProps = AppConfig.geProducerConfigs();
        // create producer
        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(producerProps);
        // create producer records and send
        IntStream.range(0,10).forEach(index -> {
        ProducerRecord<String,String> producerRecord =new ProducerRecord<String, String>("first_topic", "hello world "+index);
        // send asynchronus and call back will have meta data about inserted record
        producer.send(producerRecord, (recordMetadata, e) -> {
            //no exception
            if(Objects.isNull(e)){
                System.out.println("inserted record meta data " + recordMetadata);
            }else{
                logger.error("error occurred for "+recordMetadata,e);
            }
        });
        });
        //flush
        producer.flush();
        //flush and close
        producer.close();
    }
}
