package com.sbryan.service.http;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "5s")
public class TransportHttpTest extends AbstractTest {

    @Value("classpath:/api/transport/get/get-response.json")
    private Resource successGetTransportResponse;

    @Value("classpath:/api/transport/get/get-all-response.json")
    private Resource successGetAllTransportsResponse;

    @Value("classpath:/api/transport/post/request.json")
    private Resource successCreateTransportRequest;
    @Value("classpath:/api/transport/post/response.json")
    private Resource successCreateTransportResponse;

    @Value("classpath:/api/transport/put/request.json")
    private Resource successUpdateTransportRequest;
    @Value("classpath:/api/transport/put/response.json")
    private Resource successUpdateTransportResponse;

    @Value("classpath:/api/transport/delete/response.json")
    private Resource successDeleteTransportResponse;

    @AfterEach
    @SneakyThrows
    public void after() {
        doAfterEach();
    }

    public void init() {
        postScenarioFromResource("/transport/create", successCreateTransportRequest, successCreateTransportResponse, 200);
    }

    @Test
    @DisplayName("Get Transport")
    public void getTransportTest() {
        init();
        getScenarioFromResource("/transport/get/3fa85f64-5717-4562-b3fc-2c963f66afa6", successGetTransportResponse, 200);
    }

    @Test
    @DisplayName("Get All Transports")
    public void getAllTransportsTest() {
        init();
        getScenarioFromResource("/transport/get", successGetAllTransportsResponse, 200);
    }

    @Test
    @DisplayName("Create Transport when transport already exists")
    @Disabled("Doesn't work")
    public void createTransportTest() {
        init();
        postScenarioFromResource("/transport/create",
                successCreateTransportRequest, successCreateTransportResponse, 500);
    }

    @Test
    @DisplayName("Update transport")
    public void updateTransportTest() {
        init();
        putScenarioFromResource("/transport/update/3fa85f64-5717-4562-b3fc-2c963f66afa6",
                successUpdateTransportRequest, successUpdateTransportResponse, 200);
    }

    @Test
    @DisplayName("Delete transport")
    public void deleteTransportTest() {
        init();
        deleteScenarioFromResource("/transport/delete/3fa85f64-5717-4562-b3fc-2c963f66afa6",
                successDeleteTransportResponse, 200);
    }

}
