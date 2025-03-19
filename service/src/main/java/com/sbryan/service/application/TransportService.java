package com.sbryan.service.application;

import com.service.deliveryservice.api.adapter.in.http.model.Transport;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface TransportService {
    Mono<Transport> getTransport(UUID id);

    Mono<Transport> create(Transport transport);

    Mono<Transport> update(Transport transport);

    Mono<String> delete(UUID id);

    Mono<List<Transport>> getAll();
}
