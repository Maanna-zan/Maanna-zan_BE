package com.hanghae99.maannazan.domain.calendar;

import com.hanghae99.maannazan.domain.calendar.dto.CalendarRequestDto;
import com.hanghae99.maannazan.domain.calendar.dto.CalendarResponseDto;
import com.hanghae99.maannazan.domain.entity.Calendar;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.repository.CalendarRepository;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    //캘린더 일정 작성
    @Transactional
    public void createCalendarMemo(CalendarRequestDto calendarRequestDto, User user) {
        Calendar calendar = new Calendar(calendarRequestDto, user);
        calendarRepository.save(calendar);
    }

    //캘린더 상세조회
    @Transactional
    public CalendarResponseDto getCalendarMemo(Long calendarId, User user){
        Calendar calendar = calendarRepository.findByUserIdAndId(user.getId(), calendarId);
        return new CalendarResponseDto(calendar);
    }

    //캘린더 전체 조회
    @Transactional
    public List<CalendarResponseDto> getCalendarMemoList(User user) {
        List<Calendar> calendarList = calendarRepository.findByUser(user);
        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();
        for (Calendar calendar : calendarList) {
            calendarResponseDtoList.add(new CalendarResponseDto(calendar));
        }
        return calendarResponseDtoList;
    }

    //캘린더 업데이트
    @Transactional
    public void updateCalendarMemo(CalendarRequestDto calendarRequestDto, Long calendarId,User user) {
        Calendar calendar = calendarRepository.findByUserIdAndId(user.getId(), calendarId);
        calendar.update(calendarRequestDto, user);
    }
    //캘린더 삭제
    @Transactional
    public void deleteCalendarMemo(Long calendarId, User user) {
        Calendar calendar = calendarRepository.findByUserIdAndId(user.getId(), calendarId);
        calendarRepository.delete(calendar);
    }
}
