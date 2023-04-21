package com.hanghae99.maannazan.global.db;


import com.hanghae99.maannazan.domain.entity.Station;
import com.hanghae99.maannazan.domain.repository.StationRepository;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@RestController
@RequiredArgsConstructor
public class StationDB {

    private final StationRepository stationRepository;

    @GetMapping("/api")
    public String load_save(){
        String result = "";

        try {

            URL url = new URL("https://api.odcloud.kr/api/15099316/v1/uddi:cfee6c20-4fee-4c6b-846b-a11c075d0987?page=1&perPage=278&serviceKey=KXQPfp3h8oZY%2BqzWEgjNLSQB%2Flwa%2B8AvUDEwYKRuTWWlF%2FOdDDSl3eMFhT2bpj9az%2BTW89%2FyQfFPAn6TyFTXpQ%3D%3D"
            );

            BufferedReader bf;
            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONArray infoArr = (JSONArray) jsonObject.get("data");
            System.out.println(infoArr);

            for(int i=0;i<infoArr.size();i++){
                JSONObject tmp = (JSONObject)infoArr.get(i);
                Station infoObj=new Station(i+(long)1,
                        (String)tmp.get("역명"),
                        (String)tmp.get("작성일자"),
                        (String)tmp.get("경도"),
                        (Long)tmp.get("호선"),
                        (String) tmp.get("위도"),
                        (Long)tmp.get("고유역번호(외부역코드)"));

                stationRepository.save(infoObj);

            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}


