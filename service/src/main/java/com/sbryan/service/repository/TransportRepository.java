package com.sbryan.service.repository;

import com.sbryan.service.entity.TransportEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TransportRepository extends ReactiveCrudRepository<TransportEntity, String> {
}
