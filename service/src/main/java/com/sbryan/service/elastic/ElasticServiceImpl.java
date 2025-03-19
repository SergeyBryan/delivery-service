package com.sbryan.service.elastic;

import com.sbryan.service.elastic.repository.ItemElasticRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ElasticServiceImpl implements ElasticService {

    private final ItemElasticRepository itemElasticRepository;

    @Override
    public Mono<List<String>> getAllCategories() {
        return Mono.just(itemElasticRepository.findAllCategories());
    }
}
