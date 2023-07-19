package com.example.demo.global.config;

import com.example.demo.global.security.TokenProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

    /**
     *
     * @param jwtProperties
     * @return tokenProvider
     * <p>JwtConfig는 JWT 설정파일로
     * TokenProvider에 의존성을 주입하고
     * 빈을 생성하는 역할을 수행합니다.</p>
     */
    @Bean(name = "tokenProvider")
    public TokenProvider tokenProvider(JwtProperties jwtProperties){
        return new TokenProvider(jwtProperties.getSecret(), jwtProperties.getAccessTokenValidityInSeconds());
    }
}
