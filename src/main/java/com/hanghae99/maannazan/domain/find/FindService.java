package com.hanghae99.maannazan.domain.find;



import com.hanghae99.maannazan.domain.entity.Station;
import com.hanghae99.maannazan.domain.find.dto.FindRequestDto;
import com.hanghae99.maannazan.domain.find.dto.FindResponseDto;
import com.hanghae99.maannazan.domain.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindService {
    private final StationRepository stationRepository;

@Transactional
//    public FindResponseDto getCenterPlace(double x,double y,double x2,double y2,double x3,double y3,double x4,double y4) {
    public FindResponseDto getCenterPlace(FindRequestDto findRequestDto) {
        double x= findRequestDto.getX();   //첫번째 사람
        double y= findRequestDto.getY();
        double x2 = findRequestDto.getX2(); // 두번째 사람
        double y2 = findRequestDto.getY2();
        double x3 = findRequestDto.getX3(); // 세번째 사람
        double y3 = findRequestDto.getY3();
        double x4 = findRequestDto.getX4(); // 네번째 사람
        double y4 = findRequestDto.getY4();

        List<Station> StationList = stationRepository.findAll();
        Station nearestStation = null;
        double shortestDistance = Double.MAX_VALUE;


        if(x==0.0 || x2==0.0){
            throw new NullPointerException("두명 이상 검색해주세요");
        }
        //몇명인지 구분하고 중간 위치 구해서 db에 저장하고 가까운 지하철역 찾아주는 쿼리문
        if(x3 == 0.0 && x4 == 0.0){   //두명일 때 공식
            double midPointX = (x + x2) / 2;
            double midPointY = (y + y2) / 2;
            //
            for (Station station : StationList) {
                double stationLatitude = Double.parseDouble(station.getX());
                double stationLongitude = Double.parseDouble(station.getY());
                double distance = Math.sqrt(Math.pow(midPointX - stationLatitude, 2) + Math.pow(midPointY - stationLongitude, 2));
                // ((중간x좌표 - 지하철역x값 의 )2제곱  +  (중간y좌표 - 지하철역y값 의 )2제곱) 의 제곱근
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    nearestStation = station;
                }
            }
            midPointX = Double.parseDouble(nearestStation.getX());
            midPointY = Double.parseDouble(nearestStation.getY());
            //
            return new FindResponseDto(midPointX,midPointY);


        } else if(x4 == 0.0 && y4 == 0.0){        //세명일 때 공식
            double midPointX = (x + x2 + x3) / 3;
            double midPointY = (y + y2 + y3) / 3;

            for (Station station : StationList) {
                double stationLatitude = Double.parseDouble(station.getX());
                double stationLongitude = Double.parseDouble(station.getY());
                double distance = Math.sqrt(Math.pow(midPointX - stationLatitude, 2) + Math.pow(midPointY - stationLongitude, 2));
                // ((중간x좌표 - 지하철역x값 의 )2제곱  +  (중간y좌표 - 지하철역y값 의 )2제곱) 의 제곱근
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    nearestStation = station;
                }
            }
            midPointX = Double.parseDouble(nearestStation.getX());
            midPointY = Double.parseDouble(nearestStation.getY());

            return new FindResponseDto(midPointX, midPointY);
        }else{                    //네명일 때 공식    //FIXME   chatGpt 안썼습니다~     주멘 지분 3%
            x = (x+x2+x3)/3;
            y = (y+y2+y3)/3;

            x2 = (x+x3+x4)/3;
            y2 = (y+y3+y4)/3;

            x3 = (x+x2+x4)/3;
            y3 = (y+y2+y4)/3;

            x4 = (x2+x3+x4)/3;
            y4 = (y2+y3+y4)/3;

            double centerX = Math.round(((((x * y2) - (y * x2))* (x3 - x4) - (x - x2) * ((x3 * y4) - (y3 * x4)))
                                /
                              (((x - x2) * (y3-y4)) - ((y - y2) * (x3 - x4))))*100000.0) /100000.0;

            double centerY = Math.round((((((x * y2) - (y * x2)) * (y3 - y4)) - (y - y2) * ((x3 * y4)-(y3 * x4)))
                                /
                               (((x - x2) * (y3 - y4)) - ((y - y2) * (x3 - x4))))*100000.0)/100000.0;

            double centerX2 = Math.round(((((x2 * y3) - (y2 * x3))* (x4 - x) - (x2 - x3) * ((x4 * y) - (y4 * x)))
                    /
                    (((x2 - x3) * (y4-y)) - ((y2 - y3) * (x4 - x))))*100000.0)/100000.0;

            double centerY2 = Math.round((((((x2 * y3) - (y2 * x3)) * (y4 - y)) - (y2 - y3) * ((x4 * y)-(y4 * x)))
                    /
                    (((x2 - x3) * (y4 - y)) - ((y2 - y3) * (x4 - x))))*100000.0)/100000.0;

            double centerX3 = Math.round((((((x3 * y4) - (y3 * x4))* (x - x2) - (x3 - x4) * ((x * y2) - (y * x2)))
                    /
                    (((x3 - x4) * (y-y2)) - ((y3 - y4) * (x - x2)))) + 0.0001)*100000.0)/100000.0;

            double centerY3 = Math.round(((((((x3 * y4) - (y3 * x4)) * (y - y2)) - (y3 - y4) * ((x * y2)-(y * x2)))
                    /
                    (((x3 - x4) * (y - y2)) - ((y3 - y4) * (x - x2)))) + 0.0001)*100000.0)/100000.0;

            double centerX4 = Math.round((((((x4 * y) - (y4 * x))* (x2 - x3) - (x4 - x) * ((x2 * y3) - (y2 * x3)))
                    /
                    (((x4 - x) * (y2-y3)) - ((y4 - y) * (x2 - x3)))) + 0.0001)*100000.0)/100000.0;

            double centerY4 = Math.round(((((((x4 * y) - (y4 * x)) * (y2 - y3)) - (y4 - y) * ((x2 * y3)-(y2 * x3)))
                    /
                    (((x4 - x) * (y2 - y3)) - ((y4 - y) * (x2 - x3)))) + 0.0001)*100000.0)/100000.0;

            System.out.println(centerX);
            System.out.println(centerY);
            System.out.println(centerX2);
            System.out.println(centerY2);
            System.out.println(centerX3);
            System.out.println(centerY3);
            System.out.println(centerX4);
            System.out.println(centerY4);


//           //FIXME 식1
//            x = (centerX+centerX2+centerX3)/3;
//            y = (centerY+centerY2+centerY3)/3;
//
//            x2 = Math.round(((centerX+centerX3+centerX4)/3)*10000.0)/10000.0;
//            y2 = Math.round(((centerY+centerY3+centerY4)/3)*10000.0)/10000.0;
//
//            x3 = Math.round(((centerX+centerX2+centerX4)/3)*10000.0)/10000.0;
//            y3 = Math.round(((centerY+centerY2+centerY4)/3)*10000.0)/10000.0;
//
//            x4 = (centerX2+centerX3+centerX4)/3;
//            y4 = (centerY2+centerY3+centerY4)/3;
//
//            System.out.println(x);
//            System.out.println(y);
//            System.out.println(x2);
//            System.out.println(y2);
//            System.out.println(x3);
//            System.out.println(y3);
//            System.out.println(x4);
//            System.out.println(y4);
//
//            double midPointX = (((x * y2) - (y * x2))* (x3 - x4) - (x - x2) * ((x3 * y4) - (y3 * x4)))
//                    /
//                    (((x - x2) * (y3-y4)) - ((y - y2) * (x3 - x4)));
//
//            double midPointY = ((((x * y2) - (y * x2)) * (y3 - y4)) - (y - y2) * ((x3 * y4)-(y3 * x4)))
//                    /
//                    (((x - x2) * (y3 - y4)) - ((y - y2) * (x3 - x4)));



            //FIXME 식2
//            double midPointX = (((centerX * centerY2) - (centerY * centerX2))* (centerX3 - centerX4) - (centerX - centerX2) * ((centerX3 * centerY4) - (centerY3 * centerX4)))
//                    /
//                    (((centerX - centerX2) * (centerY3-centerY4)) - ((centerY - centerY2) * (centerX3 - centerX4)));
//
//            double midPointY = ((((centerX * centerY2) - (centerY * centerX2)) * (centerY3 - centerY4)) - (centerY - centerY2) * ((centerX3 * centerY4)-(centerY3 * centerX4)))
//                    /
//                    (((centerX - centerX2) * (centerY3 - centerY4)) - ((centerY - centerY2) * (centerX3 - centerX4)));


            //FIXME 식3
            double midPointX = (centerX + centerX2 + centerX3 + centerX4) /4;
            double midPointY = (centerY + centerY2 + centerY3 + centerY4) /4;
            System.out.println(midPointX);
            System.out.println(midPointY);

            


            for (Station station : StationList) {
                double stationLatitude = Double.parseDouble(station.getX());
                double stationLongitude = Double.parseDouble(station.getY());
                double distance = Math.sqrt(Math.pow(midPointX - stationLatitude, 2) + Math.pow(midPointY - stationLongitude, 2));
                // ((중간x좌표 - 지하철역x값 의 )2제곱  +  (중간y좌표 - 지하철역y값 의 )2제곱) 의 제곱근
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    nearestStation = station;
                }
            }
            midPointX = Double.parseDouble(nearestStation.getX());
            midPointY = Double.parseDouble(nearestStation.getY());

            return new FindResponseDto(midPointX,midPointY);
            }

        }
    }






//    public SubwayStation findNearestSubwayStation(double midPointX, double midPointY) {
//        List<Station> StationList = stationRepository.findAll();
//        Station nearestStation = null;
//        double shortestDistance = Double.MAX_VALUE;
//
//        for (Station station : StationList) {
//            double stationLatitude = Double.parseDouble(station.getX());
//            double stationLongitude = Double.parseDouble(station.getY());
//            double distance = Math.sqrt(Math.pow(midPointX - stationLatitude, 2) + Math.pow(midPointY - stationLongitude, 2));
//            // ((중간x좌표 - 지하철역x값 의 )2제곱  +  (중간y좌표 - 지하철역y값 의 )2제곱) 의 제곱근
//            if (distance < shortestDistance) {
//                shortestDistance = distance;
//                nearestStation = station;
//            }
//        }
//
//        return nearestStation;
//    }


