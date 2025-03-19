package com.sbryan.service.entity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Accessors(chain = true)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item")
public class ItemEntity implements Persistable<Long> {

    @Id
    @Column(value = "id")
    private Long id;
    @Column(value = "name")
    private String name;
    @Column(value = "description")
    private String description;
    @Column(value = "quantity")
    private Integer pcs;
    @Column(value = "price")
    private Double price;
    @Column(value = "discount_price")
    private Double discountPrice;
    @Column(value = "created_date")
    private LocalDate createdDate = LocalDate.now();
    @Column(value = "category_id")
    private Long categoryId;

    @Transient
    private boolean isNew = false;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
