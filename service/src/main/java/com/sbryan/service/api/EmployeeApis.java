package com.sbryan.service.api;

import com.sbryan.service.application.EmployeeService;
import com.sbryan.service.mapper.EmployeeMapper;
import com.service.deliveryservice.api.adapter.in.http.EmployeeApi;
import com.service.deliveryservice.api.adapter.in.http.model.CreateEmployeeResponse;
import com.service.deliveryservice.api.adapter.in.http.model.DeleteEmployeeResponse;
import com.service.deliveryservice.api.adapter.in.http.model.Employee;
import com.service.deliveryservice.api.adapter.in.http.model.GetEmployeeResponse;
import com.service.deliveryservice.api.adapter.in.http.model.GetEmployeesResponse;
import com.service.deliveryservice.api.adapter.in.http.model.UpdateEmployeeRequest;
import com.service.deliveryservice.api.adapter.in.http.model.UpdateEmployeeResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Api(tags = {"Employee"})
@RestController
@RequiredArgsConstructor
public class EmployeeApis implements EmployeeApi {

    private final EmployeeService service;
    private final EmployeeMapper mapper;

    @Override
    public Mono<CreateEmployeeResponse> saveEmployee(
            Mono<Employee> employee, ServerWebExchange exchange) {
        return employee.flatMap(service::addEmployee)
                .map(mapper::mapCreate);
    }

    @Override
    public Mono<DeleteEmployeeResponse> deleteEmployee(
            Long id, ServerWebExchange exchange) {
        return service.removeEmployee(id)
                .map(mapper::map);
    }

    @Override
    public Mono<GetEmployeesResponse> getEmployeeList(ServerWebExchange exchange) {
        return service.listEmployees()
                .map(mapper::mapGetAll);
    }

    @Override
    public Mono<GetEmployeeResponse> getEmployeeById(Long id, ServerWebExchange exchange) {
        return service.getEmployeeById(id)
                .map(mapper::mapGet);
    }

    @Override
    public Mono<UpdateEmployeeResponse> updateEmployee(
            Long id, Mono<UpdateEmployeeRequest> request, ServerWebExchange exchange) {
        return request.flatMap(req -> service.updateEmployee(id, mapper.map(req.getEmployee())))
                .map(mapper::mapUpdate);
    }
}

