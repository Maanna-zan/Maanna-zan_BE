package com.hanghae99.maannazan.domain.find;



import com.hanghae99.maannazan.domain.entity.Station;
import com.hanghae99.maannazan.domain.find.dto.FindRequestDto;
import com.hanghae99.maannazan.domain.find.dto.FindResponseDto;
import com.hanghae99.maannazan.domain.repository.StationRepository;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
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
//        double x= findRequestDto.getX();   //첫번째 사람
//        double y= findRequestDto.getY();
//        double x2 = findRequestDto.getX2(); // 두번째 사람
//        double y2 = findRequestDto.getY2();
//        double x3 = findRequestDto.getX3(); // 세번째 사람
//        double y3 = findRequestDto.getY3();
//        double x4 = findRequestDto.getX4(); // 네번째 사람
//        double y4 = findRequestDto.getY4();


    double y = findRequestDto.getX();   //첫번째 사람          // 임시로 x랑 y 위치 변경, 프론트에서 반대로 보내주는 중
    double x = findRequestDto.getY();                        // 프론트 수정 되면 이 코드는 다시 삭제 예정
    double y2 = findRequestDto.getX2(); // 두번째 사람
    double x2 = findRequestDto.getY2();
    double y3 = findRequestDto.getX3(); // 세번째 사람
    double x3 = findRequestDto.getY3();
    double y4 = findRequestDto.getX4(); // 네번째 사람
    double x4 = findRequestDto.getY4();

    List<Station> StationList = stationRepository.findAll();
    Station nearestStation = null;
    double shortestDistance = Double.MAX_VALUE;



    //클라이언트에서 값을 보내줄 때 x,x2,x3,x4 순서대로 오는것이 아니었다. 그래서 임시로 조건식을 수정(추후 조건식 수정 필요)
//    if (x == 0.0 || x2 == 0.0) {
    if (x+x2+x3+x4 < 40) {
        throw new CustomException(CustomErrorCode.X1_CAN_NOT_BE_NULL);
    }
    //몇명인지 구분하고 중간 위치 구해서 db에 저장하고 가까운 지하철역 찾아주는 쿼리문
    //클라이언트에서 값을 보내줄 때 x,x2,x3,x4 순서대로 오는것이 아니었다. 그래서 임시로 조건식을 수정(추후 조건식 수정 필요)
