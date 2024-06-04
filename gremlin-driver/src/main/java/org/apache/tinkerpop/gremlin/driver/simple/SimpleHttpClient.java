/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tinkerpop.gremlin.driver.simple;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.tinkerpop.gremlin.driver.RequestInterceptor;
import org.apache.tinkerpop.gremlin.driver.handler.HttpGremlinResponseStreamDecoder;
import org.apache.tinkerpop.gremlin.driver.handler.HttpGremlinRequestEncoder;
import org.apache.tinkerpop.gremlin.util.MessageSerializerV4;
import org.apache.tinkerpop.gremlin.util.message.RequestMessageV4;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import org.apache.tinkerpop.gremlin.util.ser.GraphBinaryMessageSerializerV4;
import org.apache.tinkerpop.gremlin.structure.io.binary.GraphBinaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A simple, non-thread safe Gremlin Server client using HTTP. Typical use is for testing and demonstration.
 */
public class SimpleHttpClient extends AbstractClient {
    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpClient.class);
    private final Channel channel;

    public SimpleHttpClient() {
        this(URI.create("http://localhost:8182/gremlin"));
    }

    public SimpleHttpClient(final URI uri) {
        super("simple-http-client-%d");
        final Bootstrap b = new Bootstrap().group(group);
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        final String protocol = uri.getScheme();
        if (!"http".equalsIgnoreCase(protocol) && !"https".equalsIgnoreCase(protocol))
            throw new IllegalArgumentException("Unsupported protocol: " + protocol);
        final String host = uri.getHost();
        final int port;
        if (uri.getPort() == -1) {
            if ("http".equalsIgnoreCase(protocol)) {
                port = 80;
            } else if ("https".equalsIgnoreCase(protocol)) {
                port = 443;
            } else {
                port = -1;
            }
        } else {
            port = uri.getPort();
        }

        try {
            final boolean ssl = "https".equalsIgnoreCase(protocol);
            final SslContext sslCtx;
            if (ssl) {
                sslCtx = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            } else {
                sslCtx = null;
            }

            final MessageSerializerV4<GraphBinaryMapper> serializer = new GraphBinaryMessageSerializerV4();
            b.channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(final SocketChannel ch) {
                            final ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                            }
                            p.addLast(
                                    new HttpClientCodec(),
                                    new HttpGremlinResponseStreamDecoder(serializer, Integer.MAX_VALUE),
                                    new HttpGremlinRequestEncoder(serializer, new ArrayList<>(), false),

                                    callbackResponseHandler);
                        }
                    });

            channel = b.connect(uri.getHost(), uri.getPort()).sync().channel();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void writeAndFlush(final RequestMessageV4 requestMessage) throws Exception {
        channel.writeAndFlush(requestMessage);
    }

    @Override
    public void close() throws IOException {
        try {
            channel.close().get(30, TimeUnit.SECONDS);
        } catch (Exception ex) {
            logger.error("Failure closing simple WebSocketClient", ex);
        } finally {
            if (!group.shutdownGracefully().awaitUninterruptibly(30, TimeUnit.SECONDS)) {
                logger.error("Could not cleanly shutdown thread pool on WebSocketClient");
            }
        }
    }
}
