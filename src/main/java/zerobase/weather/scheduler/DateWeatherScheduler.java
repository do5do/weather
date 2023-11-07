package zerobase.weather.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.openapi.WeatherScrapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class DateWeatherScheduler {
    private final WeatherScrapper weatherScrapper;
    private final DateWeatherRepository dateWeatherRepository;

    @Scheduled(cron = "0 0 1 * * *")
    public void saveDateWeather() {
        dateWeatherRepository.save(
                DateWeather.of(weatherScrapper.getWeather()));
        log.info("Save DateWeather successful");
    }
}
