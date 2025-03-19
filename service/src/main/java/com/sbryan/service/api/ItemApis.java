package com.sbryan.service.api;

import com.sbryan.service.application.ItemService;
import com.sbryan.service.mapper.ItemMapper;
import com.service.deliveryservice.api.adapter.in.http.ItemApi;
import com.service.deliveryservice.api.adapter.in.http.model.CreateItemRequest;
import com.service.deliveryservice.api.adapter.in.http.model.CreateItemResponse;
import com.service.deliveryservice.api.adapter.in.http.model.DeleteItemResponse;
import com.service.deliveryservice.api.adapter.in.http.model.GetItemResponse;
import com.service.deliveryservice.api.adapter.in.http.model.GetItemsResponse;
import com.service.deliveryservice.api.adapter.in.http.model.Item;
import com.service.deliveryservice.api.adapter.in.http.model.UpdateItemRequest;
import com.service.deliveryservice.api.adapter.in.http.model.UpdateItemResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Api(tags = "Item")
@RestController
@RequiredArgsConstructor
public class ItemApis implements ItemApi {

    private final ItemService service;
    private final ItemMapper mapper;

    @Override
    public Mono<GetItemsResponse> getItems(ServerWebExchange exchange) {
        return service.getAll().map(p-> {var m = mapper.map(p);
            System.out.println(p.getItems());
            System.out.println(m.getContent());
            System.out.println(m.getStats());
        return m;});
    }

    @Override
    public Mono<CreateItemResponse> createItem(Mono<CreateItemRequest> request, ServerWebExchange exchange) {
        return request.flatMap(req -> {
                    Mono<Item> itemMono = Mono.just(req.getItem());
                    return service.create(itemMono);
                })
                .map(mapper::mapCreate);
    }

    @Override
    public Mono<DeleteItemResponse> deleteItem(Long id, ServerWebExchange exchange) {
        return service.deleteItem(id)
                .map(mapper::mapDelete);
    }

    @Override
    public Mono<GetItemResponse> getItem(Long id, ServerWebExchange exchange) {
        return service.getItemById(id)
                .map(mapper::mapGet);
    }

    @Override
    public Mono<GetItemsResponse> getItemsByName(String name, ServerWebExchange exchange) {
        return service.getItemByName(name);
    }

    @Override
    public Mono<GetItemsResponse> getItemsByCategoryName(String name, ServerWebExchange exchange) {
        return service.getAllByCategoryName(name)
                .map(mapper::map);
    }

    @Override
    public Mono<UpdateItemResponse> updateItem(Long id, Mono<UpdateItemRequest> request, ServerWebExchange exchange) {
        return request.flatMap(req -> service.updateItem(id, req.getItem())
                .map(mapper::mapUpdate));
    }
}
