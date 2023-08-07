package com.example.demo.src.account.service;

import com.example.demo.src.account.domain.Account;
import com.example.demo.src.account.dto.RequestAccountCharge;
import com.example.demo.src.account.provider.AccountProvider;
import com.example.demo.src.account.repository.AccountRepository;
import com.example.demo.src.member.domain.Member;
//import com.example.demo.src.member.repository.MemberRepository;
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
}
