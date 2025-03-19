package com.sbryan.service.elastic.repository;

import com.sbryan.service.elastic.entry.GlobalItemInfo;
import java.util.List;

public interface ItemElasticRepositoryExtension {

    GlobalItemInfo findAllWithAggregation();

    List<String> findAllCategories();

    GlobalItemInfo findAllItemsByCategoryName(String name);

    GlobalItemInfo findItemsByName(String name);
}
