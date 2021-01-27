/**
 * Copyright 2016-2021 The Reaktivity Project
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
package org.reaktivity.specification.nukleus.socks.control;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.rules.RuleChain.outerRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;

public class ControlIT
{
    private final K3poRule k3po = new K3poRule();

    private final TestRule timeout = new DisableOnDebug(new Timeout(5, SECONDS));

    @Rule
    public final TestRule chain = outerRule(k3po).around(timeout);

    @Test
    @Specification({
        "route/server/routed.domain/nukleus",
        "route/server/routed.domain/controller"
    })
    public void shouldRouteServerWithDomainAddress() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server/routed.ipv4/nukleus",
        "route/server/routed.ipv4/controller"
    })
    public void shouldRouteServerWithIPv4Address() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server/routed.ipv6/nukleus",
        "route/server/routed.ipv6/controller"
    })
    public void shouldRouteServerWithIPv6Address() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/client/routed.domain/nukleus",
        "route/client/routed.domain/controller"
    })
    public void shouldRouteClientWithDomainAddress() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/client/routed.ipv4/nukleus",
        "route/client/routed.ipv4/controller"
    })
    public void shouldRouteClientWithIPv4Address() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/client/routed.ipv6/nukleus",
        "route/client/routed.ipv6/controller"
    })
    public void shouldRouteClientWithIPv6Address() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server.reverse/routed.domain/nukleus",
        "route/server.reverse/routed.domain/controller"
    })
    public void shouldRouteReverseServerWithDomainAddress() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server.reverse/routed.ipv4/nukleus",
        "route/server.reverse/routed.ipv4/controller"
    })
    public void shouldRouteReverseServerWithIPv4Address() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server.reverse/routed.ipv6/nukleus",
        "route/server.reverse/routed.ipv6/controller"
    })
    public void shouldRouteReverseServerWithIPv6Address() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/client.reverse/routed.domain/nukleus",
        "route/client.reverse/routed.domain/controller"
    })
    public void shouldRouteReverseClientWithDomainAddress() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/client.reverse/routed.ipv4/nukleus",
        "route/client.reverse/routed.ipv4/controller"
    })
    public void shouldRouteReverseClientWithIPv4Address() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/client.reverse/routed.ipv6/nukleus",
        "route/client.reverse/routed.ipv6/controller"
    })
    public void shouldRouteReverseClientWithIPv6Address() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "unroute/server/nukleus",
        "unroute/server/controller"
    })
    public void shouldUnrouteServer() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "unroute/unknown/nukleus",
        "unroute/unknown/controller"
    })
    public void shouldNotUnrouteUnknown() throws Exception
    {
        k3po.start();
        k3po.finish();
    }

    @Test
    @Specification({
        "unroute/client/nukleus",
        "unroute/client/controller"
    })
    public void shouldUnrouteClient() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT");
        k3po.finish();
    }

    @Test
    @Specification({
        "unroute/server.reverse/nukleus",
        "unroute/server.reverse/controller"
    })
    public void shouldUnrouteReverseServer() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER_REVERSE");
        k3po.finish();
    }

    @Test
    @Specification({
        "unroute/client.reverse/nukleus",
        "unroute/client.reverse/controller"
    })
    public void shouldUnrouteReverseClient() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT_REVERSE");
        k3po.finish();
    }
}
