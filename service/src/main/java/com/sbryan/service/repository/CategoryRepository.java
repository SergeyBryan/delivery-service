package com.sbryan.service.repository;

import com.sbryan.service.entity.CategoryEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CategoryRepository extends ReactiveCrudRepository<CategoryEntity, Long> {
}
