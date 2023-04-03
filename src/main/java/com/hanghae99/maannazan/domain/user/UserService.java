package com.hanghae99.maannazan.domain.user;

import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.repository.UserRepository;
import com.hanghae99.maannazan.domain.user.dto.CheckEmailRequestDto;
import com.hanghae99.maannazan.domain.user.dto.CheckNickNameRequestDto;
import com.hanghae99.maannazan.domain.user.dto.LoginRequestDto;
import com.hanghae99.maannazan.domain.user.dto.SignupRequestDto;
import com.hanghae99.maannazan.global.exception.CustomException;
import com.hanghae99.maannazan.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.hanghae99.maannazan.global.exception.CustomErrorCode.*;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    //리턴이 따로 필요없어서 void로 처리
    //회원가입 Service
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String userName = signupRequestDto.getUserName();
        String nickName = signupRequestDto.getNickName();
        String email = signupRequestDto.getEmail();
        String phoneNumber = signupRequestDto.getPhoneNumber();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String birth = signupRequestDto.getBirth();

        Optional<User> foundNickName = userRepository.findByNickName(nickName);
        if (foundNickName.isPresent()) throw new CustomException(DUPLICATE_NICKNAME);

        Optional<User> foundEmail = userRepository.findByEmail(email);
        if (foundEmail.isPresent()) {
            throw new CustomException(DUPLICATE_EMAIL);
        }

        Optional<User> foundPhoneNumber = userRepository.findByPhoneNumber(phoneNumber);
        if (foundPhoneNumber.isPresent()) {
            throw new CustomException(DUPLICATE_PHONENUMBER);
        }

        User user = new User(userName, nickName, email, phoneNumber, password, birth);
        userRepository.save(user);
    }

    //리턴이 따로 필요없어서 void로 처리
    //로그인 서비스
    @Transactional
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(NOT_PROPER_PASSWORD);
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getNickName()));
    }
    // 유저이메일 중복 확인 서비스
    @Transactional(readOnly = true)
    public void checkEmail(CheckEmailRequestDto checkEmailRequestDto) {
        String email = checkEmailRequestDto.getEmail();
        Optional<User> foundEmail = userRepository.findByEmail(email);
        if (foundEmail.isPresent()) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
    }
    // 닉네임 중복 확인 서비스
    @Transactional(readOnly = true)
    public void checkNickName(CheckNickNameRequestDto checkNickNameRequestDto) {
        String nickName = checkNickNameRequestDto.getNickName();
        Optional<User> foundNickName = userRepository.findByNickName(nickName);
        if (foundNickName.isPresent()) throw new CustomException(DUPLICATE_NICKNAME);
    }
}
