package com.leralee.apigateway.filter;

import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Adds/propagates traceId for downstream services.
 */
@Component
public class TracingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = exchange.getRequest().getHeaders().getFirst("X-Trace-Id");
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
        }
        MDC.put("traceId", traceId);

        ServerHttpRequest mutated = exchange.getRequest().mutate()
                .header("X-Trace-Id", traceId)
                .build();

        return chain.filter(exchange.mutate().request(mutated).build())
                .doFinally(sig -> MDC.clear());
    }

    @Override
    public int getOrder() {
        return -10;
    }
}
