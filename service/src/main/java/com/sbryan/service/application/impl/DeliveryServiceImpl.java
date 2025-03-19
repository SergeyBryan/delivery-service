package com.sbryan.service.application.impl;

import com.sbryan.service.exception.ItemNotFoundException;
import com.sbryan.service.mapper.DeliveryMapper;
import com.sbryan.service.repository.DeliveryRepository;
import com.sbryan.service.repository.link.ItemDeliveryLinkRepository;
import com.service.deliveryservice.api.adapter.in.http.model.Item;
import com.service.deliveryservice.api.adapter.in.http.model.ReceiveDeliveryRequest;
import com.service.deliveryservice.api.adapter.in.http.model.ReceiveDeliveryResponse;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl {
    private final ItemServiceImpl itemService;

    private final DeliveryMapper mapper;
    private final DeliveryRepository deliveryRepository;

    private final ItemDeliveryLinkRepository itemDeliveryLinkRepository;
    private final TransportServiceImpl transportService;

    public Mono<ReceiveDeliveryResponse> receiveDelivery(ReceiveDeliveryRequest request) {
        var trans = transportService.create(request.getTransport());
        var qty = Mono.just(request.getItems().stream()
                .mapToLong(Item::getPcs)
                .sum());
        var items = request.getItems().stream()
                .map(p -> itemService.updateItem(p.getId(), p)
                        .onErrorResume(ex -> {
                            if (ex instanceof ItemNotFoundException) {
                                return itemService.create(Mono.just(p));
                            }
                            return Mono.error(ex);
                        })
                        .map(Item::getId))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Flux::merge))
                .collectList();

        return Mono.zip(trans, getUsername(), qty, items)
                .flatMap(tuple -> {
                    var transport = tuple.getT1();
                    var name = tuple.getT2();
                    var quantity = tuple.getT3();

                    return deliveryRepository.save(mapper.map(quantity, name, transport, request).setNew(true))
                            .flatMap(delivery -> Flux.fromIterable(request.getItems())
                                    .flatMap(item -> itemDeliveryLinkRepository.save(mapper.map(
                                            delivery.getUuid(), item.getPcs().longValue(), String.valueOf(item.getId()))))
                                    .collectList()
                                    .thenReturn(mapper.map(delivery)));
                });
    }

    private Mono<String> getUsername() {
        var user = (UserDetails) Optional.of(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .orElse(null);
        return Mono.just(Optional.ofNullable(user)
                .map(UserDetails::getUsername)
                .orElse("User is not registered"));
    }

}
