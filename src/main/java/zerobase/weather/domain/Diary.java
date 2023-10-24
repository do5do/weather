package zerobase.weather.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

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

    public static Diary of(DateWeather weather,
                           String text) {
        return Diary.builder()
                .weather(weather.getWeather())
                .icon(weather.getIcon())
                .temperature(weather.getTemperature())
                .text(text)
                .date(weather.getDate())
                .build();
    }

    public void updateDiary(String text) {
        this.text = text;
    }
}
