package com.sbryan.service.application;

import com.service.deliveryservice.api.adapter.in.http.model.Category;
import java.util.List;
import reactor.core.publisher.Mono;

public interface CategoryService {

    Mono<Category> save(Category category);

    Mono<List<String>> findAll();

    Mono<Category> update(Long id, Category category);

    Mono<String> delete(Long id);
}
