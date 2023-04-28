package com.hanghae99.maannazan.domain.user;

import com.hanghae99.maannazan.domain.comment.CommentService;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.like.LikeService;
import com.hanghae99.maannazan.domain.post.PostService;
import com.hanghae99.maannazan.domain.repository.RefreshTokenRepository;
import com.hanghae99.maannazan.domain.repository.UserRepository;
import com.hanghae99.maannazan.domain.user.dto.LoginRequestDto;
import com.hanghae99.maannazan.domain.user.dto.SignupRequestDto;
import com.hanghae99.maannazan.global.exception.CustomException;
import com.hanghae99.maannazan.global.jwt.JwtUtil;
import com.hanghae99.maannazan.global.jwt.TokenDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostService postService;
    @Mock
    private CommentService commentService;
    @Mock
    private LikeService likeService;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private JavaMailSender mailSender;
    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase{
        @DisplayName("회원가입")
        @Test
        void signup() {
            //given
            SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                    .userName("장동희")
                    .nickName("장동희")
                    .phoneNumber("01020948737")
                    .email("ehdehdrnt123@naver.com")
                    .password("ehd12ehd12!@")
                    .birth("19980630")
                    .build();
            String userName = signupRequestDto.getUserName();
            String nickName = signupRequestDto.getNickName();
            String email = signupRequestDto.getEmail();
            String phoneNumber = signupRequestDto.getPhoneNumber();
            Mockito.when(passwordEncoder.encode(anyString())).thenReturn(signupRequestDto.getPassword());
            String password = passwordEncoder.encode(signupRequestDto.getPassword());
            String birth = signupRequestDto.getBirth();
            User user = new User(userName, nickName, email, phoneNumber, password, birth);
            Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

            //when
            userService.signup(signupRequestDto);
            Mockito.when(userRepository.findByUserName("장동희")).thenReturn(user);
            User saveUser = userRepository.findByUserName("장동희");

            //then
            assertThat(saveUser.getNickName()).isEqualTo("장동희");
            assertThat(saveUser.getUserName()).isEqualTo("장동희");
            assertThat(saveUser.getEmail()).isEqualTo("ehdehdrnt123@naver.com");
            assertThat(saveUser.getBirth()).isEqualTo("19980630");
            assertThat(saveUser.getPhoneNumber()).isEqualTo("01020948737");
            assertThat(saveUser.getPassword()).isEqualTo("ehd12ehd12!@");


        }
        @DisplayName("로그인")
        @Test
        void login() {
            Map<String, String> headers = new HashMap<>();
            HttpServletResponse response = mock(HttpServletResponse.class);

            doAnswer(invocation -> {
                String key = invocation.getArgument(0);
                String value = invocation.getArgument(1);
                headers.put(key, value);
                return null;
            }).when(response).addHeader(anyString(), anyString());

            User user = User.builder()
                    .userName("장동희")
                    .nickName("장동희")
                    .phoneNumber("01020948737")
                    .email("ehdehdrnt123@naver.com")
                    .password(passwordEncoder.encode("ehd12ehd12!@"))
                    .birth("19980630")
                    .build();
            LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                    .email("ehdehdrnt123@naver.com")
                    .password("ehd12ehd12!@")
                    .build();

            String fakeAccess = "fakeAccess";
            String fakeRefresh = "fakeRefresh";

            TokenDto tokenDto = new TokenDto(fakeAccess, fakeRefresh);

            Mockito.when(userRepository.findByEmail("ehdehdrnt123@naver.com")).thenReturn(Optional.of(user));
            Mockito.when(jwtUtil.createAllToken(loginRequestDto.getEmail())).thenReturn(tokenDto);
            Mockito.when(refreshTokenRepository.findByUserEmail("ehdehdrnt123@naver.com")).thenReturn(Optional.empty());

            String result = userService.login(loginRequestDto,response);

            assertThat(user).isNotNull();
            assertThat(user.getNickName()).isEqualTo(result);

            String accessToken = headers.get(JwtUtil.ACCESS_TOKEN);
            String refreshToken = headers.get(JwtUtil.REFRESH_TOKEN);

            assertThat(accessToken).isNotEmpty();
            assertThat(refreshToken).isNotEmpty();

        }
    }
    @Nested
    @DisplayName("실패 케이스")
    class FailCase{
        @DisplayName("회원가입 실패(중복된 이메일)")
        @Test
        void failLoginEmail(){
            SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                    .userName("장동희")
                    .nickName("장동희")
                    .phoneNumber("01020948737")
                    .email("ehdehdrnt123@naver.com")
                    .password("ehd12ehd12!@")
                    .birth("19980630")
                    .build();

            String userName = signupRequestDto.getUserName();
            String nickName = signupRequestDto.getNickName();
            String email = signupRequestDto.getEmail();
            String phoneNumber = signupRequestDto.getPhoneNumber();
            Mockito.when(passwordEncoder.encode(anyString())).thenReturn(signupRequestDto.getPassword());
            String password = passwordEncoder.encode(signupRequestDto.getPassword());
            String birth = signupRequestDto.getBirth();

            User user = User.builder()
                    .userName("장동국")
                    .nickName("장동희3")
                    .phoneNumber("01020948731")
                    .email("ehdehdrnt123@naver.com")
                    .password(passwordEncoder.encode(password))
                    .birth("19980630")
                    .build();

            when(userRepository.findByEmail(signupRequestDto.getEmail())).thenReturn(Optional.of(user));
            CustomException customException = assertThrows(CustomException.class, ()->{
                userService.signup(signupRequestDto);
            });

            assertThat(customException).isNotNull();
            assertThat(customException.getErrorCode().getMessage()).isEqualTo("중복된 이메일이 존재합니다");


        }

        @DisplayName("회원가입 실패(중복된 닉네임)")
        @Test
        void failLoginNickName(){
            SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                    .userName("장동희")
                    .nickName("장동희")
                    .phoneNumber("01020948737")
                    .email("ehdehdrnt123@naver.com")
                    .password("ehd12ehd12!@")
                    .birth("19980630")
                    .build();

            String userName = signupRequestDto.getUserName();
            String nickName = signupRequestDto.getNickName();
            String email = signupRequestDto.getEmail();
            String phoneNumber = signupRequestDto.getPhoneNumber();
            Mockito.when(passwordEncoder.encode(anyString())).thenReturn(signupRequestDto.getPassword());
            String password = passwordEncoder.encode(signupRequestDto.getPassword());
            String birth = signupRequestDto.getBirth();

            User user = User.builder()
                    .userName("장동희")
                    .nickName("장동희")
                    .phoneNumber("01020948731")
                    .email("ehdehdrnt12@naver.com")
                    .password(passwordEncoder.encode(password))
                    .birth("19980630")
                    .build();

            when(userRepository.findByNickName(signupRequestDto.getNickName())).thenReturn(Optional.of(user));
            CustomException customException = assertThrows(CustomException.class, ()->{
                userService.signup(signupRequestDto);
            });

            assertThat(customException).isNotNull();
            assertThat(customException.getErrorCode().getMessage()).isEqualTo("중복된 닉네임이 존재합니다");


        }
        @DisplayName("회원가입 실패(중복된 전화번호)")
        @Test
        void failLoginPhoneNumber(){
            SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                    .userName("장동희")
                    .nickName("장동희")
                    .phoneNumber("01020948737")
                    .email("ehdehdrnt123@naver.com")
                    .password("ehd12ehd12!@")
                    .birth("19980630")
                    .build();

            String userName = signupRequestDto.getUserName();
            String nickName = signupRequestDto.getNickName();
            String email = signupRequestDto.getEmail();
            String phoneNumber = signupRequestDto.getPhoneNumber();
            Mockito.when(passwordEncoder.encode(anyString())).thenReturn(signupRequestDto.getPassword());
            String password = passwordEncoder.encode(signupRequestDto.getPassword());
            String birth = signupRequestDto.getBirth();

            User user = User.builder()
                    .userName("장동희")
                    .nickName("장동희3")
                    .phoneNumber("01020948737")
                    .email("ehdehdrnt12@naver.com")
                    .password(passwordEncoder.encode(password))
                    .birth("19980630")
                    .build();

            when(userRepository.findByPhoneNumber(signupRequestDto.getPhoneNumber())).thenReturn(Optional.of(user));
            CustomException customException = assertThrows(CustomException.class, ()->{
                userService.signup(signupRequestDto);
            });

            assertThat(customException).isNotNull();
            assertThat(customException.getErrorCode().getMessage()).isEqualTo("중복된 번호가 존재합니다");


        }



    }




    @Test
    void checkEmail() {
    }

    @Test
    void checkNickName() {
    }

    @Test
    void checkFindPw() {
    }

    @Test
    void updatePassword() {
    }

    @Test
    void getTempPassword() {
    }

    @Test
    void mailSend() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void checkFindEmail() {
    }
}