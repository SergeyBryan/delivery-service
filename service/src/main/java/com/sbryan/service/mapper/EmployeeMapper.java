package com.sbryan.service.mapper;

import com.sbryan.service.entity.EmployeeEntity;
import com.service.deliveryservice.api.adapter.in.http.model.CreateEmployeeResponse;
import com.service.deliveryservice.api.adapter.in.http.model.DeleteEmployeeResponse;
import com.service.deliveryservice.api.adapter.in.http.model.Employee;
import com.service.deliveryservice.api.adapter.in.http.model.GetEmployeeResponse;
import com.service.deliveryservice.api.adapter.in.http.model.GetEmployeesResponse;
import com.service.deliveryservice.api.adapter.in.http.model.UpdateEmployeeRequestPayload;
import com.service.deliveryservice.api.adapter.in.http.model.UpdateEmployeeResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee map(EmployeeEntity source);

    EmployeeEntity map(Employee source);

    List<Employee> map(List<EmployeeEntity> source);

    @Mapping(target = "position", expression = "java(PositionEnum.fromValue(source.getPosition()))")
    Employee map(UpdateEmployeeRequestPayload source);

    CreateEmployeeResponse mapCreate(Employee employee);

    UpdateEmployeeResponse mapUpdate(Employee employee);

    GetEmployeeResponse mapGet(Employee employee);

    @Mapping(target = "message")
    DeleteEmployeeResponse map(String message);

    default GetEmployeesResponse mapGetAll(List<Employee> source) {
        return GetEmployeesResponse.builder()
                .content(source)
                .build();
    }
}
