package com.sbryan.service.elastic.repository;

import com.sbryan.service.elastic.entry.ItemField;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemElasticRepository extends ReactiveElasticsearchRepository<ItemField, String>, ItemElasticRepositoryExtension {
}