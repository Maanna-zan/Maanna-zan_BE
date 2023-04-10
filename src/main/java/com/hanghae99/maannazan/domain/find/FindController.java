package com.hanghae99.maannazan.domain.find;


import com.hanghae99.maannazan.domain.find.dto.FindRequestDto;
import com.hanghae99.maannazan.domain.find.dto.FindResponseDto;
import com.hanghae99.maannazan.global.exception.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "Find", description = "위치조회 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FindController {

    private final FindService findService;
    @Operation(summary = "getCenterPlace", description = "중간지점 검색")
    @PostMapping("/find")
    public ResponseEntity<ResponseMessage<FindResponseDto>> getCenterPlace(@RequestBody FindRequestDto findRequestDto){
        return  ResponseMessage.SuccessResponse("중간지점 찾기 성공",findService.getCenterPlace(findRequestDto));
    }
}
