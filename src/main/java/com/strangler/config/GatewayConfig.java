package com.strangler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    // Propriedade para definir o uso do novo microserviço
    @Value("${features.use-new-customer-service:true}")
    private boolean useNewCustomerService;
    

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Rota para o serviço "CobrarService"
            .route("customer-service-route", r -> r
                .path("/api/customers/**")
                .filters(f -> f.stripPrefix(1))
                .uri(useNewCustomerService ? "http://localhost:8082" : "http://legacy-system-url")
            )
            // Rota padrão que encaminha demais requisições ao sistema legado
            .route("legacy-route", r -> r
                .path("/**")
                .uri("http://legacy-system-url")
            )
            .build();
    }
}

