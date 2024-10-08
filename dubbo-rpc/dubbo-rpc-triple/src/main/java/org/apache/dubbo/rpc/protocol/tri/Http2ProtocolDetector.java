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
package org.apache.dubbo.rpc.protocol.tri;

import org.apache.dubbo.remoting.api.ProtocolDetector;
import org.apache.dubbo.remoting.buffer.ByteBufferBackedChannelBuffer;
import org.apache.dubbo.remoting.buffer.ChannelBuffer;
import org.apache.dubbo.remoting.buffer.ChannelBuffers;

import io.netty.handler.codec.http2.Http2CodecUtil;

import static java.lang.Math.min;

public class Http2ProtocolDetector implements ProtocolDetector {
    private final ChannelBuffer clientPrefaceString = new ByteBufferBackedChannelBuffer(
            Http2CodecUtil.connectionPrefaceBuf().nioBuffer());

    @Override
    public Result detect(ChannelBuffer in) {
        int prefaceLen = clientPrefaceString.readableBytes();
        int bytesRead = min(in.readableBytes(), prefaceLen);

        // If the input so far doesn't match the preface, break the connection.
        if (bytesRead == 0 || !ChannelBuffers.prefixEquals(in, clientPrefaceString, bytesRead)) {
            return Result.unrecognized();
        }
        if (bytesRead == prefaceLen) {
            return Result.recognized();
        }
        return Result.needMoreData();
    }
}
