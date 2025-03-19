package com.sbryan.service.mapper;

import com.sbryan.service.entity.CategoryEntity;
import com.service.deliveryservice.api.adapter.in.http.model.Category;
import com.service.deliveryservice.api.adapter.in.http.model.CreateCategoryResponse;
import com.service.deliveryservice.api.adapter.in.http.model.DeleteCategoryResponse;
import com.service.deliveryservice.api.adapter.in.http.model.GetCategoriesResponse;
import com.service.deliveryservice.api.adapter.in.http.model.UpdateCategoryResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryEntity map(Category category);

    Category map(CategoryEntity source);

    CreateCategoryResponse mapCreate(Category category);

    UpdateCategoryResponse mapUpdate(Category category);

    DeleteCategoryResponse mapDelete(String response);

    default GetCategoriesResponse mapGet(List<String> source) {
        return GetCategoriesResponse.builder()
                .content(source)
                .build();
    }
}
