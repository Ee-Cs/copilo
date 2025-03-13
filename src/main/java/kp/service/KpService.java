package kp.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kp.Constants;
import kp.model.Box;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The wrapper for the {@link ElasticsearchClient}.
 */
public class KpService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private final ElasticsearchClient client;

    /**
     * Parameterized constructor.
     *
     * @param password the password
     */
    public KpService(String password) {
        this.client = createClient(new UsernamePasswordCredentials(Constants.LOGIN, password));
    }

    /**
     * Creates the {@link ElasticsearchClient}.
     *
     * @param credentials the {@link UsernamePasswordCredentials}
     * @return the {@link ElasticsearchClient}
     */
    private ElasticsearchClient createClient(UsernamePasswordCredentials credentials) {

        final RestClient restClient = RestClient.builder(Constants.HTTPS_HOST)
                .setHttpClientConfigCallback(builder -> modifyHttpClientConfiguration(builder, credentials))
                .build();
        return new ElasticsearchClient(new RestClientTransport(restClient, new JacksonJsonpMapper()));
    }

    /**
     * Modifies the HTTP client configuration.
     *
     * @param httpClientBuilder the {@link HttpAsyncClientBuilder}
     * @param credentials       the {@link UsernamePasswordCredentials}
     * @return httpClientBuilder the {@link HttpAsyncClientBuilder}
     */
    private HttpAsyncClientBuilder modifyHttpClientConfiguration(HttpAsyncClientBuilder httpClientBuilder,
                                                                 UsernamePasswordCredentials credentials) {

        SSLContext sslContext = null;
        try {
            sslContext = TransportUtils.sslContextFromHttpCaCrt(Constants.CERTIFICATE_FILE);
        } catch (IOException e) {
            logger.error("modifyHttpClientConfiguration(): exiting, IOException[{}]", e.getMessage());
            System.exit(1);
        }
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        return httpClientBuilder.setSSLContext(sslContext).setDefaultCredentialsProvider(credentialsProvider);
    }

    /**
     * Recreates the index.
     *
     * @throws IOException the IO exception
     */
    public void recreateIndex() throws IOException {

        try {
            client.indices().delete(builder -> builder.index(Constants.INDEX_NAME));
        } catch (ElasticsearchException | IOException e) {
            logger.error("recreateIndex(): index deleting causes exception[{}]", e.getMessage());
        }
        client.indices().create(builder -> builder.index(Constants.INDEX_NAME));
        logger.info("recreateIndex():");
    }

    /**
     * Adds the boxes from the dataset file.
     *
     * @throws IOException the IO exception
     */
    public void addBoxesFromFile() throws IOException {

        final BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();
        final List<Box> boxList = new ObjectMapper().readValue(Constants.DATASET_FILE, new TypeReference<>() {
        });
        boxList.forEach(box -> bulkRequestBuilder.operations(
                operBuilder -> operBuilder.index(
                        idxBuilder -> idxBuilder.index(Constants.INDEX_NAME).id(box.id()).document(box))));
        final BulkResponse response = client.bulk(bulkRequestBuilder.build());
        if (response.errors()) {
            for (BulkResponseItem item : response.items()) {
                if (item.error() != null) {
                    logger.error("addBoxesFromFile(): item error[{}]", item.error().reason());
                }
            }
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info("addBoxesFromFile(): file[{}], number of items[{}]",
                    Constants.DATASET_FILE, response.items().size());
        }
    }

    /**
     * Searches with the query.
     *
     * @throws IOException the IO exception
     */
    public void searchWithQuery() throws IOException {

        final String keyword = "reactive";
        final String project = "miscellany";
        final Query mustAppearQuery = new Query.Builder().term(
                        termBuilder -> termBuilder.field("keyword").value(
                                valueBuilder -> valueBuilder.stringValue(keyword)))
                .build();
        final Query mustNotAppearQuery = new Query.Builder().term(
                        termBuilder -> termBuilder.field("project").value(
                                valueBuilder -> valueBuilder.stringValue(project)))
                .build();
        final SearchResponse<Box> searchResponse = client.search(
                searchRequestBuilder -> searchRequestBuilder.size(100).index(Constants.INDEX_NAME).query(
                        queryBuilder -> queryBuilder.bool(boolQueryBuilder -> boolQueryBuilder
                                .must(mustAppearQuery).mustNot(mustNotAppearQuery))),
                Box.class);
        final List<Hit<Box>> hitList = searchResponse.hits().hits();
        if (hitList.isEmpty()) {
            logger.warn("searchKeyword(): nothing found with keyword[{}]", keyword);
            return;
        }
        final TotalHits total = searchResponse.hits().total();
        final StringBuilder strBld = new StringBuilder();
        strBld.append("searchKeyword(): keyword[%s], not in project[%s]%n".formatted(keyword, project));
        if (Objects.nonNull(total)) {
            strBld.append("\tresults total[%s], total is accurate[%b]%n"
                    .formatted(total.value(), TotalHitsRelation.Eq == total.relation()));
        } else {
            strBld.append("\t'TotalHits' is null%n");
        }
        final AtomicInteger atomic = new AtomicInteger();
        hitList.forEach(hit -> {
            final Box box = hit.source();
            if (Objects.nonNull(box)) {
                strBld.append(("%n\t%02d, score[%.3f], keyword[%s], project[%s], category[%s]%n" +
                               "\t\tpath[%s]%n\t\tfileName[%s], fileExtension[%s], lineNumber[%d]").formatted(
                        atomic.incrementAndGet(), hit.score(), box.keyword(), box.project(), box.category(),
                        box.path(), box.fileName(), box.fileExtension(), box.lineNumber()));
            } else {
                strBld.append("\tbox is null%n");
            }
        });
        logger.info(strBld.toString());
    }

}
