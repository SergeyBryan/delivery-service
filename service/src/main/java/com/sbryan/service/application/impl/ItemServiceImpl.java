package com.sbryan.service.application.impl;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.sbryan.service.application.ItemService;
import com.sbryan.service.elastic.entry.GlobalItemInfo;
import com.sbryan.service.elastic.repository.ItemElasticRepository;
import com.sbryan.service.exception.ElasticException;
import com.sbryan.service.exception.ItemNotFoundException;
import com.sbryan.service.mapper.ItemMapper;
import com.sbryan.service.repository.ItemRepository;
import com.service.deliveryservice.api.adapter.in.http.model.GetItemsResponse;
import com.service.deliveryservice.api.adapter.in.http.model.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final ItemMapper mapper;

    private final ItemElasticRepository itemElasticRepository;

    @Override
    public Mono<Item> create(Mono<Item> itemMono) {
        return itemMono.flatMap(item -> {
            var i = mapper.map(item).setNew(true);
            return itemRepository.save(i)
                    .doOnSuccess(saved -> log.info("The item with id {} was created", i.getId()))
                    .flatMap(itemEs ->
                        itemElasticRepository.save(mapper.mapToEs(itemEs)))
                    .doOnSuccess(saved -> log.info("The item with id {} was saved in Elasticsearch", saved.getId()))
                    .onErrorResume(ex -> Mono.error(new ElasticException("Could not create item in Elasticsearch")))
                    .map(mapper::mapToEs);
        });
    }

    @Override
    public Mono<Item> getItemById(Long id) {
        return itemElasticRepository.findById(String.valueOf(id))
                .map(mapper::mapToEs)
                .switchIfEmpty(Mono.error(ItemNotFoundException::new));
    }

    @Override
    public Mono<GetItemsResponse> getItemByName(String name) {
        return Mono.just(mapper.map(itemElasticRepository.findItemsByName(name)));
    }

    @Override
    public Mono<GlobalItemInfo> getAll() {
        return Mono.just(itemElasticRepository.findAllWithAggregation());
    }

    @Override
    public Mono<GlobalItemInfo> getAllByCategoryName(String name) {
        return Mono.just(itemElasticRepository.findAllItemsByCategoryName(name));
    }

    @Override
    public Mono<String> deleteItem(Long id) {
        return itemRepository.findById(id)
                .switchIfEmpty(Mono.error(new ItemNotFoundException()))
                .doOnError(error -> log.error("The item with id {} was not found", id))
                .flatMap(entity -> itemRepository.deleteById(id)
                        .publishOn(Schedulers.boundedElastic())
                        .doOnSuccess(deleted -> {
                            log.info("The item with id {} was deleted", id);
                            itemElasticRepository.deleteById(String.valueOf(id)).subscribe();
                            log.info("The item with id {} was deleted in Elasticsearch", id);
                        })
                        .thenReturn("The item with id %d was deleted".formatted(id)));
    }

    @Override
    public Mono<Item> updateItem(Long id, Item item) {
        return itemRepository.findById(id)
                .switchIfEmpty(Mono.error(new ItemNotFoundException()))
                .flatMap(entity -> itemRepository.save(mapper.map(item).setId(id))
                        .publishOn(Schedulers.boundedElastic())
                        .doOnSuccess(updated -> {
                            log.info("The item with id {} was updated", id);
                            itemElasticRepository.save(mapper.mapToEs(updated), RefreshPolicy.IMMEDIATE).subscribe();
                            log.info("The item with id {} was updated in Elasticsearch", id);
                        }))
                .map(mapper::map);
    }
}
