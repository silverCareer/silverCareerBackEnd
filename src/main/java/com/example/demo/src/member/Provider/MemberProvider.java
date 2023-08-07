package com.example.demo.src.member.Provider;

import com.example.demo.src.member.dto.ResponseMyInfo;
import com.example.demo.src.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProvider {
    private final MemberRepository memberRepository;

    public ResponseMyInfo getMyInfo(String memberName) throws IllegalAccessException {
        ResponseMyInfo responseMyInfo = ResponseMyInfo.of(memberRepository.findByUsername(memberName).orElseThrow(()
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
