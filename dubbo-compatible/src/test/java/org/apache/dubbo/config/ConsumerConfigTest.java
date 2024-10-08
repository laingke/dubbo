/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.config;

import org.apache.dubbo.common.utils.SystemPropertyConfigUtils;

import com.alibaba.dubbo.config.ConsumerConfig;
import org.junit.jupiter.api.Test;

import static org.apache.dubbo.common.constants.CommonConstants.SystemProperty.SYSTEM_TCP_RESPONSE_TIMEOUT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class ConsumerConfigTest {
    @Test
    void testTimeout() throws Exception {
        try {
            SystemPropertyConfigUtils.clearSystemProperty(SYSTEM_TCP_RESPONSE_TIMEOUT);
            ConsumerConfig consumer = new ConsumerConfig();
            consumer.setTimeout(10);
            assertThat(consumer.getTimeout(), is(10));
            assertThat(SystemPropertyConfigUtils.getSystemProperty(SYSTEM_TCP_RESPONSE_TIMEOUT), equalTo("10"));
        } finally {
            SystemPropertyConfigUtils.clearSystemProperty(SYSTEM_TCP_RESPONSE_TIMEOUT);
        }
    }

    @Test
    void testDefault() throws Exception {
        ConsumerConfig consumer = new ConsumerConfig();
        consumer.setDefault(true);
        assertThat(consumer.isDefault(), is(true));
    }

    @Test
    void testClient() throws Exception {
        ConsumerConfig consumer = new ConsumerConfig();
        consumer.setClient("client");
        assertThat(consumer.getClient(), equalTo("client"));
    }
}
