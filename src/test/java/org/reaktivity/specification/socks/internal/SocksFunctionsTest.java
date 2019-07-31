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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.kaazing.k3po.lang.internal.el.ExpressionFactoryUtils.newExpressionFactory;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.junit.Before;
import org.junit.Test;
import org.kaazing.k3po.lang.internal.el.ExpressionContext;
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
    /**
    @Test
    public void shouldLoadFunctions() throws Exception
    {
        String expressionText = "${socks:routeEx('example.com', 8080)}";
        ValueExpression expression = factory.createValueExpression(ctx, expressionText, String.class);
        String routeExBytes = (String) expression.getValue(ctx);
        assertNotNull(routeExBytes);
    }

    @Test
    public void shouldBuildRouteEx() throws Exception
    {
        byte[] bytes = SocksFunctions.routeEx("example.com", 8080);
        DirectBuffer buffer = new UnsafeBuffer(bytes);
        SocksRouteExFW routeEx = new SocksRouteExFW().wrap(buffer, 0, buffer.capacity());

        assertEquals("example.com", routeEx.address().asString());
     }
      **/
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
    public void shouldBuildRouteEx() throws Exception
    {
        byte[] bytes = SocksFunctions.routeEx().address("example.com").port(8080).build();
        DirectBuffer buffer = new UnsafeBuffer(bytes);
        SocksRouteExFW routeEx = new SocksRouteExFW().wrap(buffer, 0, buffer.capacity());

        assertEquals("example.com", routeEx.address().asString());
        assertEquals(8080, routeEx.port());
    }


    @Test
    public void shouldBuildBeginEx() throws Exception
    {
        byte[] bytes = SocksFunctions.beginEx().address("example.com").port(8080).build();
        DirectBuffer buffer = new UnsafeBuffer(bytes);
        SocksBeginExFW beginEx = new SocksBeginExFW().wrap(buffer, 0, buffer.capacity());

        assertEquals("example.com", beginEx.address().asString());
        assertEquals(8080, beginEx.port());
    }
}
