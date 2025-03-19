package com.sbryan.service.api;

import com.sbryan.service.application.impl.DeliveryServiceImpl;
import com.service.deliveryservice.api.adapter.in.http.DeliveryApi;
import com.service.deliveryservice.api.adapter.in.http.model.ReceiveDeliveryRequest;
import com.service.deliveryservice.api.adapter.in.http.model.ReceiveDeliveryResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class DeliveryApis implements DeliveryApi {

    private final DeliveryServiceImpl deliveryService;

    @Override
    public Mono<ReceiveDeliveryResponse> receiveDelivery(Mono<ReceiveDeliveryRequest> request, ServerWebExchange exchange) {
        return request.flatMap(deliveryService::receiveDelivery);
    }
}
