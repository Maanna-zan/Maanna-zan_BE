package com.hanghae99.maannazan.domain.find.dto;

import com.hanghae99.maannazan.domain.entity.Station;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindResponseDto {
    private double lat;
    private double lng;
    private String stationName;
    private Long stationLine;
    private String message = "중간지점과 가까운 지하철 역이 검색되었습니다";

    public FindResponseDto(double lat, double lng, String stationName, Long stationLine) {
        this.lat = lat;
        this.lng = lng;
        this.stationName = stationName;
        this.stationLine = stationLine;
    }

    public FindResponseDto(double lat, double lng, String message) {
        this.lat = lat;
        this.lng = lng;
        this.message = message;
    }
}
