package com.hanghae99.maannazan.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae99.maannazan.domain.calendar.dto.MapRequestDto;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "KAKAO_ID")
    private Kakao kakao;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern="yyyyMMdd")
    private LocalDate selectedDate;

    public Schedule(Kakao kakao, User user, LocalDate seselectedDate) {
        if(selectedDate==null){
            throw new CustomException(CustomErrorCode.DATE_IS_NULL);
        }
        this.kakao =kakao;
        this.user = user;
        this.selectedDate = seselectedDate;

    }
}
