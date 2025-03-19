package com.sbryan.service.api;

import com.sbryan.service.application.CategoryService;
import com.sbryan.service.mapper.CategoryMapper;
import com.service.deliveryservice.api.adapter.in.http.CategoryApi;
import com.service.deliveryservice.api.adapter.in.http.model.CreateCategoryRequest;
import com.service.deliveryservice.api.adapter.in.http.model.CreateCategoryResponse;
import com.service.deliveryservice.api.adapter.in.http.model.DeleteCategoryResponse;
import com.service.deliveryservice.api.adapter.in.http.model.GetCategoriesResponse;
import com.service.deliveryservice.api.adapter.in.http.model.UpdateCategoryRequest;
import com.service.deliveryservice.api.adapter.in.http.model.UpdateCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CategoryApis implements CategoryApi {

    private final CategoryService categoryService;
    private final CategoryMapper mapper;

    @Override
    public Mono<CreateCategoryResponse> createCategory(Mono<CreateCategoryRequest> createCategoryRequest, ServerWebExchange exchange) {
        return createCategoryRequest
                .flatMap(c -> categoryService.save(c.getCategory()))
                .map(mapper::mapCreate);
    }

    @Override
    public Mono<DeleteCategoryResponse> deleteCategory(Long id, ServerWebExchange exchange) {
        return categoryService.delete(id)
                .map(mapper::mapDelete);
    }

    @Override
    public Mono<GetCategoriesResponse> getCategories(ServerWebExchange exchange) {
        return categoryService.findAll()
                .map(mapper::mapGet);
    }

    @Override
    public Mono<UpdateCategoryResponse> updateCategory(Long id, Mono<UpdateCategoryRequest> updateCategoryRequest, ServerWebExchange exchange) {
        return updateCategoryRequest.flatMap(req -> categoryService.update(id, req.getCategory()))
                .map(mapper::mapUpdate);
    }
}
