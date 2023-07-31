package com.example.demo.src.account.provider;


import org.springframework.stereotype.Service;

@Service
public class AccountProvider {

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
