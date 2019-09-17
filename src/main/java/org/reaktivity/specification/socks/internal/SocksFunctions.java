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

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.kaazing.k3po.lang.el.Function;
import org.kaazing.k3po.lang.el.spi.FunctionMapperSpi;
import org.reaktivity.specification.socks.internal.types.control.SocksRouteExFW;
import org.reaktivity.specification.socks.internal.types.stream.SocksBeginExFW;

public final class SocksFunctions
{
    private static final int MAX_BUFFER_SIZE = 1024 * 8;
    private static final Pattern IPV4_ADDRESS_PATTERN =
        Pattern.compile("(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)" +
                        "\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)" +
                        "\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)" +
                        "\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
    private static final ThreadLocal<Matcher> IPV4_ADDRESS_MATCHER =
        ThreadLocal.withInitial(() -> IPV4_ADDRESS_PATTERN.matcher(""));
    private static final Pattern IPV6_STD_ADDRESS_PATTERN =
        Pattern.compile("([0-9a-f]{1,4})\\:([0-9a-f]{1,4})\\:" +
                        "([0-9a-f]{1,4})\\:([0-9a-f]{1,4})\\:" +
                        "([0-9a-f]{1,4})\\:([0-9a-f]{1,4})\\:" +
                        "([0-9a-f]{1,4})\\:([0-9a-f]{1,4})");
    private static final Pattern IPV6_HEX_COMPRESSED_VALIDATE_PATTERN =
        Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)" +
                        "::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");
    private static final Pattern IPV6_HEX_COMPRESSED_MATCH_PATTERN =
        Pattern.compile("(?:([0-9a-f]{1,4})\\:?)?(?:([0-9a-f]{1,4})\\:?)?" +
                        "(?:([0-9a-f]{1,4})\\:?)?(?:([0-9a-f]{1,4})\\:?)?" +
                        "(?:([0-9a-f]{1,4})\\:?)?(?:([0-9a-f]{1,4})\\:?)?" +
                        "([0-9a-f]{1,4})?(::)" +
                        "(?:(?:([0-9a-f]{1,4})\\:?)??)(?:(?:([0-9a-f]{1,4})\\:?)??)" +
                        "(?:(?:([0-9a-f]{1,4})\\:?)??)(?:(?:([0-9a-f]{1,4})\\:?)??)" +
                        "(?:(?:([0-9a-f]{1,4})\\:?)??)(?:(?:([0-9a-f]{1,4})\\:?)??)" +
                        "(?:(?:([0-9a-f]{1,4})?))");
    private static final Pattern DOMAIN_NAME_MATCH_PATTERN =
        Pattern.compile("(([www])(\\.))?(?:[a-z0-9]((?:[a-z0-9-]{0,61}[a-z0-9])?))(\\.){1}([a-z0-9]{0,6})");
    private static final ThreadLocal<Matcher> IPV6_STD_ADDRESS_MATCHER =
        ThreadLocal.withInitial(() -> IPV6_STD_ADDRESS_PATTERN.matcher(""));
    private static final ThreadLocal<Matcher> IPV6_HEX_COMPRESSED_VALIDATE_MATCHER =
        ThreadLocal.withInitial(() -> IPV6_HEX_COMPRESSED_VALIDATE_PATTERN.matcher(""));
    private static final ThreadLocal<Matcher> IPV6_HEX_COMPRESSED_MATCHER =
        ThreadLocal.withInitial(() -> IPV6_HEX_COMPRESSED_MATCH_PATTERN.matcher(""));
    private static final ThreadLocal<Matcher> DOMAIN_NAME_MATCHER =
        ThreadLocal.withInitial(() -> DOMAIN_NAME_MATCH_PATTERN.matcher(""));
    private static final ThreadLocal<byte[]> IPV4_ADDRESS_BYTES =
        ThreadLocal.withInitial(() -> new byte[4]);
    private static final ThreadLocal<byte[]> IPV6_ADDRESS_BYTES =
        ThreadLocal.withInitial(() -> new byte[16]);

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
            if (IPV4_ADDRESS_MATCHER.get().reset(address).matches())
            {
                final Matcher ipv4Matcher = IPV4_ADDRESS_MATCHER.get();
                final byte[] ipv4AddressBytes = IPV4_ADDRESS_BYTES.get();
                encodeIpv4AddressBytes(ipv4Matcher, ipv4AddressBytes);
                routeExRW.address(b -> b.ipv4Address(s -> s.set(ipv4AddressBytes)));
            }
            else if (IPV6_STD_ADDRESS_MATCHER.get().reset(address).matches())
            {
                final byte[] addressBytes = IPV6_ADDRESS_BYTES.get();
                final Matcher ipv6Matcher = IPV6_STD_ADDRESS_MATCHER.get();
                encodeStandardIpv6AddressBytes(ipv6Matcher, addressBytes);
                routeExRW.address(b -> b.ipv6Address(s -> s.set(addressBytes)));
            }
            else if (IPV6_HEX_COMPRESSED_VALIDATE_MATCHER.get().reset(address).matches() &&
                IPV6_HEX_COMPRESSED_MATCHER.get().reset(address).matches())
            {
                final byte[] addressBytes = IPV6_ADDRESS_BYTES.get();
                final Matcher ipv6Matcher = IPV6_HEX_COMPRESSED_MATCHER.get();
                encodeCompressedIpv6AddressBytes(ipv6Matcher, addressBytes);
                routeExRW.address(b -> b.ipv6Address(s -> s.set(addressBytes)));
            }
            else if (DOMAIN_NAME_MATCHER.get().reset(address).matches())
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
            if (IPV4_ADDRESS_MATCHER.get().reset(address).matches())
            {
                final Matcher ipv4Matcher = IPV4_ADDRESS_MATCHER.get();
                final byte[] ipv4AddressBytes = IPV4_ADDRESS_BYTES.get();
                encodeIpv4AddressBytes(ipv4Matcher, ipv4AddressBytes);
                beginExRW.address(b -> b.ipv4Address(s -> s.set(ipv4AddressBytes)));
            }
            else if (IPV6_STD_ADDRESS_MATCHER.get().reset(address).matches())
            {
                final byte[] addressBytes = IPV6_ADDRESS_BYTES.get();
                final Matcher ipv6Matcher = IPV6_STD_ADDRESS_MATCHER.get();
                encodeStandardIpv6AddressBytes(ipv6Matcher, addressBytes);
                beginExRW.address(b -> b.ipv6Address(s -> s.set(addressBytes)));
            }
            else if (IPV6_HEX_COMPRESSED_VALIDATE_MATCHER.get().reset(address).matches() &&
                IPV6_HEX_COMPRESSED_MATCHER.get().reset(address).matches())
            {
                final byte[] addressBytes = IPV6_ADDRESS_BYTES.get();
                final Matcher ipv6Matcher = IPV6_HEX_COMPRESSED_MATCHER.get();
                encodeCompressedIpv6AddressBytes(ipv6Matcher, addressBytes);
                beginExRW.address(b -> b.ipv6Address(s -> s.set(addressBytes)));
            }
            else if (DOMAIN_NAME_MATCHER.get().reset(address).matches())
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

    private static void fillInBytes(
        byte[] addressBytes,
        int index,
        String groupHex)
    {
        short groupBytes = parseShort(groupHex, 16);
        addressBytes[index++] = (byte) ((groupBytes >> 8) & 0xff);
        addressBytes[index] = (byte) (groupBytes & 0xff);
    }

    private static void encodeCompressedIpv6AddressBytes(
        Matcher ipv6Matcher,
        byte[] addressBytes)
    {
        Arrays.fill(addressBytes, (byte) 0);
        for (int group = 1; group < 8; group++)
        {
            String groupHex = ipv6Matcher.group(group);
            if (groupHex == null)
            {
                break;
            }
            fillInBytes(addressBytes, 2 * (group - 1), groupHex);
        }
        for (int group = 15; group > 8; group--)
        {
            String groupHex = ipv6Matcher.group(group);
            if (groupHex == null)
            {
                break;
            }
            fillInBytes(addressBytes, 2 * (group - 8), groupHex);
        }
    }

    public static byte parseByte(
        String s,
        int radix)
    {
        assert s.length() > 0 && s.length() <= 3;
        return (byte) Integer.parseInt(s, radix);
    }

    public static short parseShort(
        String s,
        int radix)
    {
        assert s.length() > 0 && s.length() <= 4;
        return (short) Integer.parseInt(s, radix);
    }

    private static void encodeStandardIpv6AddressBytes(
        Matcher ipv6Matcher,
        byte[] addressBytes)
    {
        for (int group = 1; group < ipv6Matcher.groupCount() + 1; group++)
        {
            String groupHex = ipv6Matcher.group(group);
            fillInBytes(addressBytes, 2 * (group - 1), groupHex);
        }
    }

    private static void encodeIpv4AddressBytes(
        Matcher ipv4Matcher,
        byte[] ipv4AddressBytes)
    {
        for (int group = 1; group < ipv4AddressBytes.length + 1; group++)
        {
            ipv4AddressBytes[group - 1] = parseByte(ipv4Matcher.group(group), 10);
        }
    }

    private SocksFunctions()
    {
        // utility
    }
}
