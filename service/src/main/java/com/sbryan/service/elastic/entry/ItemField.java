package com.sbryan.service.elastic.entry;

import com.service.deliveryservice.api.adapter.in.http.model.Category;
import com.service.deliveryservice.api.adapter.in.http.model.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "item")
@Getter
@Setter
@Accessors(chain = true)
@ToString
@Builder
@Setting(
//        sortFields = {"id"},
        sortModes = Setting.SortMode.min,
        sortOrders = Setting.SortOrder.asc,
        sortMissingValues = Setting.SortMissing._last
)
public class ItemField {

    @Id
//    @Field
    private String id;

    @Field(name = "item", type = FieldType.Object)
    private Item item;

    @Field(name = "category", type = FieldType.Nested)
    private Category category;

}
