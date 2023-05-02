package com.hanghae99.maannazan.domain.entity;

import com.hanghae99.maannazan.domain.calendar.dto.CalendarRequestDto;
import com.hanghae99.maannazan.domain.comment.dto.CommentRequestDto;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

    @Entity
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class Calendar {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column
        @Size(min = 1, message = "제목을 입력해주세요.")
        private String title;

        @Column
        @Size(min = 1, message = "내용을 입력해주세요.")
        private String content;

        @Column
        private LocalDate selectedDate;

        @ManyToOne
        @JoinColumn(name = "USER_ID")
        private User user;



        public Calendar(CalendarRequestDto calendarRequestDto, User user) {  //캘린더 일정 작성
            if(calendarRequestDto.getTitle()==null||calendarRequestDto.getTitle().isEmpty())
            {
                throw new CustomException(CustomErrorCode.TITLE_IS_EMPTY);
            }
            if(calendarRequestDto.getContent()==null||calendarRequestDto.getContent().isEmpty())
            {
                throw new CustomException(CustomErrorCode.CONTENT_IS_EMPTY);
            }
            if(calendarRequestDto.getSelectedDate()==null)
            {
                throw new CustomException(CustomErrorCode.DATE_IS_EMPTY);
            }

            this.title = calendarRequestDto.getTitle();
            this.content = calendarRequestDto.getContent();
            this.selectedDate = calendarRequestDto.getSelectedDate();
            this.user = user;
        }




        public void update(CalendarRequestDto calendarRequestDto) {
            this.title = calendarRequestDto.getTitle();
            this.content = calendarRequestDto.getContent();
            this.selectedDate = calendarRequestDto.getSelectedDate();

        }
    }

