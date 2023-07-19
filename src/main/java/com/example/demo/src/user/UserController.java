package com.example.demo.src.user;

import com.example.demo.global.exception.BaseException;
import com.example.demo.global.exception.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.exception.BaseResponseStatus.POST_USERS_EMPTY_EMAIL;
import static com.example.demo.global.exception.BaseResponseStatus.POST_USERS_INVALID_EMAIL;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;


@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserProvider userProvider;
    private final UserService userService;
    private final JwtService jwtService;


    /**
     * 회원가입 API
     * [POST] /users
     *
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("") // (POST) 127.0.0.1:9000/users
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {

        if (postUserReq.getEmail() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        if (!isRegexEmail(postUserReq.getEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try {
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}

    /*
    jwt에서 idx 추출.
    int userIdxByJwt = jwtService.getUserIdx();
    userIdx와 접근한 유저가 같은지 확인
    if(userIdx != userIdxByJwt){
        return new BaseResponse<>(INVALID_USER_JWT);
    }
    */
