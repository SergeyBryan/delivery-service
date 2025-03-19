package com.sbryan.service.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("item_delivery_link")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ItemDeliveryLink implements Persistable<String> {

    @Id
    @Column("id")
    private String id;

    @Column("delivery_id")
    private String deliveryId;

    @Column("item_id")
    private String itemId;

    @Column("quantity")
    private Long quantity;

    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
