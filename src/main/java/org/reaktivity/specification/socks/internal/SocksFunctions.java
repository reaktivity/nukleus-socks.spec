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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SocksFunctions
{
    private static final int MAX_BUFFER_SIZE = 1024 * 8;
    private static final Pattern IPV4_ADDRESS_PATTERN =
        Pattern.compile("(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
    private static final ThreadLocal<Matcher> IPV4_ADDRESS_MATCHER =
        ThreadLocal.withInitial(() -> IPV4_ADDRESS_PATTERN.matcher(""));
    private static final Pattern IPV6_ADDRESS_PATTERN =
        Pattern.compile("([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}");
    private static final ThreadLocal<Matcher> IPV6_ADDRESS_MATCHER =
        ThreadLocal.withInitial(() -> IPV6_ADDRESS_PATTERN.matcher(""));

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
        private final SocksRouteExFW.Builder routeExRW;

        private SocksRouteExBuilder()
        {
            MutableDirectBuffer writeBuffer = new UnsafeBuffer(new byte[MAX_BUFFER_SIZE]);
            this.routeExRW = new SocksRouteExFW.Builder().wrap(writeBuffer, 0, writeBuffer.capacity());
        }

        public SocksRouteExBuilder address(
            String address) throws UnknownHostException
        {
            Matcher ipv4GroupMatch = IPV4_ADDRESS_MATCHER.get().reset(address);
            int hit = 0;
            while (ipv4GroupMatch.find() && hit <= 5)
            {
                hit++;
            }
            if (hit == 4)
            {
                final byte[] addressBytes = new byte[4];
                int i = 0;
                ipv4GroupMatch = IPV4_ADDRESS_MATCHER.get().reset(address);
                while (ipv4GroupMatch.find())
                {
                    addressBytes[i++] = (byte) Integer.parseInt(ipv4GroupMatch.group());
                }
                routeExRW.address(b -> b.ipv4Address(s -> s.set(addressBytes)));
            }
            else if (IPV6_ADDRESS_MATCHER.get().reset(address).matches())
            {
                final byte[] addressBytes = InetAddress.getByName(address).getAddress();
                routeExRW.address(b -> b.ipv6Address(s -> s.set(addressBytes)));
            }
            else
            {
                routeExRW.address(b -> b.domainName(address));
            }
            return this;
        }

        public SocksRouteExBuilder port(
            int port)
        {
            this.routeExRW.port(port);
            return this;
        }

        public byte[] build()
        {
            final SocksRouteExFW routeEx = this.routeExRW.build();
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

        public SocksBeginExBuilder typeId(
            int typeId)
        {
            beginExRW.typeId(typeId);
            return this;
        }

        public SocksBeginExBuilder address(
            String address) throws UnknownHostException
        {
            Matcher ipv4GroupMatch = IPV4_ADDRESS_MATCHER.get().reset(address);
            int hit = 0;
            while (ipv4GroupMatch.find() && hit <= 5)
            {
                hit++;
            }

            if (hit == 4)
            {
                final byte[] addressBytes = new byte[4];
                int i = 0;
                ipv4GroupMatch = IPV4_ADDRESS_MATCHER.get().reset(address);
                while (ipv4GroupMatch.find())
                {
                    addressBytes[i++] =(byte) Integer.parseInt(ipv4GroupMatch.group());
                }
                beginExRW.address(b -> b.ipv4Address(s -> s.set(addressBytes)));
            }
            else if (IPV6_ADDRESS_MATCHER.get().reset(address).matches())
            {
                final byte[] addressBytes = InetAddress.getByName(address).getAddress();
                beginExRW.address(b -> b.ipv6Address(s -> s.set(addressBytes)));
            }
            else
            {
                beginExRW.address(b -> b.domainName(address));
            }
            return this;
        }

        public SocksBeginExBuilder port(
            int port)
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
