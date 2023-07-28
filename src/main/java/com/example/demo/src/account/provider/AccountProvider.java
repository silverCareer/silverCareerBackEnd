package com.example.demo.src.account.provider;


import org.springframework.stereotype.Service;

@Service
public class AccountProvider {
    public boolean validateBalance(long amount){
        if(amount <= 0){
            throw new IllegalArgumentException("0원 이하는 입금 불가능합니다.");
        }
        return true;
    }
}
