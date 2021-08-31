package com.mvbbb.yim.ws;

import com.mvbbb.yim.ws.handler.RecFrameHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketServer {

    Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private int port;

    public WebSocketServer(int port) {
        this.port = port;
    }

    void startWebSocketServer(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WSChannelInitializer());
        Channel channel = null;
        try {
            channel = bootstrap.bind(port).sync().addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    logger.info("启动成功");
                } else {
                    logger.error("启动失败");
                }
            }).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
