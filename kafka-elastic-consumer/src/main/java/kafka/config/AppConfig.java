package kafka.config;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

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
        // safe producer
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,"true");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG,Integer.toString(Integer.MAX_VALUE));
        properties.setProperty(ProducerConfig.ACKS_CONFIG,"all");
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,"5");
        // high throughput producer at an expense of little latency and cpu utilization
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG,"snappy"); //gzip,snappy ...
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG,"20");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG,Integer.toString(32*1024)); //32 kb

        return properties;
    }


    public static Properties getConsumerConfigs(){
        Properties properties = new Properties();
        // basic properties
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //assigning group to consumers to work as group
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "my-application");
        // starts reading from begining for new consumer group id
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        //disabling auto commit, manually commit after processing
       // properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        //polling one record in batch
       // properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        return properties;
    }

    public static RestHighLevelClient getElasticSearchClient(){
        //https://i9uss:emnsf@kafka-course-5699090762.ap-east-2.bonsaisearch.net:443
        /*String userName = "XXX";
        String password = "XXX";
        String hostName = "xXX";*/
        String userName = "i9ussbztid";
        String password = "emnsfdosmk";
        String hostName = "kafka-course-5699090762.ap-southeast-2.bonsaisearch.net";

        // don't do below if you are running local ES
        final CredentialsProvider credintialProvider = new BasicCredentialsProvider();
        credintialProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(userName,password));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(hostName,443,"https"))
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credintialProvider));
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

}
