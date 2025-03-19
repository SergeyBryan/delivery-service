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
@AutoConfigureWebTestClient(timeout = "30s")
@ActiveProfiles("test")
public class ItemEntityHttpTest extends AbstractTest {

    @Value("classpath:api/item/get/get-response.json")
    private Resource successItemResponse;

    @Value("classpath:api/item/get/get-all-response.json")
    private Resource successAllItemsResponse;

    @Value("classpath:api/item/post/request.json")
    private Resource successCreateItemRequest;

    @Value("classpath:api/item/post/response.json")
    private Resource successCreateItemResponse;

    @Value("classpath:api/item/post/errorResponse.json")
    private Resource errorCreateItemResponse;

    @Value("classpath:api/item/put/request.json")
    private Resource successUpdateItemRequest;

    @Value("classpath:api/item/put/response.json")
    private Resource successUpdateItemResponse;

    @Value("classpath:api/item/delete/response.json")
    private Resource successDeleteItemResponse;

    @AfterEach
    @SneakyThrows
    public void after() {
        doAfterEach();
    }

    @SneakyThrows
    public void init() {
        Thread.sleep(5000);
        postScenarioFromResource("/item/create", successCreateItemRequest, successCreateItemResponse, 200);
    }

    @Test
    @DisplayName("Success get items")
    @SneakyThrows
    public void testSuccessAllItemsResponse() {
        init();
        Thread.sleep(1000);
        getScenarioFromResource("/item/get", successAllItemsResponse, 200);
    }

    @Test
    @DisplayName("Success get item by id")
    public void testSuccessItemResponse() {
        init();
        getScenarioFromResource("/item/get/1", successItemResponse, 200);
    }

    @Test
    @DisplayName("Success update item")
    public void testSuccessUpdateItem() {
        init();
        putScenarioFromResource("/item/update/1", successUpdateItemRequest, successUpdateItemResponse, 200);
    }

    @Test
    @DisplayName("Success delete item")
    public void testSuccessDeleteItem() {
        init();
        deleteScenarioFromResource("/item/delete/1", successDeleteItemResponse, 200);
    }

    @Test
    @DisplayName("Create item when item already exists")
    public void testCreateItemWhenItemAlreadyExists() {
        init();
        postScenarioFromResource("/item/create", successCreateItemRequest, errorCreateItemResponse, 500, false);
    }

}
