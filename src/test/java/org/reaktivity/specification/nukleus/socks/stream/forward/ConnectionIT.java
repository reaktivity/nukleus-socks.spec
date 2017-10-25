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
package org.reaktivity.specification.nukleus.socks.stream.forward;

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

public class ConnectionIT
{
    private final K3poRule k3po = new K3poRule()
        .addScriptRoot("scripts", "org/reaktivity/specification/nukleus/socks/streams/forward");

    private final TestRule timeout = new DisableOnDebug(new Timeout(10, SECONDS));

    @Rule
    public final TestRule chain = outerRule(k3po).around(timeout);

    @Test
    @ScriptProperty("serverAccept \"nukleus://socks/streams/source\"")
    @Specification({
        "${scripts}/client.connect.send.data/client",
        "${scripts}/client.connect.send.data/server"})
    public void shouldEstablishConnection() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT");
        k3po.finish();
    }

    @Test
    @ScriptProperty("serverAccept \"nukleus://socks/streams/source\"")
    @Specification({
        "${scripts}/client.connect.send.data.throttling.server.smaller/client",
        "${scripts}/client.connect.send.data.throttling.server.smaller/server"})
    public void shouldEstablishConnectionWithThrottlingServerSmaller() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT");
        k3po.finish();
    }

    @Test
    @ScriptProperty("serverAccept \"nukleus://socks/streams/source\"")
    @Specification({
        "${scripts}/client.connect.send.data.throttling.client.smaller/client",
        "${scripts}/client.connect.send.data.throttling.client.smaller/server"})
    public void shouldEstablishConnectionWithThrottlingClientSmaller() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT");
        k3po.finish();
    }


    @Test
    @ScriptProperty("serverAccept \"nukleus://socks/streams/source\"")
    @Specification({
        "${scripts}/client.connect.send.data.throttling.window.1/client",
        "${scripts}/client.connect.send.data.throttling.window.1/server"})
    public void shouldEstablishConnectionWithThrottlingWindow1() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT");
        k3po.finish();
    }

    @Test
    @ScriptProperty("serverAccept \"nukleus://socks/streams/source\"")
    @Specification({
        "${scripts}/client.does.not.connect.no.acceptable.methods/client",
        "${scripts}/client.does.not.connect.no.acceptable.methods/server"})
    public void shouldNotEstablishConnectionNoAcceptableMethods() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT");
        k3po.finish();
    }

    @Test
    @ScriptProperty("serverAccept \"nukleus://socks/streams/source\"")
    @Specification({
        "${scripts}/client.connect.fallback.to.no.authentication/client",
        "${scripts}/client.connect.fallback.to.no.authentication/server"})
    public void shouldEstablishConnectionFallbackToNoAuthentication() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT");
        k3po.finish();
    }

    @Test
    @ScriptProperty("serverAccept \"nukleus://socks/streams/source\"")
    @Specification({
        "${scripts}/client.connect.request.with.command.not.supported/client",
        "${scripts}/client.connect.request.with.command.not.supported/server"})
    public void shouldNotEstablishConnectionCommandNotSupported() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_CLIENT");
        k3po.finish();
    }
}
