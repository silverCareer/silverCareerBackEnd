package com.example.demo.src.kakao;

import com.example.demo.src.auth.domain.Auth;
import com.example.demo.src.auth.domain.Members;
import com.example.demo.src.kakao.dto.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateService {

    private final UserRepository userRepository;


    @Transactional
    public ResponseUser createKakaoUser(Auth user) {
        //이거는 이제 추가정보까지 다 기입하고 난 후에
        if(user == null) {
            userRepository.save(user);
        }

        //Members member = userRepository.findById(user.getUserId()); //optional로 감싸야하나..
        ResponseUser member = ResponseUser.builder()
                .userIdx(user.getAuthIdx())
                .password(user.getPassword())
                .userName(user.getUsername())
                .userEmail(user.getEmail())
                .userImage(user.getUserImage())
                .phoneNumber(user.getPhoneNumber())
                .age(user.getAge())
                .provider(user.getProvider())
                .build();

        return member;
    }
}
