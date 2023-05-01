package com.hanghae99.maannazan.domain.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae99.maannazan.domain.entity.Schedule;
import com.hanghae99.maannazan.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Getter
@NoArgsConstructor
public class MapResponseDto {
    private String apiId;
    private String address_name;

    private String category_group_code;

    private String category_group_name;

    private String category_name;

    private String distance;

    private String phone;

    private String place_name;

    private String place_url;

    private String road_address_name;

    private String x;

    private String y;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern="yyyyMMdd")
    private LocalDate selectedDate;

    public MapResponseDto(Schedule schedule) {
        this.apiId = schedule.getKakao().getApiId();
        this.address_name = schedule.getKakao().getAddressName();
        this.category_group_code = schedule.getKakao().getCategoryGroupCode();
        this.category_group_name = schedule.getKakao().getCategoryGroupName();
        this.category_name = schedule.getKakao().getCategoryName();
        this.distance = schedule.getKakao().getDistance();
        this.phone = schedule.getKakao().getPhone();
        this.place_name = schedule.getKakao().getPlaceName();
        this.place_url = schedule.getKakao().getPlaceUrl();
        this.road_address_name = schedule.getKakao().getRoadAddressName();
        this.x = schedule.getKakao().getX();
        this.y = schedule.getKakao().getY();
        this.selectedDate = schedule.getSelectedDate();
    }
}
