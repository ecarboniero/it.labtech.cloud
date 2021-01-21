package it.labtech.cloud.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class BasepathGlobalFilter implements GlobalFilter, Ordered {
    private static final Logger _LOGGER = LoggerFactory.getLogger(BasepathGlobalFilter.class);

    @Value("${spring.webflux.base-path}")
    private String basePath;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        _LOGGER.info("BasepathGlobalFilter start");
        ServerHttpRequest req = exchange.getRequest();
        String path = req.getURI().getRawPath();
        String newPath = path.replaceFirst(basePath, "/");

        ServerHttpRequest request = req.mutate().path(newPath).contextPath(null).build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return 9999;
    }
}
