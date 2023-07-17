package com.example.demo.src.auth;

import com.example.demo.src.auth.model.PostLoginReq;
import com.example.demo.src.auth.model.PostLoginRes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AuthDao {

    Integer findByEmailAndPassword(PostLoginReq postLoginReq);
}