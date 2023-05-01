package com.hanghae99.maannazan.domain.calendar;

import com.hanghae99.maannazan.domain.calendar.dto.CalendarRequestDto;
import com.hanghae99.maannazan.domain.calendar.dto.CalendarResponseDto;
import com.hanghae99.maannazan.domain.entity.Calendar;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.repository.CalendarRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CalendarServiceTest {

    @InjectMocks
    private CalendarService calendarService;

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private User user;

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase {
        @Test
        @DisplayName("캘린더 작성")
        void createCalendarMemo() {
            //given     //request값
            CalendarRequestDto calendarRequestDto = CalendarRequestDto.builder()
                    .title("제목입니다")
                    .content("내용입니다")
                    .selectedDate(LocalDate.parse("2023-04-30"))
                    .build();
//            String title = calendarRequestDto.getTitle();
//            String content = calendarRequestDto.getContent();
//            LocalDate selectedDate = calendarRequestDto.getSelectedDate();
            Calendar calendar = new Calendar(calendarRequestDto, user);

            //when
            Mockito.when(calendarRepository.save(Mockito.any(Calendar.class))).thenReturn(calendar);
            calendarService.createCalendarMemo(calendarRequestDto, user);
            Mockito.when(calendarRepository.save(calendar)).thenReturn(calendar);
            Calendar saveCalendar = calendarRepository.save(calendar);

            //then
            assertThat(saveCalendar.getTitle()).isEqualTo("제목입니다");
            assertThat(saveCalendar.getContent()).isEqualTo("내용입니다");
            assertThat(saveCalendar.getSelectedDate()).isEqualTo(LocalDate.parse("2023-04-30"));
            assertThat(saveCalendar.getUser()).isEqualTo(user);
        }

        @Test
        @DisplayName("캘린더 상세조회")
        void getCalendarMemo() {
            //given
            Calendar calendar = Calendar.builder()
                    .id(1L)
                    .title("제목입니다")
                    .content("내용입니다")
                    .selectedDate(LocalDate.parse("2023-04-30"))
                    .user(user)
                    .build();
            Long calendarId = 1L;

            CalendarResponseDto calendarResponseDto = new CalendarResponseDto(calendar);

            //when    //이것들을 실행할 때~
            Mockito.when(calendarRepository.findByUserIdAndId(user.getId(), calendarId)).thenReturn(calendar);
            CalendarResponseDto result = calendarService.getCalendarMemo(calendarId,user);

            //then    //실행결과가 이래야한다~
            //entity 확인
            assertThat(calendar).isNotNull();
            assertThat(calendar.getUser()).isNotNull();
            assertThat(calendar.getId()).isNotNull();
            //정상적으로 실행이 됐는지를 확인 (반환 값)
            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo("제목입니다");
            assertThat(result.getContent()).isEqualTo(calendar.getContent());
            assertThat(result.getSelectedDate()).isEqualTo(LocalDate.parse("2023-04-30"));
            //ResponseDto 확인
            assertThat(calendarResponseDto.getTitle()).isEqualTo(result.getTitle());
            assertThat(calendarResponseDto.getContent()).isEqualTo(result.getContent());
            assertThat(calendarResponseDto.getSelectedDate()).isEqualTo(result.getSelectedDate());
            //몇번 실행됐는지
            verify(calendarRepository, times(1)).findByUserIdAndId(user.getId(), calendarId);
            //예외가 없었다면 실행이 됐는지
            assertDoesNotThrow( () -> {
                calendarService.getCalendarMemo(calendarId,user);
            });

        }

        @Test
        @DisplayName("캘린더 전체 조회")
        void getCalendarMemoList() {
            //given


        }

        @Test
        @DisplayName("캘린더 업데이트")
        void updateCalendarMemo() {
            //given
            CalendarRequestDto calendarRequestDto = CalendarRequestDto.builder()
                    .title("수정할 제목입니다")
                    .content("수정할 내용입니다")
                    .selectedDate(LocalDate.parse("2023-04-30"))
                    .build();

            Calendar calendar = Calendar.builder()
                    .id(1L)
                    .title("제목입니다")
                    .content("내용입니다")
                    .selectedDate(LocalDate.parse("2023-05-30"))
                    .user(user)
                    .build();
            Long calendarId = 1L;


            //when
            Mockito.when(calendarRepository.findByUserIdAndId(user.getId(), calendarId)).thenReturn(calendar);
            calendar.update(calendarRequestDto,user);
            calendarService.updateCalendarMemo(calendarRequestDto, calendarId ,user);


            //then    //실행결과가 이래야한다~
            //entity 확인
            assertThat(calendar.getId()).isNotNull();
            assertThat(calendar.getUser()).isNotNull();
            assertThat(calendarRequestDto).isNotNull();
            //정상적으로 실행이 됐는지를 확인 (반환 값)
            assertThat(calendar.getUser()).isEqualTo(user);
            assertThat(calendar.getTitle()).isEqualTo("수정할 제목입니다");
            assertThat(calendar.getContent()).isEqualTo("수정할 내용입니다");
            assertThat(calendar.getSelectedDate()).isEqualTo(LocalDate.parse("2023-04-30"));
            //몇번 실행됐는지
            verify(calendarRepository, times(1)).findByUserIdAndId(user.getId(), calendarId);
            //예외가 없었다면 실행이 됐는지
            assertDoesNotThrow( () -> {
                calendarService.updateCalendarMemo(calendarRequestDto ,calendarId, user);
            });

        }




        @Test
        @DisplayName("캘린더 삭제")
        void deleteCalendarMemo() {
            //given
            Calendar calendar = Calendar.builder()
                    .id(1L)
                    .title("제목입니다")
                    .content("내용입니다")
                    .selectedDate(LocalDate.parse("2023-05-30"))
                    .user(user)
                    .build();
            Long calendarId = 1L;

            //when
            Mockito.when(calendarRepository.findByUserIdAndId(user.getId(), calendarId)).thenReturn(calendar);
            calendarService.deleteCalendarMemo(calendarId ,user);

            //then    //실행결과가 이래야한다~
            verify(calendarRepository, times(1)).findByUserIdAndId(user.getId(), calendarId);
            verify(calendarRepository, times(1)).delete(calendar);

            assertDoesNotThrow( () -> {
                calendarService.deleteCalendarMemo(calendarId, user);
            });
        }
    }
    @Nested
    @DisplayName("실패 케이스")
    class FailCase {
}
}