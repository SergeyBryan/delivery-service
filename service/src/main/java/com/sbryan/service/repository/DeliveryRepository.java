package com.sbryan.service.repository;

import com.sbryan.service.entity.DeliveryEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface DeliveryRepository extends ReactiveCrudRepository<DeliveryEntity, String> {
}
