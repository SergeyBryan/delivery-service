package com.sbryan.service.mapper;

import com.sbryan.service.elastic.entry.AggregatedItemFieldValues;
import com.sbryan.service.elastic.entry.GlobalItemInfo;
import com.sbryan.service.elastic.entry.ItemField;
import com.sbryan.service.entity.ItemEntity;
import com.service.deliveryservice.api.adapter.in.http.model.CreateItemResponse;
import com.service.deliveryservice.api.adapter.in.http.model.DeleteItemResponse;
import com.service.deliveryservice.api.adapter.in.http.model.GetItemResponse;
import com.service.deliveryservice.api.adapter.in.http.model.GetItemsResponse;
import com.service.deliveryservice.api.adapter.in.http.model.Item;
import com.service.deliveryservice.api.adapter.in.http.model.Stats;
import com.service.deliveryservice.api.adapter.in.http.model.UpdateItemResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemEntity map(Item item);

    Item map(ItemEntity itemEntity);

    @Mapping(target = "content", source = "items")
    @Mapping(target = "stats", source = "values")
    GetItemsResponse map(GlobalItemInfo source);

    Stats map(AggregatedItemFieldValues values);

    default ItemField mapToEs(ItemEntity itemEn) {
        Item item = map(itemEn);
        return ItemField.builder()
                .id(item.getId().toString())
                .item(item)
                .build();
    }

    default Item mapToEs(ItemField es) {
        return es.getItem();
    }

    default List<Item> map(List<ItemField> itemEl) {
        return itemEl.stream()
                .map(this::mapToEs)
                .toList();
    }

    GetItemResponse mapGet(Item item);
    UpdateItemResponse mapUpdate(Item item);

    DeleteItemResponse mapDelete(String response);
    CreateItemResponse mapCreate(Item item);
}
