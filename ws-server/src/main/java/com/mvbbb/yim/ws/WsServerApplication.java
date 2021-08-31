package com.mvbbb.yim.ws;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

@SpringBootApplication
@EnableDubbo
@PropertySource({"classpath:/dubbo-consumer.properties","classpath:/dubbo-provider.properties"})
public class WsServerApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(WsServerApplication.class,args);
    }

    @Resource
    RedisTemplate<String,String> redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        new WebSocketServer(7100).startWebSocketServer();
        ConsumeMsgTask consumeMsgTask = new ConsumeMsgTask(redisTemplate);
        new Thread(consumeMsgTask).start();;
    }
}
