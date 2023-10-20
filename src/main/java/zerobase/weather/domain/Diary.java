package zerobase.weather.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import zerobase.weather.dto.WeatherApiResponse;

import java.time.LocalDate;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Diary extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String weather;
    private String icon;
    private Double temperature;
    private String text;
    private LocalDate date;

    public static Diary of(WeatherApiResponse weatherApiResponse, String text, LocalDate date) {
        return Diary.builder()
                .weather(weatherApiResponse.getWeather().weather())
                .icon(weatherApiResponse.getWeather().icon())
                .temperature(weatherApiResponse.main().temperature())
                .text(text)
                .date(date)
                .build();
    }

    public void updateDiary(String text) {
        this.text = text;
    }
}
