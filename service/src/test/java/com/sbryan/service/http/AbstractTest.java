package com.sbryan.service.http;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sbryan.service.elastic.entry.ItemField;
import static com.sbryan.service.utils.ResourceUtils.*;
import io.r2dbc.spi.ConnectionFactory;
import io.swagger.v3.oas.models.OpenAPI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.springframework.web.reactive.function.BodyInserters.*;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

public class AbstractTest {

    private static final String POSTGRESQL_IMAGE = "postgres:16-alpine";
    private static final String ELASTIC_IMAGE = "ghcr.io/hirotasoshu/elasticsearch";
    private static final String POSTGRESQL_INIT_DB_SQL = "init_db.sql";
    private static final String DOCKER_ENTRYPOINT = "/docker-entrypoint-initdb.d/init_db.sql";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OpenAPI openAPI;

    @Value("classpath:create_item.sql")
    private Resource delete;
    @Autowired
    private static final ElasticsearchTemplate elasticsearchTemplate;

    public static final ElasticsearchClient ELASTIC_SEARCH_CLIENT;

    private static final RestClient ELASTIC_SEARCH_REST_CLIENT;

    protected static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;
    protected static ElasticsearchContainer container;
    @Autowired
    private ConnectionFactory r2dbcConnectionFactory;

    static {
        var psqlImage = DockerImageName
                .parse(POSTGRESQL_IMAGE)
                .asCompatibleSubstituteFor(POSTGRESQL_IMAGE);
        var elasticSearchImage = DockerImageName
                .parse(ELASTIC_IMAGE)
                .asCompatibleSubstituteFor("docker.elastic.co/elasticsearch/elasticsearch");

        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(psqlImage)
                .waitingFor(Wait.forListeningPort())
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test")
                .withClasspathResourceMapping(POSTGRESQL_INIT_DB_SQL, DOCKER_ENTRYPOINT, BindMode.READ_ONLY);
        container = new ElasticsearchContainer(elasticSearchImage)
                .withEnv("xpack.security.enabled", "false")
                .withEnv("discovery.type", "single-node")
                .waitingFor(Wait.forListeningPort())
                .withCreateContainerCmdModifier(cmd -> {
                    cmd.getHostConfig().withMemory(1073741824L);
                    cmd.withName("elasticsearch-testcontainer");
                });
        container.start();

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("test", "test"));

        ELASTIC_SEARCH_REST_CLIENT = RestClient
                .builder(HttpHost.create("http://" + container.getHttpHostAddress()))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
                .build();

        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper(objectMapper);

        ElasticsearchTransport elasticsearchTransport = new RestClientTransport(ELASTIC_SEARCH_REST_CLIENT, jacksonJsonpMapper);
        ELASTIC_SEARCH_CLIENT = new ElasticsearchClient(elasticsearchTransport);
        elasticsearchTemplate = new ElasticsearchTemplate(ELASTIC_SEARCH_CLIENT);
        elasticsearchTemplate.indexOps(ItemField.class).create();
        Startables.deepStart(Stream.of(POSTGRE_SQL_CONTAINER)).join();
    }

    protected void doAfterEach() {
        executeScript(delete);

        deleteIndex();
        createIndex();
    }

    @DynamicPropertySource
    static void initProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://"
                + POSTGRE_SQL_CONTAINER.getHost() + ":" + POSTGRE_SQL_CONTAINER.getFirstMappedPort()
                + "/" + POSTGRE_SQL_CONTAINER.getDatabaseName());
        registry.add("spring.r2dbc.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.r2dbc.password", POSTGRE_SQL_CONTAINER::getPassword);

        registry.add("elastic.host", () -> "localhost");
        registry.add("elastic.port", () -> container.getMappedPort(9200));
        registry.add("spring.elasticsearch.uris", container::getHttpHostAddress);
    }

    protected void getScenarioFromResource(String url, Resource expectedResponse, int httpCode) {
        webTestClient.get()
                .uri(url)
                .headers(headers -> headers.setBasicAuth("user", "user"))
                .exchange()
                .expectStatus().isEqualTo(httpCode)
                .expectBody()
                .json(getStringFromResource(expectedResponse));
    }

    protected void postScenarioFromResource(String url, Resource expectedRequest, Resource expectedResponse, int httpCode) {
        webTestClient.post()
                .uri(url)
                .headers(headers -> headers.setBasicAuth("user", "user"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(getStringFromResource(expectedRequest)))
                .exchange()
                .expectStatus().isEqualTo(httpCode)
                .expectBody()
                .json(getStringFromResource(expectedResponse));
    }

    protected void postScenarioFromResource(String url, Resource expectedRequest, Resource expectedResponse, int httpCode, boolean strict) {
        webTestClient.post()
                .uri(url)
                .headers(headers -> headers.setBasicAuth("user", "user"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(getStringFromResource(expectedRequest)))
                .exchange()
                .expectStatus().isEqualTo(httpCode)
                .expectBody()
                .json(getStringFromResource(expectedResponse), strict);
    }

    protected void putScenarioFromResource(String url, Resource request, Resource response, int httpCode) {
        webTestClient.put()
                .uri(url)
                .headers(headers -> headers.setBasicAuth("user", "user"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(getStringFromResource(request)))
                .exchange()
                .expectStatus().isEqualTo(httpCode)
                .expectBody()
                .json(getStringFromResource(response));
    }

    protected void deleteScenarioFromResource(String url, Resource response, int httpCode) {
        webTestClient.delete()
                .uri(url)
                .headers(headers -> headers.setBasicAuth("user", "user"))
                .exchange()
                .expectStatus().isEqualTo(httpCode)
                .expectBody()
                .json(getStringFromResource(response));
    }

    protected void createIndex() {
        elasticsearchTemplate.indexOps(ItemField.class).create();
    }

    protected void deleteIndex() {
        elasticsearchTemplate.indexOps(ItemField.class).delete();
    }

    protected void executeScript(final Resource sqlScript) {
        Mono.from(r2dbcConnectionFactory.create())
                .flatMap(connection -> ScriptUtils.executeSqlScript(connection, new EncodedResource(sqlScript, StandardCharsets.UTF_8))
                        .then(Mono.from(connection.close())))
                .block();
    }

}
