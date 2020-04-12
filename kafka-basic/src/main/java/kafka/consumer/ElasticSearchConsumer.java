package kafka.consumer;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import kafka.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchConsumer {
    static Logger logger = LoggerFactory.getLogger(ElasticSearchConsumer.class);
    public static void main(String ...a) throws Exception{
        RestHighLevelClient elasticSearchClient = AppConfig.getElasticSearchClient();

        String jsonString = "{ \"foo\" : \"bar\"}";
        // if index twitter does not exists , then it will throw error , so create twitter index in elastic first
        IndexRequest indexRequest = new IndexRequest("twitter","tweets")
                .source(jsonString, XContentType.JSON);

        IndexResponse indexResponse = elasticSearchClient.index(indexRequest, RequestOptions.DEFAULT);
        String id = indexResponse.getId();
        logger.info("Document created with id  "+  id);

        //close client gracefully
        elasticSearchClient.close();
    }


}
