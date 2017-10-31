/**
 * Copyright 2016-2017 The Reaktivity Project
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

import java.nio.charset.StandardCharsets;

import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.kaazing.k3po.lang.el.Function;
import org.kaazing.k3po.lang.el.spi.FunctionMapperSpi;
import org.reaktivity.specification.socks.internal.types.String16FW;
import org.reaktivity.specification.socks.internal.types.stream.TcpBeginExFW;

public final class Functions
{

    @Function
    public static byte[] destAddrPort(String destAddrPort)
    {
        MutableDirectBuffer writeBuffer = new UnsafeBuffer(new byte[1024]);
        String16FW dapType = new String16FW.Builder()
            .wrap(writeBuffer, 0, writeBuffer.capacity())
            .set(destAddrPort, StandardCharsets.UTF_8)
            .build();
        byte[] dapBytes = new byte[dapType.sizeof()];
        dapType.buffer().getBytes(0, dapBytes);
        return dapBytes;
    }

    /**
     * Builds the TcpBeginExFW from given IPv4 addresses
     * FIXME Should be implemented in tcp.spec nukleus, with tests on both client and server.
     * FIXME Implementation should pass these tests.
     *
     * @param localAddress
     * @param localPort
     * @param remoteAddress
     * @param remotePort
     * @return A byte array representing the extension ready to be read or written
     */
    @Function
    public static byte[] beginTcpExtensionIpv4(
        byte[] localAddress,
        int localPort,
        byte[] remoteAddress,
        int remotePort)
    {
        MutableDirectBuffer writeBuffer = new UnsafeBuffer(new byte[1024]);
        TcpBeginExFW beginExFW = new TcpBeginExFW.Builder()
            .wrap(writeBuffer, 0, writeBuffer.capacity())
            .localAddress(builderType -> builderType.ipv4Address(builderData -> builderData.set(localAddress)))
            .localPort(localPort)
            .remoteAddress(builderType -> builderType.ipv4Address(builderData -> builderData.set(remoteAddress)))
            .remotePort(remotePort)
            .build();
        byte[] tcpBeginExBytes = new byte[beginExFW.sizeof()];
        beginExFW.buffer().getBytes(0, tcpBeginExBytes);
        return tcpBeginExBytes;
    }

    /**
     * Builds the TcpBeginExFW from given IPv6 addresses
     * FIXME Should be implemented in tcp.spec nukleus, with tests on both client and server.
     * FIXME Implementation should pass these tests.
     *
     * @param localAddress
     * @param localPort
     * @param remoteAddress
     * @param remotePort
     * @return
     */
    @Function
    public static byte[] beginTcpExtensionIpv6(
        byte[] localAddress,
        int localPort,
        byte[] remoteAddress,
        int remotePort)
    {
        MutableDirectBuffer writeBuffer = new UnsafeBuffer(new byte[1024]);
        TcpBeginExFW beginExFW = new TcpBeginExFW.Builder()
            .wrap(writeBuffer, 0, writeBuffer.capacity())
            .localAddress(builderType -> builderType.ipv6Address(builderData -> builderData.set(localAddress)))
            .localPort(localPort)
            .remoteAddress(builderType -> builderType.ipv6Address(builderData -> builderData.set(remoteAddress)))
            .remotePort(remotePort)
            .build();
        byte[] tcpBeginExBytes = new byte[beginExFW.sizeof()];
        beginExFW.buffer()
            .getBytes(0, tcpBeginExBytes);
        return tcpBeginExBytes;
    }

    /**
     * Generates a text by concatenating the pattern: LongText110000000010 and truncating if the length is not multiple of 20.
     * @param length The length of text to generate
     * @return
     */
    @Function
    public static String generateLongText(int length)
    {
        String pattern = "LongText110000000010";
        if (length <= pattern.length())
        {
            return pattern.substring(0, length);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length / pattern.length(); i++)
        {
            sb.append(pattern);
        }
        if (length % pattern.length() > 0)
        {
            sb.append(pattern.substring(0, length % pattern.length()));
        }
        return sb.toString();
    }

    public static class Mapper extends FunctionMapperSpi.Reflective
    {

        public Mapper()
        {
            super(Functions.class);
        }

        @Override
        public String getPrefixName()
        {
            return "socks";
        }

    }

    private Functions()
    {
        // utility
    }
}