//    if (x3 == 0.0 && x4 == 0.0) {   //두명일 때 공식
    if (x+x2+x3+x4 < 80) {   //두명일 때 공식
        double midPointX = (x + x2 + x3 +x4) / 2;
        double midPointY = (y + y2 + y3 + y4) / 2;
        boolean foundNearbyStation = false;


        final int R = 6371; // 지구 반지름 (단위: km)

        //
        for (Station station : StationList) {
            double stationX = Double.parseDouble(station.getX());
            double stationY = Double.parseDouble(station.getY());

            //중간 위치와 지하철 역 사이의 반경 몇키로인지 계산
            double xDistance = Math.toRadians(midPointX - stationX);
            double yDistance = Math.toRadians(midPointY - stationY);
            double a = Math.sin(xDistance / 2) * Math.sin(xDistance / 2)
                    + Math.cos(Math.toRadians(stationX)) * Math.cos(Math.toRadians(midPointX))
                    * Math.sin(yDistance / 2) * Math.sin(yDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double km = R * c; // 단위: km

            if(km<4){ //4키로 내에 지하철역이 있으면 가장 가까운 지하철역을 저장해서 반환해줌!
                foundNearbyStation = true;
                double distance = Math.sqrt(Math.pow(midPointX - stationX, 2) + Math.pow(midPointY - stationY, 2));
                // ((중간x좌표 - 지하철역x값 의 )2제곱  +  (중간y좌표 - 지하철역y값 의 )2제곱) 의 제곱근
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    nearestStation = station;
                }
            }


        }
        // 반경 3km 내에 지하철역이 있으면 해당 역 반환
        if (foundNearbyStation) {
            midPointX = Double.parseDouble(nearestStation.getX());
            midPointY = Double.parseDouble(nearestStation.getY());
            return new FindResponseDto(midPointX, midPointY, nearestStation.getStationName(), nearestStation.getStationLine());
        }
        String message = "주변에 지하철역이 없어 중간지점이 검색되었습니다";
        // 반경 3km 내에 지하철역이 없으면 중간 좌표 반환
        return new FindResponseDto(midPointX, midPointY, message);


//
//            midPointX = Double.parseDouble(nearestStation.getX());
//            midPointY = Double.parseDouble(nearestStation.getY());
//            //
//            return new FindResponseDto(midPointX,midPointY, nearestStation.getStationName(), nearestStation.getStationLine());

        //클라이언트에서 값을 보내줄 때 x,x2,x3,x4 순서대로 오는것이 아니었다. 그래서 임시로 조건식을 수정(추후 조건식 수정 필요)
//    } else if(x4 == 0.0 && y4 == 0.0){        //세명일 때 공식
    } else if(x+x2+x3+x4 < 120){        //세명일 때 공식
            double midPointX = (x + x2 + x3 + x4) / 3;
            double midPointY = (y + y2 + y3 + y4) / 3;
            boolean foundNearbyStation = false;

        final int R = 6371; // 지구 반지름 (단위: km)

        //
        for (Station station : StationList) {
            double stationX = Double.parseDouble(station.getX());
            double stationY = Double.parseDouble(station.getY());

            //중간 위치와 지하철 역 사이의 반경 몇키로인지 계산
            double xDistance = Math.toRadians(midPointX - stationX);
            double yDistance = Math.toRadians(midPointY - stationY);
            double a = Math.sin(xDistance / 2) * Math.sin(xDistance / 2)
                    + Math.cos(Math.toRadians(stationX)) * Math.cos(Math.toRadians(midPointX))
                    * Math.sin(yDistance / 2) * Math.sin(yDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double km = R * c; // 단위: km

            if(km<4){ //4키로 내에 지하철역이 있으면 가장 가까운 지하철역을 저장해서 반환해줌!
                foundNearbyStation = true;
                double distance = Math.sqrt(Math.pow(midPointX - stationX, 2) + Math.pow(midPointY - stationY, 2));
                // ((중간x좌표 - 지하철역x값 의 )2제곱  +  (중간y좌표 - 지하철역y값 의 )2제곱) 의 제곱근
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    nearestStation = station;
                }
            }


        }
        // 반경 3km 내에 지하철역이 있으면 해당 역 반환
        if (foundNearbyStation) {
            midPointX = Double.parseDouble(nearestStation.getX());
            midPointY = Double.parseDouble(nearestStation.getY());
            return new FindResponseDto(midPointX, midPointY, nearestStation.getStationName(), nearestStation.getStationLine());
        }
            String message = "주변에 지하철역이 없어 중간지점이 검색되었습니다";
            // 반경 3km 내에 지하철역이 없으면 중간 좌표 반환
            return new FindResponseDto(midPointX, midPointY, message);
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
            boolean foundNearbyStation = false;





        final int R = 6371; // 지구 반지름 (단위: km)

        //
        for (Station station : StationList) {
            double stationX = Double.parseDouble(station.getX());
            double stationY = Double.parseDouble(station.getY());

            //중간 위치와 지하철 역 사이의 반경 몇키로인지 계산
            double xDistance = Math.toRadians(midPointX - stationX);
            double yDistance = Math.toRadians(midPointY - stationY);
            double a = Math.sin(xDistance / 2) * Math.sin(xDistance / 2)
                    + Math.cos(Math.toRadians(stationX)) * Math.cos(Math.toRadians(midPointX))
                    * Math.sin(yDistance / 2) * Math.sin(yDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double km = R * c; // 단위: km

            if(km<4){ //4키로 내에 지하철역이 있으면 가장 가까운 지하철역을 저장해서 반환해줌!
                foundNearbyStation = true;
                double distance = Math.sqrt(Math.pow(midPointX - stationX, 2) + Math.pow(midPointY - stationY, 2));
                // ((중간x좌표 - 지하철역x값 의 )2제곱  +  (중간y좌표 - 지하철역y값 의 )2제곱) 의 제곱근
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    nearestStation = station;
                }
            }
        }
        // 반경 3km 내에 지하철역이 있으면 해당 역 반환
        if (foundNearbyStation) {
            midPointX = Double.parseDouble(nearestStation.getX());
            midPointY = Double.parseDouble(nearestStation.getY());
            return new FindResponseDto(midPointX, midPointY, nearestStation.getStationName(), nearestStation.getStationLine());
        }
        String message = "주변에 지하철역이 없어 중간지점이 검색되었습니다";
        // 반경 3km 내에 지하철역이 없으면 중간 좌표 반환
        return new FindResponseDto(midPointX, midPointY, message);
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


