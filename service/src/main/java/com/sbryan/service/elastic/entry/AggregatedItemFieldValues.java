package com.sbryan.service.elastic.entry;

import lombok.Builder;

@Builder
public record AggregatedItemFieldValues(Double minValue, Double maxValue) {
}
