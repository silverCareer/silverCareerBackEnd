
package com.example.demo.src.account.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.exception.error.Account.NotEnoughBalanceException;
import com.example.demo.global.exception.error.Account.NotFoundAccountException;
import com.example.demo.global.exception.error.charge.InvalidAmountException;
import com.example.demo.global.exception.error.member.NotFoundMemberException;
import com.example.demo.src.account.domain.Account;
import com.example.demo.src.account.dto.RequestAccountCharge;
import com.example.demo.src.account.repository.AccountRepository;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.member.dto.MemberCashChargeEvent;
import com.example.demo.src.member.dto.MemberCreateEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    // 계좌 생성
    @Override
    @Transactional
    public ResponseEntity<CommonResponse> createAccount(MemberCreateEvent memberCreateEvent){

        Member member = memberRepository.findByUsername(memberCreateEvent.getUsername())
                .orElseThrow(NotFoundMemberException::new);

        Account account = Account.builder()
                .bankName(memberCreateEvent.getBankName())
                .accountNum(memberCreateEvent.getAccountNum())
                .balance(0L)
                .member(member)
                .build();

        accountRepository.save(account);

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response("계좌 생성 성공").build());
    }

    // 계좌 잔액 충전
    @Override
    @Transactional
    public ResponseEntity<CommonResponse> charge(final RequestAccountCharge chargeDto, final String memberEmail){
        Account account = accountRepository.findAccountByMember_Username(memberEmail)
                .orElseThrow(NotFoundAccountException::new);

        long balance = chargeDto.getBalance();
        if (validateBalance(balance)) {
            account.addBalance(balance);
        }

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(String.format("%d원 충전 성공", balance)).build());
    }

    // 계좌 잔액 차감
    @Override
    @Transactional
    public ResponseEntity<CommonResponse> accountDeduct(MemberCashChargeEvent memberCashChargeEvent){
        Account account = accountRepository.findAccountByMember_Username(memberCashChargeEvent.getEmail())
                .orElseThrow(NotFoundAccountException::new);

        long amount = memberCashChargeEvent.getBalance();

        if(validateAccountBalance(amount, account.getBalance())){
            account.minusBalance(amount);
        }
        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(String.format("%d원 차감 성공", amount)).build());
    }


    // Helper Function
    // 금액이 0 이상의 정수인지 판단
    private boolean validateBalance(long amount){
        if(amount <= 0){
            throw new InvalidAmountException();
        }
        return true;
    }

    // 잔액 확인
    private boolean validateAccountBalance(long amount, long accountBalance){
        if(accountBalance < amount){
            throw new NotEnoughBalanceException();
        }
        return true;
    }
}

