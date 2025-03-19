package com.sbryan.service.application;

import com.service.deliveryservice.api.adapter.in.http.model.Employee;
import java.util.List;
import reactor.core.publisher.Mono;

public interface EmployeeService {
    Mono<Employee> addEmployee(Employee employee);

    Mono<String> removeEmployee(Long id);

    Mono<Employee> updateEmployee(Long id, Employee employee);

    Mono<List<Employee>> listEmployees();

    Mono<Employee> getEmployeeById(Long id);
}
