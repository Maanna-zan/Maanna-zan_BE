package com.hanghae99.maannazan.domain.kakaoapi;

import com.hanghae99.maannazan.domain.entity.*;
import com.hanghae99.maannazan.domain.kakaoapi.dto.AlkolDataAndSearchDataDto;
import com.hanghae99.maannazan.domain.kakaoapi.dto.AlkolResponseDto;
import com.hanghae99.maannazan.domain.kakaoapi.dto.KakaoResponseDto;
import com.hanghae99.maannazan.domain.like.LikeService;
import com.hanghae99.maannazan.domain.post.PostService;
import com.hanghae99.maannazan.domain.post.dto.PostImageResponseDto;
import com.hanghae99.maannazan.domain.post.dto.PostResponseDto;
import com.hanghae99.maannazan.domain.repository.KakaoApiRepository;
import com.hanghae99.maannazan.domain.repository.SearchRepository;
import com.hanghae99.maannazan.domain.search.dto.SearchDto;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KakaoApiService {
    private final KakaoApiRepository kakaoApiRepository;

    private final PostService postService;

    private final LikeService likeService;
    private final SearchRepository searchRepository;



    //카카오 검색 api 저장
    public void apiSave(Map<String, Object> body) {
        List<Map<String, Object>> documents = (List<Map<String, Object>>) body.get("documents");
        Set<String> existingApiIds = kakaoApiRepository.findAllApiIds();
        List<Kakao> kakaos = new ArrayList<>();
        for (Map<String, Object> document : documents) {
            String apiId = document.get("id").toString();
            if (existingApiIds.contains(apiId)){
                continue;
            }
            Kakao kakao = new Kakao();
            kakao.setAddressName(document.get("address_name").toString());
            kakao.setCategoryGroupCode(document.get("category_group_code").toString());
            kakao.setCategoryGroupName(document.get("category_group_name").toString());
            kakao.setCategoryName(document.get("category_name").toString());
            kakao.setDistance(document.get("distance").toString());
            kakao.setApiId(document.get("id").toString());
            kakao.setPhone(document.get("phone").toString());
            kakao.setPlaceName(document.get("place_name").toString());
            kakao.setPlaceUrl(document.get("place_url").toString());
            kakao.setRoadAddressName(document.get("road_address_name").toString());
            kakao.setX(document.get("x").toString());
            kakao.setY(document.get("y").toString());
            kakaos.add(kakao);
            existingApiIds.add(apiId);
        }
        kakaoApiRepository.saveAll(kakaos);
    }

    // 상세 술집 페이지
    @Transactional
    public List<KakaoResponseDto> getAlkol(String apiId, User user) {
        Kakao kakaoView = getAlkolByKakaoApiId(apiId);
        kakaoView.roomViewCount(kakaoView.getRoomViewCount()+1);
        List<KakaoResponseDto> kakaoResponseDtoList = new ArrayList<>();
        boolean roomLike = likeService.getAlkolLike(apiId, user);
        double tasteAvg = postService.getTasteAvg(kakaoView);
        double serviceAvg = postService.getServiceAvg(kakaoView);
        double atmosphereAvg = postService.getAtmosphereAvg(kakaoView);
        double satisfactionAvg = postService.getSatisfactionAvg(kakaoView);
        List<Post> posts = postService.getPostByKakaoApiId(kakaoView);
        int numberOfPosts = posts.size();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for (Post post : posts) {
            if (user != null) {
                boolean like = likeService.getPostLike(post, user);
                postResponseDtoList.add(new PostResponseDto(post, like));

            } else {
                postResponseDtoList.add(new PostResponseDto(post));
            }
        }
        kakaoResponseDtoList.add(new KakaoResponseDto(kakaoView, postResponseDtoList, numberOfPosts, roomLike, tasteAvg, serviceAvg, atmosphereAvg, satisfactionAvg));
        return kakaoResponseDtoList;

    }


    // 모든 술집 조회
//    public AlkolDataAndSearchDataDto getAllAlkol(String placeName,String categoryName,String addressName,String roadAddressName,User user, int page, int size) {
//
//        if (placeName != null) {
//            Pageable pageable = PageRequest.of(page, size);
//            Page<Kakao> kakaoSearchList = kakaoApiRepository.findByPlaceNameContainingOrCategoryNameContainingOrAddressNameContainingOrRoadAddressNameContaining(placeName, categoryName, addressName, roadAddressName, pageable);
//            if (kakaoSearchList == null) {
//                throw new CustomException(CustomErrorCode.ALKOL_NOT_FOUND);
//            }
//            List<Kakao> entityList = kakaoSearchList.getContent();
//            List<AlkolResponseDto> AlkolResponseDtoList = new ArrayList<>();
//            for (Kakao kakao : entityList) {
//                boolean roomLike = likeService.getAlkolLike(kakao.getApiId(), user);
//                List<Post> posts = postService.getPostByKakaoApiId(kakao);
//                List<PostImageResponseDto> postImageResponseDtoList = new ArrayList<>();
//                for (Post post : posts) {
//                    postImageResponseDtoList.add(new PostImageResponseDto(post));
//                }
//                AlkolResponseDtoList.add(new AlkolResponseDto(kakao, roomLike, postImageResponseDtoList));
//            }
//
//            //검색 결과 저장
//            if (user != null) {
//                List<Search> searchList = searchRepository.findByUserId(user.getId());
//                if ((searchList == null || searchList.size() < 5) && user != null) {
//                    Search search = new Search(placeName, user);
//                    searchRepository.save(search);
//
//                    List<SearchDto> searchDtoList = new ArrayList<>();
//                    for (Search searchWord : searchList) { //User정보까지 보내주기에 Dto에 searchWord만 저장해서 보내줌
//                        searchDtoList.add(new SearchDto(searchWord.getSearchWord()));
//                    }
//                    return new AlkolDataAndSearchDataDto(AlkolResponseDtoList, searchDtoList);
//
//                } else if (searchList.size() == 5 && user != null) { //검색어가 5개일 시 가장 오래 된 검색어 삭제
//                    searchRepository.delete(searchList.get(0));
//                    Search search = new Search(placeName, user);
//                    searchRepository.save(search);
//
//                    List<SearchDto> searchDtoList = new ArrayList<>();
//                    for (Search searchWord : searchList) { //User정보까지 보내주기에 Dto에 searchWord만 저장해서 보내줌
//                        searchDtoList.add(new SearchDto(searchWord.getSearchWord()));
//                    }
//                    return new AlkolDataAndSearchDataDto(AlkolResponseDtoList, searchDtoList);
//                }
//            }
//            return new AlkolDataAndSearchDataDto(AlkolResponseDtoList);
//
//        } else {
//            Pageable pageable = PageRequest.of(page, size);
//            Page<Kakao> entityPage = kakaoApiRepository.findAll(pageable);
//            List<Kakao> entityList = entityPage.getContent();
//            List<AlkolResponseDto> AlkolResponseDtoList = new ArrayList<>();
//            for (Kakao kakao : entityList) {
//                boolean roomLike = likeService.getAlkolLike(kakao.getApiId(), user);
//                List<Post> posts = postService.getPostByKakaoApiId(kakao);
//                List<PostImageResponseDto> postImageResponseDtoList = new ArrayList<>();
//                for (Post post : posts) {
//                    postImageResponseDtoList.add(new PostImageResponseDto(post));
//                }
//                AlkolResponseDtoList.add(new AlkolResponseDto(kakao, roomLike, postImageResponseDtoList));
//            }
//            if (user != null) {
//                //검색 결과 저장
//                List<Search> searchList = searchRepository.findByUserId(user.getId());
//                List<SearchDto> searchDtoList = new ArrayList<>();
//                for (Search search : searchList) {    //User정보까지 보내주기에 Dto에 searchWord만 저장해서 보내줌
//                    searchDtoList.add(new SearchDto(search.getSearchWord()));
//                }
//                return new AlkolDataAndSearchDataDto(AlkolResponseDtoList, searchDtoList);
//            }
//            return new AlkolDataAndSearchDataDto(AlkolResponseDtoList);
//        }
//    }

    public AlkolDataAndSearchDataDto getAllAlkol(String placeName,String categoryName,String addressName,String roadAddressName,User user, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Kakao> kakaoSearchList;
        if (placeName != null) {
            kakaoSearchList = kakaoApiRepository.findByPlaceNameContainingOrCategoryNameContainingOrAddressNameContainingOrRoadAddressNameContaining(placeName, categoryName, addressName, roadAddressName, pageable);
            if (kakaoSearchList == null) {
                throw new CustomException(CustomErrorCode.ALKOL_NOT_FOUND);
            }
        } else {
            kakaoSearchList = kakaoApiRepository.findAll(pageable);
        }

        List<Kakao> entityList = kakaoSearchList.getContent();
        List<AlkolResponseDto> AlkolResponseDtoList = new ArrayList<>();
        for (Kakao kakao : entityList) {
            boolean roomLike = likeService.getAlkolLike(kakao.getApiId(), user);
            List<Post> posts = postService.getPostByKakaoApiId(kakao);
            List<PostImageResponseDto> postImageResponseDtoList = new ArrayList<>();
            for (Post post : posts) {
                postImageResponseDtoList.add(new PostImageResponseDto(post));
            }
            AlkolResponseDtoList.add(new AlkolResponseDto(kakao, roomLike, postImageResponseDtoList));
        }

        // 검색 결과 저장
        List<SearchDto> searchDtoList = null;
        if (user != null) {
            List<Search> searchList = searchRepository.findByUserId(user.getId());
            if (searchList == null || searchList.size() < 5) {
                Search search = new Search(placeName, user);
                searchRepository.save(search);
            } else if (searchList.size() == 5) { // 검색어가 5개일 시 가장 오래 된 검색어 삭제
                searchRepository.delete(searchList.get(0));
                Search search = new Search(placeName, user);
                searchRepository.save(search);
            }
            searchDtoList = searchList.stream().map(search -> new SearchDto(search.getSearchWord())).collect(Collectors.toList());
        }

        return new AlkolDataAndSearchDataDto(AlkolResponseDtoList, searchDtoList, kakaoSearchList.getTotalElements());
    }




    //    게시물 많은 순으로 술집 조회
    @Transactional
    public AlkolDataAndSearchDataDto getBestAlkol(String placeName,String categoryName,String addressName,String roadAddressName,User user, int page, int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "numberOfPosts"));
        return getAlkolResponseDtos(placeName,categoryName,addressName,roadAddressName,user, pageable);
    }

    //조회수 많은 순으로 술집 조회
    @Transactional
    public AlkolDataAndSearchDataDto getViewAlkol(String placeName,String categoryName,String addressName,String roadAddressName,User user, int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "roomViewCount"));
        return getAlkolResponseDtos(placeName,categoryName,addressName,roadAddressName,user, pageable);
    }

    //좋아요 많은 순으로 술집 조회
    public AlkolDataAndSearchDataDto getLikeAlkol(String placeName,String categoryName,String addressName,String roadAddressName,User user, int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "roomLikecnt"));
        return getAlkolResponseDtos(placeName,categoryName,addressName,roadAddressName,user, pageable);
    }

    // 공통 부분 메서드화
    private AlkolDataAndSearchDataDto getAlkolResponseDtos(String placeName,String categoryName,String addressName,String roadAddressName, User user, Pageable pageable) {
        if (placeName != null) {
            Page<Kakao> kakaoSearchList = kakaoApiRepository.findByPlaceNameContainingOrCategoryNameContainingOrAddressNameContainingOrRoadAddressNameContaining(placeName, categoryName, addressName, roadAddressName, pageable);
            if (kakaoSearchList == null) {
                throw new CustomException(CustomErrorCode.ALKOL_NOT_FOUND);
            }
            List<Kakao> entityList = kakaoSearchList.getContent();
            List<AlkolResponseDto> AlkolResponseDtoList = new ArrayList<>();
            for (Kakao kakao : entityList) {
                List<Post> posts = postService.getPostByKakaoApiId(kakao);
                List<PostImageResponseDto> postImageResponseDtoList = new ArrayList<>();
                for (Post post : posts) {
                    postImageResponseDtoList.add(new PostImageResponseDto(post));
                }
                int numberOfPosts = posts.size();
                boolean roomLike = likeService.getAlkolLike(kakao.getApiId(), user);
                AlkolResponseDtoList.add(new AlkolResponseDto(kakao, numberOfPosts, roomLike, postImageResponseDtoList));
            }
            //검색 결과 저장
            if (user != null) {
                List<Search> searchList = searchRepository.findByUserId(user.getId());
                if ((searchList == null || searchList.size() < 5) && user != null) {
                    Search search = new Search(placeName, user);
                    searchRepository.save(search);

                    List<SearchDto> searchDtoList = new ArrayList<>();
                    for (Search searchWord : searchList) { //User정보까지 보내주기에 Dto에 searchWord만 저장해서 보내줌
                        searchDtoList.add(new SearchDto(searchWord.getSearchWord()));
                    }
                    return new AlkolDataAndSearchDataDto(AlkolResponseDtoList, searchDtoList, kakaoSearchList.getTotalElements());
                } else if (searchList.size() == 5 && user != null) {
                    searchRepository.delete(searchList.get(0));
                    Search search = new Search(placeName, user);
                    searchRepository.save(search);

                    List<SearchDto> searchDtoList = new ArrayList<>();
                    for (Search searchWord : searchList) { //User정보까지 보내주기에 Dto에 searchWord만 저장해서 보내줌
                        searchDtoList.add(new SearchDto(searchWord.getSearchWord()));
                    }
                    return new AlkolDataAndSearchDataDto(AlkolResponseDtoList, searchDtoList, kakaoSearchList.getTotalElements());
                }

            }return new AlkolDataAndSearchDataDto(AlkolResponseDtoList, kakaoSearchList.getTotalElements());

        }
        Page<Kakao> entityPage = kakaoApiRepository.findAll(pageable);
        List<Kakao> entityList = entityPage.getContent();
        List<AlkolResponseDto> AlkolResponseDtoList = new ArrayList<>();
        for (Kakao kakao : entityList) {
            List<Post> posts = postService.getPostByKakaoApiId(kakao);
            List<PostImageResponseDto> postImageResponseDtoList = new ArrayList<>();
            for (Post post : posts) {
                postImageResponseDtoList.add(new PostImageResponseDto(post));
            }
            int numberOfPosts = posts.size();
            boolean roomLike = likeService.getAlkolLike(kakao.getApiId(), user);
            AlkolResponseDtoList.add(new AlkolResponseDto(kakao, numberOfPosts, roomLike, postImageResponseDtoList));
        }

        if (user != null) {
            //검색 결과 저장
            List<Search> searchList = searchRepository.findByUserId(user.getId());
            List<SearchDto> searchDtoList = new ArrayList<>();
            for (Search search : searchList) {    //User정보까지 보내주기에 Dto에 searchWord만 저장해서 보내줌
                searchDtoList.add(new SearchDto(search.getSearchWord()));
            }
            return new AlkolDataAndSearchDataDto(AlkolResponseDtoList, searchDtoList, entityPage.getTotalElements());
        }
        return new AlkolDataAndSearchDataDto(AlkolResponseDtoList, entityPage.getTotalElements());
    }



    //메서드
    public Kakao getAlkolByKakaoApiId(String kakaoApiId){    // 단일 술집 조회(상세조회)
        return kakaoApiRepository.findByApiId(kakaoApiId).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));
    }

    public Page<Kakao> getAlkolList(String apiId,Pageable pageable) {    //모든 술집 조회(페이징 처리)
        return kakaoApiRepository.findAllByApiId(apiId, pageable);
    }


    public void deleteAlkol(String apiId, User user) {
        Kakao kakao = getAlkolByKakaoApiId(apiId);
        List<Post> postList = postService.getPostByKakaoApiId(kakao);
        for (Post post : postList){
            postService.deletePost(post.getId(), user);
        }
        KakaoLikes likes = likeService.getAlkolLikes(kakao,user);
        if (likes != null){
            likeService.deleteLikes(likes);
        }
        kakaoApiRepository.delete(kakao);
    }
}
