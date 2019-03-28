/**
 * Copyright 2016-2019 The Reaktivity Project
 *
 * The Reaktivity Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.reaktivity.specification.socks.internal;

import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.kaazing.k3po.lang.el.Function;
import org.kaazing.k3po.lang.el.spi.FunctionMapperSpi;
import org.reaktivity.specification.socks.internal.types.control.SocksRouteExFW;
import org.reaktivity.specification.socks.internal.types.stream.SocksBeginExFW;

public final class SocksFunctions
{
    private static final int MAX_BUFFER_SIZE = 1024 * 8;

    @Function
    public static byte[] routeEx(
        String address,
        int port)
    {
        final MutableDirectBuffer writeBuffer = new UnsafeBuffer(new byte[MAX_BUFFER_SIZE]);
        final SocksRouteExFW routeEx = new SocksRouteExFW.Builder()
                                                         .wrap(writeBuffer, 0, writeBuffer.capacity())
                                                         .address(address)
                                                         .port(port)
                                                         .build();
        final byte[] bytes = new byte[routeEx.sizeof()];
        routeEx.buffer().getBytes(0, bytes);
        return bytes;
    }

    @Function
    public static byte[] beginEx(
        String address,
        int port)
    {
        final MutableDirectBuffer writeBuffer = new UnsafeBuffer(new byte[MAX_BUFFER_SIZE]);
        final SocksBeginExFW beginEx = new SocksBeginExFW.Builder()
                                                         .wrap(writeBuffer, 0, writeBuffer.capacity())
                                                         .address(address)
                                                         .port(port)
                                                         .build();
        final byte[] bytes = new byte[beginEx.sizeof()];
        beginEx.buffer().getBytes(0, bytes);
        return bytes;
    }

    public static class Mapper extends FunctionMapperSpi.Reflective
    {

        public Mapper()
        {
            super(SocksFunctions.class);
        }

        @Override
        public String getPrefixName()
        {
            return "socks";
        }
    }

    private SocksFunctions()
    {
        // utility
    }
}
