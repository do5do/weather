package zerobase.weather.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Weather diary API 명세서",
                description = "날씨 일기 서비스 API 명세서",
                version = "v1"))
@Configuration
public class SwaggerConfig {
}
