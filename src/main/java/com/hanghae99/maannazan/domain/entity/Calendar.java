package com.hanghae99.maannazan.domain.entity;

import com.hanghae99.maannazan.domain.calendar.dto.CalendarRequestDto;
import com.hanghae99.maannazan.domain.comment.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

    @Entity
    @Getter
    @NoArgsConstructor
    public class Calendar {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column
        private String title;

        @Column
        private String content;

        @Column
        private LocalDate selectedDate;

        @ManyToOne
        @JoinColumn(name = "USER_ID")
        private User user;



        public Calendar(CalendarRequestDto calendarRequestDto, User user) {  //캘린더 일정 작성
            this.title = calendarRequestDto.getTitle();
            this.content = calendarRequestDto.getContent();
            this.selectedDate = calendarRequestDto.getSelectedDate();
            this.user = user;
        }


        public void update(CalendarRequestDto calendarRequestDto, User user) {
            this.title = calendarRequestDto.getTitle();
            this.content = calendarRequestDto.getContent();
            this.selectedDate = calendarRequestDto.getSelectedDate();
            this.user = user;
        }
    }

