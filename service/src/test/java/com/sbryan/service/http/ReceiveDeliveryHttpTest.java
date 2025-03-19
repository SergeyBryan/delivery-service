package com.sbryan.service.http;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "55s")
@ActiveProfiles("test")
public class ReceiveDeliveryHttpTest extends AbstractTest {

    @Value("classpath:api/delivery/post/request.json")
    private Resource successCreateItemRequest;

    @Value("classpath:api/delivery/post/response.json")
    private Resource successCreateItemResponse;


    @AfterEach
    @SneakyThrows
    public void after() {
        doAfterEach();
    }

    @Test
    @DisplayName("Success get items")
    @SneakyThrows
    public void testSuccessAllItemsResponse() {
        postScenarioFromResource("/delivery", successCreateItemRequest, successCreateItemResponse, 200, false);
    }
}
