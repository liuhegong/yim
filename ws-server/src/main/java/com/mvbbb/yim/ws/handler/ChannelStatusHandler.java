package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.logic.service.UserStatusService;
import com.mvbbb.yim.ws.ConnectionPool;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Component
@ChannelHandler.Sharable
public class ChannelStatusHandler extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(ChannelStatusHandler.class);

    ConnectionPool connectionPool = ConnectionPool.getInstance();
    @DubboReference(check = false)
    UserStatusService userStatusService;

    private static ChannelStatusHandler closeChannelHandler;

    @PostConstruct
    public void init(){
        closeChannelHandler = this;
    }

    // 用户关闭连接时清理状态
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String userId = connectionPool.getUseridByChannel(channel);
        if(userId!=null){
            connectionPool.removeConnection(channel,userId);
            closeChannelHandler.userStatusService.userOffline(userId);
            logger.error("用户断开 socket 连接. userId：[{}],channel: [{}]",userId,channel);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Timer timer = new Timer("time to close channel", false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String user = connectionPool.getUseridByChannel(ctx.channel());
                if(user==null){
                    try {
                        logger.error("长时间没有发送认证消息，自动关闭 channel。channel : [{}]",ctx.channel());
                        ctx.channel().close().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 10000);
    }
}