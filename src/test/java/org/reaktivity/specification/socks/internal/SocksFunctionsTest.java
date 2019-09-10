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

import static java.util.Arrays.copyOfRange;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.kaazing.k3po.lang.internal.el.ExpressionFactoryUtils.newExpressionFactory;
import static org.reaktivity.specification.socks.internal.types.SocksAddressFW.KIND_IPV4_ADDRESS;
import static org.reaktivity.specification.socks.internal.types.SocksAddressFW.KIND_IPV6_ADDRESS;

import java.math.BigInteger;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.junit.Before;
import org.junit.Test;
import org.kaazing.k3po.lang.internal.el.ExpressionContext;
import org.reaktivity.specification.socks.internal.types.OctetsFW;
import org.reaktivity.specification.socks.internal.types.SocksAddressFW;
import org.reaktivity.specification.socks.internal.types.control.SocksRouteExFW;
import org.reaktivity.specification.socks.internal.types.stream.SocksBeginExFW;

public class SocksFunctionsTest
{
    private ExpressionFactory factory;
    private ELContext ctx;

    @Before
    public void setUp() throws Exception
    {
        factory = newExpressionFactory();
        ctx = new ExpressionContext();
    }

    @Test
    public void shouldLoadFunctions() throws Exception
    {
        String expressionText = "${socks:routeEx()" +
                                       ".address(\"example.com\")" +
                                       ".port(8080)" +
                                       ".build()}";
        ValueExpression expression = factory.createValueExpression(ctx, expressionText, String.class);
        String routeExBytes = (String) expression.getValue(ctx);

        assertNotNull(routeExBytes);
    }

    @Test
    public void shouldBuildRouteExWithDomainName() throws Exception
    {
        byte[] bytes = SocksFunctions.routeEx()
                                     .address("example.com")
                                     .port(8080)
                                     .build();
        DirectBuffer buffer = new UnsafeBuffer(bytes);
        SocksRouteExFW routeEx = new SocksRouteExFW().wrap(buffer, 0, buffer.capacity());

        assertEquals("example.com", routeEx.address().domainName().asString());
        assertEquals(8080, routeEx.port());
    }

    @Test
    public void shouldBuildBeginExWithDomainName() throws Exception
    {
        byte[] bytes = SocksFunctions.beginEx()
                                     .typeId(0)
                                     .address("example.com")
                                     .port(8080)
                                     .build();
        DirectBuffer buffer = new UnsafeBuffer(bytes);
        SocksBeginExFW beginEx = new SocksBeginExFW().wrap(buffer, 0, buffer.capacity());

        assertEquals("example.com", beginEx.address().domainName().asString());
        assertEquals(8080, beginEx.port());
    }

    @Test
    public void shouldBuildRouteExWithIpv4Address() throws Exception
    {
        byte[] bytes = SocksFunctions.routeEx()
                                     .address("127.0.0.1")
                                     .port(8080)
                                     .build();
        DirectBuffer buffer = new UnsafeBuffer(bytes);
        SocksRouteExFW routeEx = new SocksRouteExFW().wrap(buffer, 0, buffer.capacity());
        SocksAddressFW address = routeEx.address();

        assertEquals(KIND_IPV4_ADDRESS, address.kind());
        OctetsFW ipv4Address = address.ipv4Address();
        assertArrayEquals(new byte[] { 127, 0, 0, 1 },
            copyOfRange(ipv4Address.buffer().byteArray(),
                        ipv4Address.offset(),
                        ipv4Address.limit()));
        assertEquals(8080, routeEx.port());
    }

    @Test
    public void shouldBuildBeginExWithIpv4Address() throws Exception
    {
        byte[] bytes = SocksFunctions.beginEx()
                                     .typeId(0)
                                     .address("127.0.0.1")
                                     .port(8080)
                                     .build();
        DirectBuffer buffer = new UnsafeBuffer(bytes);
        SocksBeginExFW beginEx = new SocksBeginExFW().wrap(buffer, 0, buffer.capacity());
        SocksAddressFW address = beginEx.address();

        assertEquals(KIND_IPV4_ADDRESS, address.kind());
        OctetsFW ipv4Address = address.ipv4Address();
        assertArrayEquals(new byte[] { 127, 0, 0, 1 },
            copyOfRange(ipv4Address.buffer().byteArray(),
                        ipv4Address.offset(),
                        ipv4Address.limit()));
        assertEquals(8080, beginEx.port());
    }

