package com.sbryan.service.entity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("delivery")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DeliveryEntity implements Persistable<String> {

    @Id
    @Column("id")
    private String uuid;
    @Column("transport_id")
    private String transportId;
    @Column("created_date")
    private LocalDate createdDate;
    @Column("qty")
    private Long qty;
    @Column("username")
    private String username;

    @Setter
    @Transient
    private boolean isNew = false;

    @Override
    public String getId() {
        return uuid;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
