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
package org.apache.dubbo.spring.boot.autoconfigure;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.config.ConfigurationBeanBinder;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.apache.dubbo.config.spring.util.PropertySourcesUtils.getSubProperties;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link BinderDubboConfigBinder} Test
 *
 * @since 2.7.0
 */
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:/dubbo.properties")
@ContextConfiguration(classes = BinderDubboConfigBinder.class)
class BinderDubboConfigBinderTest {

    @Autowired
    private ConfigurationBeanBinder dubboConfigBinder;

    @Autowired
    private ConfigurableEnvironment environment;

    @Test
    void testBinder() {

        ApplicationConfig applicationConfig = new ApplicationConfig();
        Map<String, Object> properties = getSubProperties(environment.getPropertySources(), "dubbo.application");
        dubboConfigBinder.bind(properties, true, true, applicationConfig);
        assertEquals("hello", applicationConfig.getName());
        assertEquals("world", applicationConfig.getOwner());

        RegistryConfig registryConfig = new RegistryConfig();
        properties = getSubProperties(environment.getPropertySources(), "dubbo.registry");
        dubboConfigBinder.bind(properties, true, true, registryConfig);
        assertEquals("10.20.153.17", registryConfig.getAddress());

        ProtocolConfig protocolConfig = new ProtocolConfig();
        properties = getSubProperties(environment.getPropertySources(), "dubbo.protocol");
        dubboConfigBinder.bind(properties, true, true, protocolConfig);
        assertEquals(Integer.valueOf(20881), protocolConfig.getPort());
    }
}
