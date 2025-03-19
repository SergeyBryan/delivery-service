package com.sbryan.service.elastic;

import java.util.List;
import reactor.core.publisher.Mono;

public interface ElasticService {

    Mono<List<String>> getAllCategories();
}
