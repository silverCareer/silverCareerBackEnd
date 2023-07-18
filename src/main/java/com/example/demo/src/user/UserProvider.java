package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@RequiredArgsConstructor
@Service
@Slf4j
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;



    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
