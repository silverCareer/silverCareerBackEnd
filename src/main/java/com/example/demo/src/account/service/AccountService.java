package com.example.demo.src.account.service;

import com.example.demo.src.account.domain.Account;
import com.example.demo.src.account.dto.RequestAccountCharge;
import com.example.demo.src.account.dto.ResponseAccountCharge;
import com.example.demo.src.account.provider.AccountProvider;
import com.example.demo.src.account.repository.AccountRepository;
import com.example.demo.src.auth.domain.Auth;
import com.example.demo.src.auth.repository.AuthRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthRepository authRepository;
    private final AccountProvider accountProvider;

    @Transactional
    public void charge(RequestAccountCharge chargeDto) throws IllegalAccessException {
        Auth auth = authRepository.findById(chargeDto.authIdx()).orElseThrow(()
                -> new IllegalAccessException("해당 유저가 없습니다."));
        Optional<Account> optionalAccount = accountRepository.findByAuth_AuthIdx(chargeDto.authIdx());
        Account account = new Account();
        long balance = chargeDto.balance();
        if(accountProvider.validateBalance(balance)){
            if(optionalAccount.isEmpty()){ // 아직 캐시가 없으면 최초 등록
                account = Account.builder()
                        .bankName(chargeDto.bankName())
                        .accountNum(chargeDto.accountNum())
                        .balance(balance)
                        .auth(auth) // authIdx 값 설정
                        .build();
            } else { // 아닐 경우 누적해서
                account = optionalAccount.get();
                System.out.println(account.getBalance());
                account.addBalance(balance);
            }
        }

        accountRepository.save(account);
    }
}
