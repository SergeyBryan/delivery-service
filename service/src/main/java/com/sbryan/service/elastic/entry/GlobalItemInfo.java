package com.sbryan.service.elastic.entry;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GlobalItemInfo {

    private List<ItemField> items;

    private AggregatedItemFieldValues values;

}
