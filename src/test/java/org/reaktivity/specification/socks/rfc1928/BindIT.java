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
package org.reaktivity.specification.socks.rfc1928;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.rules.RuleChain.outerRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;

public class BindIT
{
    private final K3poRule k3po = new K3poRule()
        .addScriptRoot("scripts", "org/reaktivity/specification/socks/rfc1928/bind");

    private final TestRule timeout = new DisableOnDebug(new Timeout(10, SECONDS));

    @Rule
    public final TestRule chain = outerRule(k3po).around(timeout);

    @Test
    @Specification({
        "${scripts}/accepted.domain/client",
        "${scripts}/accepted.domain/server"})
    public void shouldAcceptDomain() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.ipv4/client",
        "${scripts}/accepted.ipv4/server"})
    public void shouldAcceptIPv4() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.ipv6/client",
        "${scripts}/accepted.ipv6/server"})
    public void shouldAcceptIPv6() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.client.abort/client",
        "${scripts}/accepted.then.client.abort/server"})
    public void shouldAcceptThenClientAborts() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.client.close/client",
        "${scripts}/accepted.then.client.close/server"})
    public void shouldAcceptThenClientCloses() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.client.reset/client",
        "${scripts}/accepted.then.client.reset/server"})
    public void shouldAcceptThenClientResets() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.client.write.data/client",
        "${scripts}/accepted.then.client.write.data/server"})
    public void shouldAcceptThenClientWritesData() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.server.abort/client",
        "${scripts}/accepted.then.server.abort/server"})
    public void shouldAcceptThenServerAborts() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.server.close/client",
        "${scripts}/accepted.then.server.close/server"})
    public void shouldAcceptThenServerCloses() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.server.reset/client",
        "${scripts}/accepted.then.server.reset/server"})
    public void shouldAcceptThenServerResets() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.server.write.data/client",
        "${scripts}/accepted.then.server.write.data/server"})
    public void shouldAcceptThenServerWritesData() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/bound.domain/client",
        "${scripts}/bound.domain/server"})
    public void shouldBindDomain() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/bound.ipv4/client",
        "${scripts}/bound.ipv4/server"})
    public void shouldBindIPv4() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/bound.ipv6/client",
        "${scripts}/bound.ipv6/server"})
    public void shouldBindIPv6() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/bound.then.client.abort/client",
        "${scripts}/bound.then.client.abort/server"})
    public void shouldBindThenClientAborts() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/bound.then.client.close/client",
        "${scripts}/bound.then.client.close/server"})
    public void shouldBindThenClientCloses() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/bound.then.client.reset/client",
        "${scripts}/bound.then.client.reset/server"})
    public void shouldBindThenClientResets() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/bound.then.server.abort/client",
        "${scripts}/bound.then.server.abort/server"})
    public void shouldBindThenServerAborts() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/bound.then.server.close/client",
        "${scripts}/bound.then.server.close/server"})
    public void shouldBindThenServerCloses() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/bound.then.server.reset/client",
        "${scripts}/bound.then.server.reset/server"})
    public void shouldBindThenServerResets() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/rejected.address.type.not.supported/client",
        "${scripts}/rejected.address.type.not.supported/server"})
    public void shouldRejectWithAddressTypeNotSupported() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }
}
