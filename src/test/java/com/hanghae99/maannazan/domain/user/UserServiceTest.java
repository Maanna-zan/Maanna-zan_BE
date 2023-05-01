package com.hanghae99.maannazan.domain.user;

import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.repository.RefreshTokenRepository;
import com.hanghae99.maannazan.domain.repository.UserRepository;
import com.hanghae99.maannazan.domain.user.dto.*;
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
            //given
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

            //when
            Mockito.when(userRepository.findByEmail("ehdehdrnt123@naver.com")).thenReturn(Optional.of(user));
            Mockito.when(jwtUtil.createAllToken(loginRequestDto.getEmail())).thenReturn(tokenDto);
            Mockito.when(refreshTokenRepository.findByUserEmail("ehdehdrnt123@naver.com")).thenReturn(Optional.empty());
            String result = userService.login(loginRequestDto,response);

            //then
            assertThat(user).isNotNull();
            assertThat(user.getNickName()).isEqualTo(result);

            String accessToken = headers.get(JwtUtil.ACCESS_TOKEN);
            String refreshToken = headers.get(JwtUtil.REFRESH_TOKEN);

            assertThat(accessToken).isNotEmpty();
            assertThat(refreshToken).isNotEmpty();

        }

        @DisplayName("email 중복확인")
        @Test
        void checkEmail(){
            //given
            User user = User.builder()
                    .userName("장동희")
                    .nickName("장동희")
                    .phoneNumber("01020948737")
                    .email("ehdehdrnt123@naver.com")
                    .password(passwordEncoder.encode("ehd12ehd12!@"))
                    .birth("19980630")
                    .build();
            CheckEmailRequestDto checkEmailRequestDto = CheckEmailRequestDto.builder()
                    .email("ehdehdrnt123@naver.com")
                    .build();

            //when
            Mockito.when(userRepository.findByEmail("ehdehdrnt123@naver.com")).thenReturn(Optional.of(user));
            CustomException customException = assertThrows(CustomException.class, ()-> userService.checkEmail(checkEmailRequestDto));

            //then
            assertThat(customException).isNotNull();
            assertThat(customException.getErrorCode().getMessage()).isEqualTo("중복된 이메일이 존재합니다");

        }

        @DisplayName("nickName 중복확인")
        @Test
        void checkNickName(){
            //given
            User user = User.builder()
                    .userName("장동희")
                    .nickName("장동희")
                    .phoneNumber("01020948737")
                    .email("ehdehdrnt123@naver.com")
                    .password(passwordEncoder.encode("ehd12ehd12!@"))
                    .birth("19980630")
                    .build();
            CheckNickNameRequestDto checkNickNameRequestDto = CheckNickNameRequestDto.builder()
                    .nickName("장동희")
                    .build();

            //when
            Mockito.when(userRepository.findByNickName("장동희")).thenReturn(Optional.of(user));
            CustomException customException = assertThrows(CustomException.class, ()-> userService.checkNickName(checkNickNameRequestDto));

            //then
            assertThat(customException).isNotNull();
            assertThat(customException.getErrorCode().getMessage()).isEqualTo("중복된 닉네임이 존재합니다");

        }
        @DisplayName("임시비밀번호 메일전송")
        @Test
        void checkFindPw(){
            User user = User.builder()
                    .userName("장동희")
                    .nickName("장동희")
                    .phoneNumber("01020948737")
                    .email("ehdehdrnt123@naver.com")
                    .password(passwordEncoder.encode("ehd12ehd12!@"))
                    .birth("19980630")
                    .build();
            CheckFindPwRequestDto checkFindPwRequestDto = CheckFindPwRequestDto.builder()
                    .email("ehdehdrnt123@naver.com")
                    .build();


            Mockito.when(userRepository.findByEmail("ehdehdrnt123@naver.com")).thenReturn(Optional.of(user));
            MailDto mailDto = userService.checkFindPw(checkFindPwRequestDto);

            assertThat(user).isNotNull();
            assertThat(mailDto.getAddress()).isEqualTo("ehdehdrnt123@naver.com");
            assertThat(mailDto.getTitle()).isEqualTo("MannaZan 임시비밀번호 안내 이메일 입니다.");
            assertThat(user.getPassword()).isNotEqualTo(passwordEncoder.encode("ehd12ehd12!@"));
        }



    }
    @Nested
    @DisplayName("실패 케이스")
    class FailCase{
        @DisplayName("회원가입 실패(중복된 이메일)")
        @Test
        void failSignUpEmail(){
            //given
            SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                    .userName("장동희")
                    .nickName("장동희")
                    .phoneNumber("01020948737")
                    .email("ehdehdrnt123@naver.com")
                    .password("ehd12ehd12!@")
                    .birth("19980630")
                    .build();
            Mockito.when(passwordEncoder.encode(anyString())).thenReturn(signupRequestDto.getPassword());
            String password = passwordEncoder.encode(signupRequestDto.getPassword());
            User user = User.builder()
                    .userName("장동국")
                    .nickName("장동희3")
                    .phoneNumber("01020948731")
                    .email("ehdehdrnt123@naver.com")
                    .password(passwordEncoder.encode(password))
                    .birth("19980630")
                    .build();

            //when
            when(userRepository.findByEmail(signupRequestDto.getEmail())).thenReturn(Optional.of(user));
            CustomException customException = assertThrows(CustomException.class, ()-> userService.signup(signupRequestDto));

            //then
            assertThat(customException).isNotNull();
            assertThat(customException.getErrorCode().getMessage()).isEqualTo("중복된 이메일이 존재합니다");


        }

        @DisplayName("회원가입 실패(중복된 닉네임)")
        @Test
        void failSignUpNickName(){
            //given
            SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                    .userName("장동희")
                    .nickName("장동희")
                    .phoneNumber("01020948737")
                    .email("ehdehdrnt123@naver.com")
                    .password("ehd12ehd12!@")
                    .birth("19980630")
                    .build();

            Mockito.when(passwordEncoder.encode(anyString())).thenReturn(signupRequestDto.getPassword());
            String password = passwordEncoder.encode(signupRequestDto.getPassword());

            User user = User.builder()
                    .userName("장동희")
                    .nickName("장동희")
                    .phoneNumber("01020948731")
                    .email("ehdehdrnt12@naver.com")
                    .password(passwordEncoder.encode(password))
                    .birth("19980630")
                    .build();

            //when
            when(userRepository.findByNickName(signupRequestDto.getNickName())).thenReturn(Optional.of(user));
            CustomException customException = assertThrows(CustomException.class, ()-> userService.signup(signupRequestDto));

            //then
            assertThat(customException).isNotNull();
            assertThat(customException.getErrorCode().getMessage()).isEqualTo("중복된 닉네임이 존재합니다");


        }
        @DisplayName("회원가입 실패(중복된 전화번호)")
        @Test
        void failSignUpPhoneNumber(){
            //given
            SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                    .userName("장동희")
                    .nickName("장동희")
                    .phoneNumber("01020948737")
                    .email("ehdehdrnt123@naver.com")
                    .password("ehd12ehd12!@")
                    .birth("19980630")
                    .build();


            Mockito.when(passwordEncoder.encode(anyString())).thenReturn(signupRequestDto.getPassword());
            String password = passwordEncoder.encode(signupRequestDto.getPassword());

            User user = User.builder()
                    .userName("장동희")
                    .nickName("장동희3")
                    .phoneNumber("01020948737")
                    .email("ehdehdrnt12@naver.com")
                    .password(passwordEncoder.encode(password))
                    .birth("19980630")
                    .build();

            //when
            when(userRepository.findByPhoneNumber(signupRequestDto.getPhoneNumber())).thenReturn(Optional.of(user));
            CustomException customException = assertThrows(CustomException.class, ()-> userService.signup(signupRequestDto));

            //then
            assertThat(customException).isNotNull();
            assertThat(customException.getErrorCode().getMessage()).isEqualTo("중복된 번호가 존재합니다");


        }

        @DisplayName("로그인 실패(일치하지않는 email)")
        @Test
        void failloginPassword() {
            //given
            HttpServletResponse response = mock(HttpServletResponse.class);

            LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                    .email("ehdehdrnt123@naver.com")
                    .password("ehd12ehd12!@")
                    .build();

            //when
            Mockito.when(userRepository.findByEmail("ehdehdrnt123@naver.com")).thenReturn(Optional.empty());
            CustomException customException = assertThrows(CustomException.class, ()-> userService.login(loginRequestDto,response));

            //then
            assertThat(customException).isNotNull();
            assertThat(customException.getErrorCode().getMessage()).isEqualTo("이메일 또는 비밀번호가 일치하지 않습니다.");

        }

        @DisplayName("로그인 실패(일치하지않는 비밀번호)")
        @Test
        void failloginEmail() {
            //given
            HttpServletResponse response = mock(HttpServletResponse.class);

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
                    .password("ehd12ehd12!")
                    .build();

            //when
            Mockito.when(userRepository.findByEmail("ehdehdrnt123@naver.com")).thenReturn(Optional.of(user));
            CustomException customException = assertThrows(CustomException.class, ()-> userService.login(loginRequestDto,response));

            //then
            assertThat(customException).isNotNull();
            assertThat(customException.getErrorCode().getMessage()).isEqualTo("이메일 또는 비밀번호가 일치하지 않습니다.");

        }



    }
}