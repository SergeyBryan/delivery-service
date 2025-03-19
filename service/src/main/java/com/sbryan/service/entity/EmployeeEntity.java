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

@Table("employee")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EmployeeEntity implements Persistable<Long> {

    @Id
    @Column("id")
    private Long id;
    @Column("name")
    private String name;
    @Column("position")
    private String position;
    @Column("salary")
    private Integer salary;
    @Column("started_date")
    private LocalDate startedDate;
    @Column("end_date")
    private LocalDate endDate;
    @Column("department")
    private String department;
    @Column("email")
    private String email;

    @Transient
    @Setter
    private boolean isNew = false;

    @Override
    public boolean isNew() {
        return isNew;
    }
}

