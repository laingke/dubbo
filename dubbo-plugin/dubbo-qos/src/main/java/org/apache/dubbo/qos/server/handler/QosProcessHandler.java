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
package org.apache.dubbo.qos.server.handler;

import org.apache.dubbo.common.utils.ExecutorUtil;
import org.apache.dubbo.qos.api.QosConfiguration;
import org.apache.dubbo.rpc.model.FrameworkModel;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.ScheduledFuture;

public class QosProcessHandler extends ByteToMessageDecoder {

    private ScheduledFuture<?> welcomeFuture;

    private final FrameworkModel frameworkModel;

    public static final String PROMPT = "dubbo>";

    private final QosConfiguration qosConfiguration;

    public QosProcessHandler(FrameworkModel frameworkModel, QosConfiguration qosConfiguration) {
        this.frameworkModel = frameworkModel;
        this.qosConfiguration = qosConfiguration;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        welcomeFuture = ctx.executor()
                .schedule(
                        () -> {
                            final String welcome = qosConfiguration.getWelcome();
                            if (welcome != null) {
                                ctx.write(Unpooled.wrappedBuffer(welcome.getBytes(StandardCharsets.UTF_8)));
                                ctx.writeAndFlush(Unpooled.wrappedBuffer(PROMPT.getBytes(StandardCharsets.UTF_8)));
                            }
                        },
                        500,
                        TimeUnit.MILLISECONDS);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 1) {
            return;
        }

        // read one byte to guess protocol
        final int magic = in.getByte(in.readerIndex());

        ChannelPipeline p = ctx.pipeline();
        p.addLast(new ForeignHostPermitHandler(qosConfiguration));
        if (isHttp(magic)) {
            // no welcome output for http protocol
            if (welcomeFuture != null && welcomeFuture.isCancellable()) {
                welcomeFuture.cancel(false);
            }
            p.addLast(new HttpServerCodec());
            p.addLast(new HttpObjectAggregator(1048576));
            p.addLast(new HttpProcessHandler(frameworkModel, qosConfiguration));
            p.remove(this);
        } else {
            p.addLast(new CtrlCHandler());
            p.addLast(new LineBasedFrameDecoder(2048));
            p.addLast(new StringDecoder(CharsetUtil.UTF_8));
            p.addLast(new StringEncoder(CharsetUtil.UTF_8));
            p.addLast(new IdleStateHandler(0, 0, 5 * 60));
            p.addLast(new TelnetIdleEventHandler());
            p.addLast(new TelnetProcessHandler(frameworkModel, qosConfiguration));
            p.remove(this);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ExecutorUtil.cancelScheduledFuture(welcomeFuture);
            ctx.close();
        }
    }

    // G for GET, and P for POST
    private static boolean isHttp(int magic) {
        return magic == 'G' || magic == 'P';
    }
}
