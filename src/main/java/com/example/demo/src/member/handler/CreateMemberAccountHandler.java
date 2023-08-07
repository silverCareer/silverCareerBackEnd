package com.example.demo.src.member.handler;

import com.example.demo.src.account.service.AccountService;
import com.example.demo.src.member.dto.MemberCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateMemberAccountHandler {
    private final AccountService accountService;

    @EventListener
    public void createAccount(MemberCreateEvent memberCreateEvent) throws IllegalAccessException {
        accountService.createAccount(memberCreateEvent);
    }
}
