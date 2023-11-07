package zerobase.weather.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import zerobase.weather.dto.WeatherApiResponse;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class DateWeather extends BaseTimeEntity {
    @Id
    private LocalDate date;
    private String weather;
    private String icon;
    private Double temperature;

    public static DateWeather of(WeatherApiResponse weatherResponse) {
        return DateWeather.builder()
                .date(LocalDate.now())
                .weather(weatherResponse.getWeather().weather())
                .icon(weatherResponse.getWeather().icon())
                .temperature(weatherResponse.main().temperature())
                .build();
    }
}
