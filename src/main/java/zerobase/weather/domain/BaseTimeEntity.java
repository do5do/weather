package zerobase.weather.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@EnableJpaAuditing // 따로 설정을 빼둔 이유는 run application에 있으면 mock 객체 생성 시 실패함
public class BaseTimeEntity {
    @CreatedDate
    LocalDateTime createdDate;

    @LastModifiedDate
    LocalDateTime modifiedDate;
}
