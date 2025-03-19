package com.sbryan.service.repository;

import com.sbryan.service.entity.EmployeeEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EmployeeRepository extends ReactiveCrudRepository<EmployeeEntity, Long> {
}
