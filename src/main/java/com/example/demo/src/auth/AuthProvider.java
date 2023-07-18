package com.example.demo.src.auth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.auth.model.PostLoginReq;
import com.example.demo.src.auth.model.PostLoginRes;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import static com.example.demo.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class AuthProvider {

    private final AuthDao authDao;

    public PostLoginRes findByEmailAndPassword(PostLoginReq postLoginReq) throws BaseException {
        PostLoginRes postLoginRes = new PostLoginRes("", 0);

        Integer member_idx = authDao.findByEmailAndPassword(postLoginReq);

        if (member_idx != null) {
            postLoginRes.setMemberIdx(member_idx);
        } else {
            throw new BaseException(LOGIN_FAIL_WRONG);
        }

        return postLoginRes;
    }
}