    @Test
    public void shouldBuildRouteExWithIpv6Address() throws Exception
    {
        byte[] bytes = SocksFunctions.routeEx()
                                     .address("2001:0db8:85a3:0000:0000:8a2e:0370:7334")
                                     .port(8080)
                                     .build();
        DirectBuffer buffer = new UnsafeBuffer(bytes);
        SocksRouteExFW routeEx = new SocksRouteExFW().wrap(buffer, 0, buffer.capacity());
        SocksAddressFW address = routeEx.address();

        assertEquals(KIND_IPV6_ADDRESS, routeEx.address().kind());
        OctetsFW ipv6Address = address.ipv6Address();
        assertArrayEquals(new BigInteger("20010db885a3000000008a2e03707334", 16).toByteArray(),
            copyOfRange(ipv6Address.buffer().byteArray(),
                        ipv6Address.offset(),
                        ipv6Address.limit()));
        assertEquals(8080, routeEx.port());
    }

    @Test
    public void shouldBuildRouteExWithIpv6AddressLeadingZeros() throws Exception
    {
        byte[] bytes = SocksFunctions.routeEx()
            .address("2001:0db8:85a3:0:0:8a2e:0370:7334")
            .port(8080)
            .build();
        DirectBuffer buffer = new UnsafeBuffer(bytes);
        SocksRouteExFW routeEx = new SocksRouteExFW().wrap(buffer, 0, buffer.capacity());
        SocksAddressFW address = routeEx.address();

        assertEquals(KIND_IPV6_ADDRESS, routeEx.address().kind());
        OctetsFW ipv6Address = address.ipv6Address();
        assertArrayEquals(new BigInteger("20010db885a3000000008a2e03707334", 16).toByteArray(),
            copyOfRange(ipv6Address.buffer().byteArray(),
                ipv6Address.offset(),
                ipv6Address.limit()));
        assertEquals(8080, routeEx.port());
    }

    @Test
    public void shouldBuildRouteExWithIpv6AddressZeroCompression1() throws Exception
    {
        byte[] bytes = SocksFunctions.routeEx()
            .address("2001:0db8:85a3::8a2e:0370:7334")
            .port(8080)
            .build();
        DirectBuffer buffer = new UnsafeBuffer(bytes);
        SocksRouteExFW routeEx = new SocksRouteExFW().wrap(buffer, 0, buffer.capacity());
        SocksAddressFW address = routeEx.address();

        assertEquals(KIND_IPV6_ADDRESS, routeEx.address().kind());
        OctetsFW ipv6Address = address.ipv6Address();
        assertArrayEquals(new BigInteger("20010db885a3000000008a2e03707334", 16).toByteArray(),
            copyOfRange(ipv6Address.buffer().byteArray(),
                ipv6Address.offset(),
                ipv6Address.limit()));
        assertEquals(8080, routeEx.port());
    }


    @Test
    public void shouldBuildBeginExWithIpv6Address() throws Exception
    {
        byte[] bytes = SocksFunctions.beginEx()
                                     .typeId(0)
                                     .address("2001:0db8:85a3:0000:0000:8a2e:0370:7334")
                                     .port(8080)
                                     .build();
        DirectBuffer buffer = new UnsafeBuffer(bytes);
        SocksBeginExFW beginEx = new SocksBeginExFW().wrap(buffer, 0, buffer.capacity());
        SocksAddressFW address = beginEx.address();

        assertEquals(KIND_IPV6_ADDRESS, beginEx.address().kind());
        OctetsFW ipv6Address = address.ipv6Address();
        assertArrayEquals(new BigInteger("20010db885a3000000008a2e03707334", 16).toByteArray(),
            copyOfRange(ipv6Address.buffer().byteArray(),
                        ipv6Address.offset(),
                        ipv6Address.limit()));
        assertEquals(8080, beginEx.port());
    }
}
