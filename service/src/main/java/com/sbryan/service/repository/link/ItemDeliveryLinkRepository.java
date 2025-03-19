package com.sbryan.service.repository.link;

import com.sbryan.service.entity.ItemDeliveryLink;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ItemDeliveryLinkRepository extends R2dbcRepository<ItemDeliveryLink, String> {
}
