package com.hanghae99.maannazan.domain.find.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindResponseDto {
    private double lat;
    private double lng;

    public FindResponseDto(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
