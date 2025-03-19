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

@Table("transport")
@Getter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TransportEntity implements Persistable<String> {

    @Id
    @Column("id")
    private String id;
    @Column("delivery_date")
    private LocalDate deliveryDate;
    @Column("accepted_date")
    private LocalDate acceptedDate;
    @Column("company")
    private String company;
    @Column("brand")
    private String brand;
    @Column("serial")
    private String serial;
    @Column("mark")
    private String mark;

    @Transient
    @Setter
    private boolean isNew = false;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
