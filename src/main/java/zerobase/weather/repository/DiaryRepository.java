package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findAllByDate(LocalDate date);
    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<Diary> findFirstByDate(LocalDate date);

    void deleteAllByDate(LocalDate date);
}
