//package com.example.demo.src.auth;
//
//import com.example.demo.global.exception.BaseException;
//import com.example.demo.src.auth.dto.PostLoginReq;
//import com.example.demo.src.auth.dto.PostLoginRes;
//import org.springframework.stereotype.Service;
//import lombok.RequiredArgsConstructor;
//
//import static com.example.demo.global.exception.BaseResponseStatus.*;
//
//@RequiredArgsConstructor
//@Service
//public class AuthProvider {
//
//    private final AuthDao authDao;
//
//    public PostLoginRes findByEmailAndPassword(PostLoginReq postLoginReq) throws BaseException {
//        PostLoginRes postLoginRes = new PostLoginRes("", 0);
//
//        Integer member_idx = authDao.findByEmailAndPassword(postLoginReq);
//
//        if (member_idx != null) {
//            postLoginRes.setMemberIdx(member_idx);
//        } else {
//            throw new BaseException(LOGIN_FAIL_WRONG);
//        }
//
//        return postLoginRes;
//    }
//}