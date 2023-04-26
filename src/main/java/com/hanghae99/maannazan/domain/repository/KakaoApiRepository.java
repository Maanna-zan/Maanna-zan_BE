package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.Kakao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface KakaoApiRepository extends JpaRepository<Kakao, Long> {

//    List<Kakao> findAllByApiId(String apiId); //FIXME 리팩토링하면서 삭제했습니다 동희님

    Page<Kakao> findAll(Pageable pageable);

    Optional<Kakao> findByApiId(String apiId);

    @Query("SELECT DISTINCT k.apiId FROM Kakao k")
    Set<String> findAllApiIds();

    Page<Kakao> findByPlaceNameContainingOrCategoryNameContainingOrAddressNameContainingOrRoadAddressNameContaining(String placeName, String categoryName,String addressName,String roadAddressName, Pageable pageable);
//    Page<Kakao> findByPlaceNameContaining(String placeName, Pageable pageable);
//    Page<Kakao> findByCategoryNameContaining(String placeName, Pageable pageable);
//    Page<Kakao> findByAddressNameContaining(String placeName, Pageable pageable);
//    Page<Kakao> findByRoadAddressNameContaining(String placeName, Pageable pageable);

}
