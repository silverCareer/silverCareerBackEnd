package com.example.demo.src.account.service;

import com.example.demo.src.account.domain.Account;
import com.example.demo.src.account.dto.RequestAccountCharge;
import com.example.demo.src.account.repository.AccountRepository;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.member.dto.MemberCashChargeEvent;
import com.example.demo.src.member.dto.MemberCreateEvent;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    // 계좌 생성
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

    // 계좌 잔액 충전
    @Transactional
    public Account charge(@Valid RequestAccountCharge chargeDto, String memberEmail) throws IllegalAccessException {
        Optional<Account> optionalAccount = accountRepository.findByMember_Username(memberEmail);

        Account account = new Account();
        long balance = chargeDto.getBalance();
        if (validateBalance(balance)) {
            account = optionalAccount.get();
            account.addBalance(balance);
        }

        accountRepository.save(account);
        return account;
    }

    // 계좌 잔액 차감
    @Transactional
    public void accountDeduct(@Valid MemberCashChargeEvent memberCashChargeEvent) throws IllegalAccessException {
        Account account = accountRepository.findByMember_Username(memberCashChargeEvent.getEmail()).orElseThrow(() ->
                new IllegalAccessException("해당 유저의 계정 정보를 찾을 수 없습니다.")
        );

        long amount = memberCashChargeEvent.getBalance();

        if(validateAccountBalance(amount, account.getBalance())){
            account.minusBalance(amount);
        }
        accountRepository.save(account);
    }

    // 금액이 0 이상의 정수인지 판단
    public boolean validateBalance(long amount){
        if(amount <= 0){
            throw new IllegalArgumentException("0원 이하는 입금 불가능합니다.");
        }
        return true;
    }

    // 잔액 확인
    public boolean validateAccountBalance(long amount, long accountBalance){
        if(accountBalance < amount){
            throw new IllegalArgumentException("잔액이 충분하지 않습니다.");
        }
        return true;
    }
}
