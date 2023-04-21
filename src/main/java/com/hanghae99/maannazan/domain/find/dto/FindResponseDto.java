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

    public FindResponseDto(double lat, double lng, String stationName, Long stationLine) {
        this.lat = lat;
        this.lng = lng;
        this.stationName = stationName;
        this.stationLine = stationLine;
    }


}
