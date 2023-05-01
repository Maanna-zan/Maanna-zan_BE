package com.hanghae99.maannazan.domain.calendar;

import com.hanghae99.maannazan.domain.calendar.dto.CalendarRequestDto;
import com.hanghae99.maannazan.domain.calendar.dto.CalendarResponseDto;
import com.hanghae99.maannazan.domain.calendar.dto.MapRequestDto;
import com.hanghae99.maannazan.domain.calendar.dto.MapResponseDto;
import com.hanghae99.maannazan.global.exception.ResponseMessage;
import com.hanghae99.maannazan.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Calendar", description = "캘린더 관련 API")
@RestController
@RequiredArgsConstructor
public class CalendarController {
 private final CalendarService calendarService;

    @Operation(summary = "캘린더 메모 작성", description = "캘린더 메모 기능")
    @PostMapping("/my-page/calendar")
    public ResponseEntity<ResponseMessage<String>> createCalendarMemo(@RequestBody CalendarRequestDto calendarRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        calendarService.createCalendarMemo(calendarRequestDto, userDetails.getUser());
        return ResponseMessage.SuccessResponse("일정 작성 완료", "");
    }

    @Operation(summary = "캘린더 일정 상세 조회", description = "캘린더 일정 상세 조회")
    @GetMapping("/my-page/calendar/{calendarId}")
    public ResponseEntity<ResponseMessage<CalendarResponseDto>> getCalendarMemo(@PathVariable Long calendarId, @AuthenticationPrincipal UserDetailsImpl userDetails)throws Exception{
        return ResponseMessage.SuccessResponse("일정 상세 조회 완료",calendarService.getCalendarMemo(calendarId, userDetails.getUser()));
    }

    @Operation(summary = "캘린더 일정 전체 조회", description = "캘린더 일정 전체 조회")
    @GetMapping("/my-page/calendarList")
    public ResponseEntity<ResponseMessage<List<CalendarResponseDto>>> getCalendarMemoList(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse("일정 전체 조회 완료",calendarService.getCalendarMemoList(userDetails.getUser()));
    }

    @Operation(summary = "캘린더 일정 업데이트", description = "캘린더 일정 업데이트")
    @PatchMapping("/my-page/calendar/{calendarId}")
    public ResponseEntity<ResponseMessage<String>> updateCalendarMemo(@PathVariable Long calendarId,@RequestBody CalendarRequestDto calendarRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        calendarService.updateCalendarMemo(calendarRequestDto, calendarId,userDetails.getUser());
        return ResponseMessage.SuccessResponse("일정 업데이트 완료","");
    }

    @Operation(summary = "캘린더 일정 삭제", description = "캘린더 일정 삭제")
    @DeleteMapping("/my-page/calendar/{calendarId}")
    public ResponseEntity<ResponseMessage<String>> deleteCalendarMemo(@PathVariable Long calendarId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        calendarService.deleteCalendarMemo(calendarId, userDetails.getUser());
        return ResponseMessage.SuccessResponse("일정 삭제 완료","");
    }

    @Operation(summary = "위치찾기 일정 만들기 작성", description = "위치찾기 일정 만들기 작성")
    @PostMapping("/my-page/schedule")
    public ResponseEntity<ResponseMessage<String>> createCalendarSchedule(@RequestBody MapRequestDto mapRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        calendarService.createCalendarSchedule(mapRequestDto, userDetails.getUser());
        return ResponseMessage.SuccessResponse("일정 만들기 작성 성공","");
    }

    @Operation(summary = "위치찾기 일정 조회", description = "위치찾기 일정 조회")
    @GetMapping("/my-page/scheduleList")
    public ResponseEntity<ResponseMessage<List<MapResponseDto>>> getCalendarSchedules(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse("위치찾기 일정 조회 성공",calendarService.getCalendarSchedules(userDetails.getUser()));

    }

    @Operation(summary = "위치찾기 일정 삭제", description = "위치찾기 일정 삭제")
    @DeleteMapping("/my-page/schedule/{scheduleId}")
    public ResponseEntity<ResponseMessage<String>> deleteCalendarSchedule(@PathVariable Long scheduleId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        calendarService.deleteCalendarSchedule(scheduleId , userDetails.getUser());
        return ResponseMessage.SuccessResponse("위치찾기 일정 삭제 성공","");

    }
}
