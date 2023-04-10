package com.hanghae99.maannazan.domain.find.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindResponseDto {
    private double midPointX;
    private double midPointY;

    public FindResponseDto(double midPointX, double midPointY) {
        this.midPointX = midPointX;
        this.midPointY = midPointY;
    }
}
