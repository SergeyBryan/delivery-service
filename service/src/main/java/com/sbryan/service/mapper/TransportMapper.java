package com.sbryan.service.mapper;

import com.sbryan.service.entity.TransportEntity;
import com.service.deliveryservice.api.adapter.in.http.model.CreateTransportResponse;
import com.service.deliveryservice.api.adapter.in.http.model.DeleteTransportResponse;
import com.service.deliveryservice.api.adapter.in.http.model.GetAllTransportsResponse;
import com.service.deliveryservice.api.adapter.in.http.model.GetTransportResponse;
import com.service.deliveryservice.api.adapter.in.http.model.Transport;
import com.service.deliveryservice.api.adapter.in.http.model.UpdateTransportResponse;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        imports = UUID.class)
public interface TransportMapper {

    @Mapping(target = "uuid", expression = "java(UUID.fromString(source.getId()))")
    Transport map(TransportEntity source);

    @Mapping(target = "id", expression = "java(source.getUuid().toString())")
    TransportEntity map(Transport source);

    List<Transport> map(List<TransportEntity> source);

    @Mapping(target = "message")
    DeleteTransportResponse map(String message);

    UpdateTransportResponse mapUpdate(Transport transport);

    CreateTransportResponse mapCreate(Transport transport);

    GetTransportResponse mapGet(Transport transport);

    default GetAllTransportsResponse mapAll(List<Transport> source) {
        return GetAllTransportsResponse.builder()
                .content(source)
                .build();
    }
}
