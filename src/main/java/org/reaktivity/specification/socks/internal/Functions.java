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
import org.reaktivity.specification.socks.internal.types.SocksMode;
import org.reaktivity.specification.socks.internal.types.SocksModeFW;
import org.reaktivity.specification.socks.internal.types.String16FW;

public final class Functions
{

    @Function
    public static byte[] mode(String mode)
    {
        MutableDirectBuffer writeBuffer = new UnsafeBuffer(new byte[1024]);
        SocksModeFW modeType = new SocksModeFW.Builder()
                .wrap(writeBuffer, 0, writeBuffer.capacity())
                .set(SocksMode.valueOf(mode))
                .build();
        byte[] modeBytes = new byte[modeType.sizeof()];
        modeType.buffer().getBytes(0, modeBytes);
        return modeBytes;
    }

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
