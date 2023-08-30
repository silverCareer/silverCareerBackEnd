package com.example.demo.src.account.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.account.dto.RequestAccountCharge;
import com.example.demo.src.member.dto.MemberCashChargeEvent;
import com.example.demo.src.member.dto.MemberCreateEvent;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    ResponseEntity<CommonResponse> createAccount(final MemberCreateEvent memberCreateEvent);
    ResponseEntity<CommonResponse> charge(final RequestAccountCharge chargeDto, final String memberEmail);
    ResponseEntity<CommonResponse> accountDeduct(final MemberCashChargeEvent memberCashChargeEvent);
}
