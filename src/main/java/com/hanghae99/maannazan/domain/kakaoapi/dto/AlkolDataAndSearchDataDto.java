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

        private long TotalElements;


        public AlkolDataAndSearchDataDto(List<AlkolResponseDto> alkolResponseDtoList, List<SearchDto> searchDtoList, long TotalElements){ //user가 null이 아닐 때
            this.alkolResponseDtoList = alkolResponseDtoList;
            this.searchDtoList = searchDtoList;
            this.TotalElements = TotalElements;
        }
        public AlkolDataAndSearchDataDto(List<AlkolResponseDto> alkolResponseDtoList,long TotalElements){    //user가 null 일 때
            this.alkolResponseDtoList = alkolResponseDtoList;
            this.TotalElements = TotalElements;
        }


    }
