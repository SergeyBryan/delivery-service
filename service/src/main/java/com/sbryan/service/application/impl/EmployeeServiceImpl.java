package com.sbryan.service.application.impl;

import com.sbryan.service.application.EmployeeService;
import com.sbryan.service.exception.EmployeeException;
import com.sbryan.service.exception.EmployeeNotFoundException;
import com.sbryan.service.mapper.EmployeeMapper;
import com.sbryan.service.repository.EmployeeRepository;
import com.service.deliveryservice.api.adapter.in.http.model.Employee;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper mapper;

    @Override
    public Mono<Employee> addEmployee(Employee employee) {
        return employeeRepository.save(mapper.map(employee).setNew(true))
                .map(em -> {
                    log.info("Employee added {}", employee.getId());
                    return mapper.map(em);
                }).onErrorResume(ex -> {
                    log.error("UNKNOWN_EMPLOYEE_ERROR: {}", ex.getMessage());
                    return Mono.error(EmployeeException::new);
                });
    }

    @Override
    public Mono<String> removeEmployee(Long id) {
        return employeeRepository.deleteById(id)
                .onErrorResume(ex -> Mono.error(EmployeeNotFoundException::new))
                .thenReturn("The employee has been removed: id =%d".formatted(id));
    }

    @Override
    public Mono<Employee> updateEmployee(Long id, Employee employee) {
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException()))
                .flatMap(entity -> {
                    employee.setId(entity.getId());
                    employee.setStartedDate(entity.getStartedDate());
                    return employeeRepository.save(mapper.map(employee));
                })
                .map(mapper::map);
    }

    @Override
    public Mono<List<Employee>> listEmployees() {
        return employeeRepository.findAll().collectList()
                .map(mapper::map);
    }

    @Override
    public Mono<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException()))
                .map(mapper::map);
    }
}
