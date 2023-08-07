package com.example.demo.src.account.service;

import com.example.demo.src.account.domain.Account;
import com.example.demo.src.account.dto.RequestAccountCharge;
import com.example.demo.src.account.provider.AccountProvider;
import com.example.demo.src.account.repository.AccountRepository;
import com.example.demo.src.member.domain.Member;
//import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountProvider accountProvider;

    @Transactional
    public void charge(RequestAccountCharge chargeDto) throws IllegalAccessException {
        Member member = memberRepository.findById(chargeDto.memberIdx()).orElseThrow(()
                -> new IllegalAccessException("해당 유저가 없습니다."));
        Optional<Account> optionalAccount = accountRepository.findByMember_MemberIdx(chargeDto.memberIdx());

        Account account = new Account();
        long balance = chargeDto.balance();
        if(accountProvider.validateBalance(balance)){
            if(optionalAccount.isEmpty()){ // 아직 캐시가 없으면 최초 등록
                account = Account.builder()
                        .bankName(chargeDto.bankName())
                        .accountNum(chargeDto.accountNum())
                        .balance(balance)
                        .member(member) // authIdx 값 설정
                        .build();
            } else { // 아닐 경우 누적해서
                account = optionalAccount.get();
                account.addBalance(balance);
            }
        }

        accountRepository.save(account);
    }
}
