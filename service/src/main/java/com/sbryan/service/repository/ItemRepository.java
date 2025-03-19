package com.sbryan.service.repository;

import com.sbryan.service.entity.ItemEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ItemRepository extends ReactiveCrudRepository<ItemEntity, Long> {

}
