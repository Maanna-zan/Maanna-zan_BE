package com.hanghae99.maannazan.domain.search.dto;

import com.hanghae99.maannazan.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchDto {
    private String searchWord;

    
    public SearchDto(String searchWord) {
        this.searchWord = searchWord;
    }
}
