package com.example.demo.src.auth;

import com.example.demo.global.exception.BaseException;
import com.example.demo.src.auth.model.PostLoginReq;
import com.example.demo.src.auth.model.PostLoginRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.demo.global.exception.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthDao authDao;
    private final AuthProvider authProvider;
    private final JwtService jwtService;

    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        String pwd;
        try {
            pwd = new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        postLoginReq.setPassword(pwd);

        PostLoginRes postLoginRes = authProvider.findByEmailAndPassword(postLoginReq);

        String jwt = jwtService.createJwt(postLoginRes.getMemberIdx());

        return new PostLoginRes(jwt, postLoginRes.getMemberIdx());
    }
}