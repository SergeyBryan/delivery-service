package com.sbryan.service.http;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "5s")
public class EmployeeHttpTest extends AbstractTest {

    @Value("classpath:api/employee/get/get-response.json")
    private Resource successGetResponse;
    @Value("classpath:api/employee/get/get-all-response.json")
    private Resource successGetAllResponse;

    @Value("classpath:api/employee/post/request.json")
    private Resource createEmployeeRequest;
    @Value("classpath:api/employee/post/response.json")
    private Resource createEmployeeResponse;

    @Value("classpath:api/employee/put/request.json")
    private Resource updateEmployeeRequest;
    @Value("classpath:api/employee/put/response.json")
    private Resource updateEmployeeResponse;

    @Value("classpath:api/employee/delete/response.json")
    private Resource deleteEmployeeResponse;


    @AfterEach
    @SneakyThrows
    public void after() {
        doAfterEach();
    }

    public void init() {
        postScenarioFromResource("/employee/create", createEmployeeRequest, createEmployeeResponse, 200);
    }

    @Test
    @DisplayName("Get all employees")
    public void getAllEmployeesTest() {
        init();
        getScenarioFromResource("/employee/get", successGetAllResponse, 200);
    }

    @Test
    @DisplayName("Get employee by id")
    public void getEmployeeByIdTest() {
        init();
        getScenarioFromResource("/employee/get/1", successGetResponse, 200);
    }

    @Test
    @DisplayName("Create employee")
    public void createEmployeeTest() {
        postScenarioFromResource("/employee/create", createEmployeeRequest, createEmployeeResponse, 200);
    }

    @Test
    @DisplayName("Update employee")
    public void updateEmployeeTest() {
        init();
        putScenarioFromResource("/employee/update/1", updateEmployeeRequest, updateEmployeeResponse, 200);
    }

    @Test
    @DisplayName("Delete employee")
    @SneakyThrows
    public void deleteEmployeeTest() {
        init();
        deleteScenarioFromResource("/employee/delete/1", deleteEmployeeResponse, 200);
    }

}
