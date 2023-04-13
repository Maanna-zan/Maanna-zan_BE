package com.hanghae99.maannazan.domain.user;

import com.hanghae99.maannazan.domain.entity.RefreshToken;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.repository.RefreshTokenRepository;
import com.hanghae99.maannazan.domain.repository.UserRepository;
import com.hanghae99.maannazan.domain.user.dto.*;
import com.hanghae99.maannazan.global.exception.CustomException;
import com.hanghae99.maannazan.global.jwt.JwtUtil;
import com.hanghae99.maannazan.global.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    private final RefreshTokenRepository refreshTokenRepository;
    private final JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "ehdehdrnt123@naver.com";





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
                () -> new CustomException(NOT_PROPER_EMAIL_OR_PASSWORD));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(NOT_PROPER_EMAIL_OR_PASSWORD);
        }
        // 아이디 정보로 Token생성
        TokenDto tokenDto = jwtUtil.createAllToken(loginRequestDto.getEmail());

        // Refresh토큰 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserEmail(loginRequestDto.getEmail());

        if(refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        }else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginRequestDto.getEmail());
            refreshTokenRepository.save(newToken);
        }
        setHeader(response, tokenDto);
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
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

    //비밀번호 이메일 전송
    @Transactional
    public MailDto checkFindPw(CheckFindPwRequestDto checkFindPw) {
        String email = checkFindPw.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException(EMAIL_NOT_FOUND));
        if (user.getEmail() != null && user.getEmail().equals(email)){
            String str = getTempPassword();
            MailDto dto = new MailDto();
            dto.setAddress(email);
            dto.setTitle("MannaZan 임시비밀번호 안내 이메일 입니다.");
            dto.setMessage("안녕하세요. MannaZan 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                    + str + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요");
            updatePassword(str, email);
            return dto;
        }
        return null;
    }

    @Transactional
    public void updatePassword(String str, String email){
        String pw = passwordEncoder.encode(str);
        User user = userRepository.findByEmail(email).get();
        user.update(pw, email);
    }

    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    public void mailSend(MailDto mailDto) {
        System.out.println("전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());
        message.setFrom(FROM_ADDRESS);
        message.setReplyTo(FROM_ADDRESS);
        mailSender.send(message);
    }



}
