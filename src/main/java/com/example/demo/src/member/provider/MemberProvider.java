package com.example.demo.src.member.provider;

import com.example.demo.src.auth.repository.AuthRepository;
import com.example.demo.src.member.dto.ResponseMyInfo;
import org.springframework.stereotype.Service;

@Service
public class MemberProvider {
    private final AuthRepository authRepository;

    public MemberProvider(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public ResponseMyInfo getMyInfo(String memberName) throws IllegalAccessException {
        ResponseMyInfo responseMyInfo = ResponseMyInfo.of(authRepository.findByUsername(memberName).orElseThrow(()
                    -> new IllegalAccessException("해당 정보가 없습니다.")));

        return responseMyInfo;
    }

    public boolean validateCash(long amount){
        if(amount <= 0){
            throw new IllegalArgumentException("0원 이하는 충전이 불가능합니다.");
        }
        return true;
    }
}
