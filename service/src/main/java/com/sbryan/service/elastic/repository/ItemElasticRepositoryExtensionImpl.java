package com.sbryan.service.elastic.repository;

import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.sbryan.service.elastic.entry.AggregatedItemFieldValues;
import com.sbryan.service.elastic.entry.GlobalItemInfo;
import com.sbryan.service.elastic.entry.ItemField;
import com.service.deliveryservice.api.adapter.in.http.model.Category;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ItemElasticRepositoryExtensionImpl implements ItemElasticRepositoryExtension {

    private static final String ITEM_PRICE = "item.price";
    private static final String CATEGORY_NAME = "category.name";
    private static final String ITEM_NAME = "item.name";
    private static final String CATEGORY = "category";
    private static final String MIN = "min";
    private static final String MAX = "max";
    private static final String NO_CATEGORY_WAS_FOUND = "No category was found";
    private final ElasticsearchOperations operations;

    @Override
    public GlobalItemInfo findAllWithAggregation() {
        return executeQuery(new NativeQueryBuilder().withQuery(
                QueryBuilders.matchAll().build()._toQuery()));
    }

    @Override
    public List<String> findAllCategories() {
        return executeFieldQuery(new NativeQueryBuilder().withQuery(
                QueryBuilders.bool().must(QueryBuilders.exists().field(CATEGORY).build()._toQuery())
                        .build()._toQuery()));
    }

    @Override
    public GlobalItemInfo findAllItemsByCategoryName(String name) {
        return executeQuery(new NativeQueryBuilder().withQuery(
                QueryBuilders.term().field(CATEGORY_NAME).queryName(name).build()._toQuery())
        );
    }

    @Override
    public GlobalItemInfo findItemsByName(String name) {
        return executeQuery(new NativeQueryBuilder().withQuery(
                QueryBuilders.matchPhrasePrefix().query(name).field(ITEM_NAME).build()._toQuery())
        );
    }

    private List<String> executeFieldQuery(NativeQueryBuilder query) {
        var nativeQuery = query
                .withSourceFilter(new FetchSourceFilter(new String[]{CATEGORY_NAME}, null))
                .withPageable(Pageable.ofSize(20))
                .build();

        var searchHits = operations.search(nativeQuery, ItemField.class);

        return searchHits.stream()
                .map(SearchHit::getContent)
                .map(itemField -> Optional.ofNullable(itemField.getCategory())
                        .map(Category::getName)
                        .orElse(NO_CATEGORY_WAS_FOUND))
                .toList();
    }

    private GlobalItemInfo executeQuery(NativeQueryBuilder query) {
        var nativeQuery = query
                .withPageable(Pageable.ofSize(20))
                .withAggregation(MIN, AggregationBuilders.min(a -> a.field(ITEM_PRICE)))
                .withAggregation(MAX, AggregationBuilders.max(a -> a.field(ITEM_PRICE)))
                .build();

        var searchHits = operations.search(nativeQuery, ItemField.class);
        var items = searchHits.stream().map(SearchHit::getContent).toList();
        var aggregations = (ElasticsearchAggregations) searchHits.getAggregations();

        return GlobalItemInfo.builder()
                .items(items)
                .values(getMinMax(aggregations))
                .build();
    }

    private AggregatedItemFieldValues getMinMax(ElasticsearchAggregations aggregations) {
        if (aggregations != null) {
            return AggregatedItemFieldValues.builder()
                    .minValue(aggregations.aggregationsAsMap().get(MIN).aggregation().getAggregate().min().value())
                    .maxValue(aggregations.aggregationsAsMap().get(MAX).aggregation().getAggregate().max().value())
                    .build();
        }
        return null;
    }
}
