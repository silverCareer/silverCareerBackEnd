package com.example.demo.src.member.handler;

import com.example.demo.src.account.service.AccountServiceImpl;
import com.example.demo.src.member.dto.MemberCashChargeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CashChargeHandler {

    private final AccountServiceImpl accountService;
    @EventListener
    public void AccountDeduct(MemberCashChargeEvent memberCashChargeEvent) throws IllegalAccessException {
        accountService.accountDeduct(memberCashChargeEvent);
    }
}
