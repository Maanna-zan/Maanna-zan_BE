package com.hanghae99.maannazan.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long stationLine;
    private Long stationNum;
    private String stationName;
    private String x;
    private String y;
    private String createDate;

    public Station( String stationName, String createDate, String y, Long stationLine, String x,  Long stationNum) {
        this.stationName = stationName;
        this.stationLine = stationLine;
        this.stationNum = stationNum;
        this.x = x;
        this.y = y;
        this.createDate = createDate;
    }

    public Station(String stationName, String createDate, String y, String x) {
        this.stationName = stationName;
        this.x = x;
        this.y = y;
        this.createDate = createDate;
    }

}