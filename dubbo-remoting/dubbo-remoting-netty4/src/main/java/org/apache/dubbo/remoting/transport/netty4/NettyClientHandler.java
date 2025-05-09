/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.remoting.transport.netty4;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.Version;
import org.apache.dubbo.common.logger.ErrorTypeAwareLogger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.remoting.ChannelHandler;
import org.apache.dubbo.remoting.exchange.Request;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;

import static org.apache.dubbo.common.constants.CommonConstants.HEARTBEAT_EVENT;
import static org.apache.dubbo.common.constants.LoggerCodeConstants.TRANSPORT_UNEXPECTED_EXCEPTION;

/**
 * NettyClientHandler
 */
@io.netty.channel.ChannelHandler.Sharable
public class NettyClientHandler extends ChannelDuplexHandler {
    private static final ErrorTypeAwareLogger logger = LoggerFactory.getErrorTypeAwareLogger(NettyClientHandler.class);

    private final URL url;

    private final ChannelHandler handler;

    public NettyClientHandler(URL url, ChannelHandler handler) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        this.url = url;
        this.handler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel ch = ctx.channel();
        NettyChannel channel = NettyChannel.getOrAddChannel(ch, url, handler);
        handler.connected(channel);

        if (logger.isInfoEnabled()) {
            logger.info(
                    "The connection {} of {} -> {} is established.",
                    ch,
                    channel.getLocalAddressKey(),
                    channel.getRemoteAddressKey());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel ch = ctx.channel();
        NettyChannel channel = NettyChannel.getOrAddChannel(ch, url, handler);
        try {
            handler.disconnected(channel);
        } finally {
            NettyChannel.removeChannel(ch);
        }

        if (logger.isInfoEnabled()) {
            logger.info(
                    "The connection {} of {} -> {} is disconnected.",
                    ch,
                    channel.getLocalAddressKey(),
                    channel.getRemoteAddressKey());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
        handler.received(channel, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // send heartbeat when read idle.
        if (evt instanceof IdleStateEvent) {
            try {
                NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
                if (logger.isDebugEnabled()) {
                    logger.debug("IdleStateEvent triggered, send heartbeat to channel " + channel);
                }
                Request req = new Request();
                req.setVersion(Version.getProtocolVersion());
                req.setTwoWay(true);
                req.setEvent(HEARTBEAT_EVENT);
                channel.send(req);
            } finally {
                NettyChannel.removeChannelIfDisconnected(ctx.channel());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel ch = ctx.channel();
        NettyChannel channel = NettyChannel.getOrAddChannel(ch, url, handler);
        try {
            handler.caught(channel, cause);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ch);
        }

        if (logger.isWarnEnabled()) {
            logger.warn(
                    TRANSPORT_UNEXPECTED_EXCEPTION,
                    "",
                    "",
                    channel == null
                            ? String.format("The connection %s has exception.", ch)
                            : String.format(
                                    "The connection %s of %s -> %s has exception.",
                                    ch, channel.getLocalAddressKey(), channel.getRemoteAddressKey()),
                    cause);
        }
    }
}
