package com.sbryan.service.application;

import com.sbryan.service.elastic.entry.GlobalItemInfo;
import com.service.deliveryservice.api.adapter.in.http.model.GetItemsResponse;
import com.service.deliveryservice.api.adapter.in.http.model.Item;
import reactor.core.publisher.Mono;

public interface ItemService {
    Mono<Item> create(Mono<Item> itemMono);

    Mono<Item> getItemById(Long id);

    Mono<GetItemsResponse> getItemByName(String name);

    Mono<GlobalItemInfo> getAll();

    Mono<GlobalItemInfo> getAllByCategoryName(String name);

    Mono<String> deleteItem(Long id);

    Mono<Item> updateItem(Long id, Item item);
}
