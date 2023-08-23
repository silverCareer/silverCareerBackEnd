package com.example.demo.src.member.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.member.dto.RequestCashCharge;
import com.example.demo.src.member.dto.RequestLogin;
import com.example.demo.src.member.dto.RequestMemberPatch;
import com.example.demo.src.member.dto.RequestSingUp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MemberAuthService {
    ResponseEntity<CommonResponse> mentorSignUp(final RequestSingUp requestSignUp);
    ResponseEntity<CommonResponse> menteeSignUp(final RequestSingUp requestSignUp);
    ResponseEntity<CommonResponse> login(final RequestLogin requestLogin);
    ResponseEntity<CommonResponse> getMyInfo(final String memberEmail);
    ResponseEntity<CommonResponse> checkDuplicatedName(final String name);
    ResponseEntity<CommonResponse> cashCharge(final String memberEmail, final RequestCashCharge chargeDto);
    ResponseEntity<CommonResponse> updateInfo(final String memberEmail, final RequestMemberPatch requestMemberPatch);
    ResponseEntity<CommonResponse> deleteMember(final String memberEmail);
    ResponseEntity<CommonResponse> updateProfileImg(final String memberEmail, final MultipartFile img) throws IOException;
    ResponseEntity<CommonResponse> getNotification(final String memberEmail, final String authority);
    ResponseEntity<CommonResponse> getAlarmStatus(final String memberEmail);
    ResponseEntity<CommonResponse> checkDuplicatedEmail(final String memberEmail);
}
