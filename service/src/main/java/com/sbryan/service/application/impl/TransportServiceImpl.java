package com.sbryan.service.application.impl;

import com.sbryan.service.application.TransportService;
import com.sbryan.service.exception.TransportAlreadyExistsException;
import com.sbryan.service.exception.TransportNotFoundException;
import com.sbryan.service.mapper.TransportMapper;
import com.sbryan.service.repository.TransportRepository;
import com.service.deliveryservice.api.adapter.in.http.model.Transport;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransportServiceImpl implements TransportService {
    private final TransportRepository transportRepository;
    private final TransportMapper mapper;
    private static final String TRANSPORT_DELETED = "The transport was deleted successfully";

    @Override
    public Mono<Transport> getTransport(UUID id) {
        return transportRepository.findById(id.toString())
                .switchIfEmpty(Mono.error(new TransportNotFoundException(id)))
                .map(mapper::map);
    }

    @Override
    public Mono<Transport> create(Transport transport) {
        return transportRepository.save(mapper.map(transport).setNew(true))
                .map(mapper::map);
    }

    @Override
    public Mono<Transport> update(Transport transport) {
        return transportRepository.findById(transport.getUuid().toString())
                .switchIfEmpty(Mono.error(new TransportNotFoundException(transport.getUuid())))
                .map(mapper::map)
                .flatMap(t -> transportRepository.save(mapper.map(transport)))
                .map(mapper::map);
    }

    @Override
    public Mono<String> delete(UUID id) {
        return transportRepository.deleteById(id.toString())
                .onErrorResume(ex -> Mono.error(new TransportNotFoundException(id)))
                .thenReturn(TRANSPORT_DELETED);
    }

    @Override
    public Mono<List<Transport>> getAll() {
        return transportRepository.findAll()
                .collectList()
                .map(mapper::map);
    }

}
