package kafka.consumer;

import com.google.gson.JsonParser;
import kafka.config.AppConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;
import java.util.regex.Pattern;

public class ElasticSearchIdempotentConsumer {
    static Logger logger = LoggerFactory.getLogger(ElasticSearchIdempotentConsumer.class);

    /**
     * if consumer receives same tweet two times , it should be processed only once. this is called idempotent consumer
     * this can be achieved by assigning id manually instead of relaying on kafka generated Ids.
     */

    private static JsonParser parser = new JsonParser();
    private static String extractIdFromRecord(String tweet) {
        return  parser.parse(tweet).getAsJsonObject().get("id_str").getAsString();
    }

    static KafkaConsumer<String,String> getKafkaConsumer(String topicName){
        Properties consumerConfigs = AppConfig.getConsumerConfigs();
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(consumerConfigs);
        consumer.subscribe(Pattern.compile(topicName));
        return consumer;
    }


    public static void main(String ...a) throws Exception{
        RestHighLevelClient elasticSearchClient = AppConfig.getElasticSearchClient();
        KafkaConsumer<String, String> twitterConsumer = getKafkaConsumer("twitter_tweets");
        while(true){
            ConsumerRecords<String, String> consumerRecords = twitterConsumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                /**
                 *  There are two strategies for generating ids
                 *  1. Kafka generic id
                 *     String id = record.topic() + record.partition() +record.offset();
                 *  2. Business specific id , in this case twitter specific id
                 */
                 String tweet = consumerRecord.value();
                 String tweet_id  = extractIdFromRecord(tweet);
                logger.info("Received tweet from kafka "+tweet);
                // if index twitter does not exists , then it will throw error , so create twitter index in elastic first
                IndexRequest indexRequest = new IndexRequest("twitter","tweets",tweet_id).source(tweet, XContentType.JSON);
                IndexResponse indexResponse = elasticSearchClient.index(indexRequest, RequestOptions.DEFAULT);
                String id = indexResponse.getId();
                logger.info("Document created with id  "+  id);

            }
            //logger.info("committing offsets ..");
            //twitterConsumer.commitAsync();
            //close client gracefully
            //elasticSearchClient.close();//close client gracefully
        }
    }

}
