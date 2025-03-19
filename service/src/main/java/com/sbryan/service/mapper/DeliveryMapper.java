package com.sbryan.service.mapper;

import com.sbryan.service.entity.DeliveryEntity;
import com.sbryan.service.entity.ItemDeliveryLink;
import com.service.deliveryservice.api.adapter.in.http.model.ReceiveDeliveryRequest;
import com.service.deliveryservice.api.adapter.in.http.model.ReceiveDeliveryResponse;
import com.service.deliveryservice.api.adapter.in.http.model.Transport;
import java.time.LocalDate;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        imports = {UUID.class, LocalDate.class})
public interface DeliveryMapper {

    @Mapping(target = "qty", source = "qty")
    @Mapping(target = "transportId", expression = "java(transport.getUuid().toString())")
    @Mapping(target = "uuid", expression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "username", source = "username",
            defaultValue = "User is not registered")
    @Mapping(target = "createdDate", expression = "java(LocalDate.now())")
    DeliveryEntity map(Long qty,
                       String username,
                       Transport transport,
                       ReceiveDeliveryRequest request);

    @Mapping(target = "id", expression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "deliveryId", source = "uuid")
    @Mapping(target = "itemId", source = "id")
    @Mapping(target = "quantity", source = "qty")
    ItemDeliveryLink map(String uuid, Long qty, String id);

    @Mapping(target = "createdDate", expression = "java(source.getCreatedDate().toString())")
    @Mapping(target = "quantity", source = "qty")
    @Mapping(target = "acceptedBy", source = "username")
    ReceiveDeliveryResponse map(DeliveryEntity source);
}
