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
    public static SocksRouteExBuilder routeEx()
    {
        return new SocksRouteExBuilder();
    }

    @Function
    public static SocksBeginExBuilder beginEx()
    {
        return new SocksBeginExBuilder();
    }


    public static final class SocksRouteExBuilder
    {
        private final SocksRouteExFW.Builder routeExRw;

        private SocksRouteExBuilder()
        {
            MutableDirectBuffer writeBuffer = new UnsafeBuffer(new byte[MAX_BUFFER_SIZE]);
            this.routeExRw = new SocksRouteExFW.Builder().wrap(writeBuffer, 0, writeBuffer.capacity());
        }

        public SocksRouteExBuilder address(String address)
        {
            this.routeExRw.address(address);
            return this;
        }

        public SocksRouteExBuilder port(int port)
        {
            this.routeExRw.port(port);
            return this;
        }

        public byte[] build()
        {
            final SocksRouteExFW routeEx = this.routeExRw.build();
            final byte[] array = new byte[routeEx.sizeof()];
            routeEx.buffer().getBytes(0, array);
            return array;
        }
    }

    public static final class SocksBeginExBuilder
    {
        private final SocksBeginExFW.Builder beginExRW;

        private SocksBeginExBuilder()
        {
            MutableDirectBuffer writeBuffer = new UnsafeBuffer(new byte[MAX_BUFFER_SIZE]);
            this.beginExRW = new SocksBeginExFW.Builder().wrap(writeBuffer, 0, writeBuffer.capacity());
        }

        public SocksBeginExBuilder typeId(int typeId)
        {
            beginExRW.typeId(typeId);
            return this;
        }

        public SocksBeginExBuilder address(String address)
        {
            this.beginExRW.address(address);
            return this;
        }

        public SocksBeginExBuilder port(int port)
        {
            this.beginExRW.port(port);
            return this;
        }

        public byte[] build()
        {
            final SocksBeginExFW beginEx = beginExRW.build();
            final byte[] array = new byte[beginEx.sizeof()];
            beginEx.buffer().getBytes(0, array);
            return array;
        }
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


