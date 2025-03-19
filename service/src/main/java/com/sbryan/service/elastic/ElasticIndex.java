package com.sbryan.service.elastic;

import com.sbryan.service.elastic.entry.ItemField;
import com.sbryan.service.elastic.repository.ItemElasticRepository;
import com.sbryan.service.entity.CategoryEntity;
import com.sbryan.service.entity.ItemEntity;
import com.sbryan.service.exception.ElasticException;
import com.sbryan.service.mapper.CategoryMapper;
import com.sbryan.service.mapper.ItemMapper;
import com.sbryan.service.repository.CategoryRepository;
import com.sbryan.service.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ElasticIndex {

    private final ItemElasticRepository repository;
    private final ItemRepository itemRepository;

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final ItemMapper mapper;

    @Scheduled(initialDelay = 5000, fixedRate = 24 * 60 * 60 * 1000)
    public void reindex() {
        repository.deleteAll()
                .onErrorResume(Mono::error)
                .thenMany(itemRepository.findAll())
                .flatMap(item -> categoryRepository.findAll()
                        .collectList()
                        .flatMap(cat -> {
                            var itemField = getItemField(item, cat);
                            return repository.save(itemField);
                        }))
                .onErrorResume(ex -> Mono.error(new ElasticException(ex.getMessage())))
                .then().subscribe();
    }

    private ItemField getItemField(ItemEntity item, List<CategoryEntity> categories) {
        var field = mapper.mapToEs(item);
        field.setId(String.valueOf(item.getId()));

        if (item.getCategoryId() == null) {
            return field;
        }

        var category = categories.get(item.getCategoryId().intValue());
        if (category == null) {
            return field;
        }

        return field.setCategory(categoryMapper.map(category));
    }


}
