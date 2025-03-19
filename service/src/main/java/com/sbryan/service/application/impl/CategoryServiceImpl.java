package com.sbryan.service.application.impl;

import com.sbryan.service.application.CategoryService;
import com.sbryan.service.elastic.ElasticService;
import com.sbryan.service.exception.CategoryNotFoundException;
import com.sbryan.service.mapper.CategoryMapper;
import com.sbryan.service.repository.CategoryRepository;
import com.service.deliveryservice.api.adapter.in.http.model.Category;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final ElasticService elasticService;

    @Override
    public Mono<Category> save(Category category) {
        return repository.save(mapper.map(category).setNew(true))
                .map(mapper::map);
    }

    @Override
    public Mono<List<String>> findAll() {
        return elasticService.getAllCategories();
    }

    @Override
    public Mono<Category> update(Long id, Category category) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CategoryNotFoundException(id)))
                .flatMap(e -> repository.save(mapper.map(category).setId(e.getId())))
                .map(mapper::map);
    }

    @Override
    public Mono<String> delete(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CategoryNotFoundException(id)))
                .flatMap(e -> repository.deleteById(id))
                .thenReturn("Category with %d was deleted".formatted(id));
    }
}
