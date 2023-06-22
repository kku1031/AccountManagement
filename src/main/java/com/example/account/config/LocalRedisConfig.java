package com.example.account.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
//레디스를 띄우기 위한 클래스
@Configuration
public class LocalRedisConfig {
    // org.springframework.beans.factory.annotation을 사용, Lombok의 Value를 사용해서 오류 났었음.
    // 스프링의 프로퍼티 파일에서 spring.redis.port라는 속성 값을 가져옵니다.
    // 해당 속성은 Redis 서버의 포트를 설정하는데 사용됩니다.
    // redisPort 변수에 주입됩니다.
    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct // 빈이 초기화된 후에 실행되는 메서드입니다.
    public void startRedis() {
        redisServer = new RedisServer(redisPort);   // redisPort를 사용하여 내장 Redis 서버를 시작합니다.
        redisServer.start();
    }

    @PreDestroy //스프링 빈이 소멸되기 전에 실행되는 메서드입니다.
    public void stopRedis() {
        if(redisServer != null) {
            redisServer.stop();                    // Redis 서버가 실행 중인 경우에만 Redis 서버를 중지합니다.
        }
    }
}