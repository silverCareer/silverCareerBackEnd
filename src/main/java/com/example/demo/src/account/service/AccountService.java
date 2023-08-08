package com.example.demo.src.account.service;

import com.example.demo.src.account.domain.Account;
import com.example.demo.src.account.dto.RequestAccountCharge;
import com.example.demo.src.account.provider.AccountProvider;
import com.example.demo.src.account.repository.AccountRepository;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.dto.MemberCashChargeEvent;
import com.example.demo.src.member.dto.MemberCreateEvent;
import com.example.demo.src.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountProvider accountProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public void createAccount(MemberCreateEvent memberCreateEvent) throws IllegalAccessException {

        Member member = memberRepository.findByUsername(memberCreateEvent.getUsername()).orElseThrow(()
                -> new IllegalAccessException("해당 유저가 없습니다."));

        Account account = Account.builder()
                .bankName(memberCreateEvent.getBankName())
                .accountNum(memberCreateEvent.getAccountNum())
                .balance(0L)
                .member(member)
                .build();

        accountRepository.save(account);
    }

    @Transactional
    public Account charge(@Valid RequestAccountCharge chargeDto, String memberEmail) throws IllegalAccessException {
        Optional<Account> optionalAccount = accountRepository.findByMember_Username(memberEmail);

        Account account = new Account();
        long balance = chargeDto.getBalance();
        if (accountProvider.validateBalance(balance)) {
            account = optionalAccount.get();
            account.addBalance(balance);
        }

        accountRepository.save(account);
        return account;
    }

//    캐시 충전
//    입력된 금액이 양수인지 확인해야함
//    이벤트 발생
//    account로 이동해서 account에 잔액 남아있는지 확인
//    잔액이 남아있다면 차감해주고
//    캐시 충전해야함
    @Transactional
    public void accountDeduct(@Valid MemberCashChargeEvent memberCashChargeEvent) throws IllegalAccessException {
        Account account = accountRepository.findByMember_Username(memberCashChargeEvent.getEmail()).orElseThrow(() ->
                new IllegalAccessException("해당 유저의 계정 정보를 찾을 수 없습니다.")
        );

        long amount = memberCashChargeEvent.getBalance();

        if(accountProvider.validateAccountBalance(amount, account.getBalance())){
            account.minusBalance(amount);
        }
        accountRepository.save(account);
    }
}
