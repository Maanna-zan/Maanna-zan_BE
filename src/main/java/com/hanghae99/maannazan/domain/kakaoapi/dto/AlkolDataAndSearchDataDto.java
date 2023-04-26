package com.hanghae99.maannazan.domain.kakaoapi.dto;

import com.hanghae99.maannazan.domain.search.dto.SearchDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


    @Getter
    @NoArgsConstructor
    public class AlkolDataAndSearchDataDto {


        private List<AlkolResponseDto> alkolResponseDtoList = new ArrayList<>();
        private List<SearchDto> searchDtoList = new ArrayList<>();


        public AlkolDataAndSearchDataDto(List<AlkolResponseDto> alkolResponseDtoList, List<SearchDto> searchDtoList){
            this.alkolResponseDtoList = alkolResponseDtoList;
            this.searchDtoList = searchDtoList;
        }



    }
