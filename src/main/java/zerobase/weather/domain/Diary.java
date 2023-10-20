package zerobase.weather.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zerobase.weather.dto.WeatherApiResponse;

import java.time.LocalDate;

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

    @Builder
    public Diary(Long id, String weather, String icon, Double temperature, String text, LocalDate date) {
        this.id = id;
        this.weather = weather;
        this.icon = icon;
        this.temperature = temperature;
        this.text = text;
        this.date = date;
    }

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
