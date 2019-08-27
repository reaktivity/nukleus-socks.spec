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
package org.reaktivity.specification.nukleus.socks.stream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.rules.RuleChain.outerRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.ScriptProperty;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;

public class ReverseIT
{
    private final K3poRule k3po = new K3poRule()
        .addScriptRoot("scripts", "org/reaktivity/specification/nukleus/socks/streams/reverse");

    private final TestRule timeout = new DisableOnDebug(new Timeout(10, SECONDS));

    @Rule
    public final TestRule chain = outerRule(k3po).around(timeout);

    @Test
    @Specification({
        "${scripts}/accepted.domain/client",
        "${scripts}/accepted.domain/server"})
    @ScriptProperty("serverAccept \"nukleus://streams/socks#0\"")
    public void shouldConnectDomain() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT_REVERSE");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.ipv4/client",
        "${scripts}/accepted.ipv4/server"})
    @ScriptProperty("serverAccept \"nukleus://streams/socks#0\"")
    public void shouldConnectIPv4() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT_REVERSE");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.ipv6/client",
        "${scripts}/accepted.ipv6/server"})
    @ScriptProperty("serverAccept \"nukleus://streams/socks#0\"")
    public void shouldConnectIPv6() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT_REVERSE");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.client.abort/client",
        "${scripts}/accepted.then.client.abort/server"})
    @ScriptProperty("serverAccept \"nukleus://streams/socks#0\"")
    public void shouldConnectThenClientAborts() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT_REVERSE");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.client.close/client",
        "${scripts}/accepted.then.client.close/server"})
    @ScriptProperty("serverAccept \"nukleus://streams/socks#0\"")
    public void shouldConnectThenClientCloses() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT_REVERSE");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.client.reset/client",
        "${scripts}/accepted.then.client.reset/server"})
    @ScriptProperty("serverAccept \"nukleus://streams/socks#0\"")
    public void shouldConnectThenClientResets() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT_REVERSE");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.client.write.data/client",
        "${scripts}/accepted.then.client.write.data/server"})
    @ScriptProperty("serverAccept \"nukleus://streams/socks#0\"")
    public void shouldConnectThenClientWritesData() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT_REVERSE");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.server.abort/client",
        "${scripts}/accepted.then.server.abort/server"})
    @ScriptProperty("serverAccept \"nukleus://streams/socks#0\"")
    public void shouldConnectThenServerAborts() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT_REVERSE");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.server.close/client",
        "${scripts}/accepted.then.server.close/server"})
    @ScriptProperty("serverAccept \"nukleus://streams/socks#0\"")
    public void shouldConnectThenServerCloses() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT_REVERSE");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.server.reset/client",
        "${scripts}/accepted.then.server.reset/server"})
    @ScriptProperty("serverAccept \"nukleus://streams/socks#0\"")
    public void shouldConnectThenServerResets() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT_REVERSE");
        k3po.finish();
    }

    @Test
    @Specification({
        "${scripts}/accepted.then.server.write.data/client",
        "${scripts}/accepted.then.server.write.data/server"})
    @ScriptProperty("serverAccept \"nukleus://streams/socks#0\"")
    public void shouldConnectThenServerWritesData() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT_REVERSE");
        k3po.finish();
    }
}
