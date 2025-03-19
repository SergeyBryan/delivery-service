package com.sbryan.service.api;

import com.sbryan.service.application.TransportService;
import com.sbryan.service.mapper.TransportMapper;
import com.service.deliveryservice.api.adapter.in.http.TransportApi;
import com.service.deliveryservice.api.adapter.in.http.model.CreateTransportRequest;
import com.service.deliveryservice.api.adapter.in.http.model.CreateTransportResponse;
import com.service.deliveryservice.api.adapter.in.http.model.DeleteTransportResponse;
import com.service.deliveryservice.api.adapter.in.http.model.GetAllTransportsResponse;
import com.service.deliveryservice.api.adapter.in.http.model.GetTransportResponse;
import com.service.deliveryservice.api.adapter.in.http.model.UpdateTransportRequest;
import com.service.deliveryservice.api.adapter.in.http.model.UpdateTransportResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TransportApis implements TransportApi {

    private final TransportService service;
    private final TransportMapper mapper;

    @Override
    public Mono<CreateTransportResponse> createTransport(
            Mono<CreateTransportRequest> request, ServerWebExchange exchange) {
        return request.flatMap(req -> service.create(req.getTransport()))
                .map(mapper::mapCreate);
    }

    @Override
    public Mono<DeleteTransportResponse> deleteTransport(UUID id, ServerWebExchange exchange) {
        return service.delete(id)
                .map(mapper::map);
    }

    @Override
    public Mono<GetTransportResponse> getTransport(UUID id, ServerWebExchange exchange) {
        return service.getTransport(id)
                .map(mapper::mapGet);
    }

    @Override
    public Mono<GetAllTransportsResponse> getTransports(ServerWebExchange exchange) {
        return service.getAll()
                .map(mapper::mapAll);
    }

    @Override
    public Mono<UpdateTransportResponse> updateTransport(
            UUID id, Mono<UpdateTransportRequest> request, ServerWebExchange exchange) {
        return request.flatMap(req -> {
                    req.getTransport().setUuid(id);
                    return service.update(req.getTransport());
                })
                .map(mapper::mapUpdate);
    }
}
