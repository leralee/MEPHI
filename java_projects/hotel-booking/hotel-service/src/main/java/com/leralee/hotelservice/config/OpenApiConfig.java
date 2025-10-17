package com.leralee.hotelservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Booking Service API")
                        .description("Сервис бронирования отелей и номеров")
                        .version("1.0.0"));
    }
}
