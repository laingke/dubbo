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
package org.apache.dubbo.common.serialize.support;

import org.apache.dubbo.common.serialization.PreferSerializationProvider;
import org.apache.dubbo.common.serialize.Serialization;
import org.apache.dubbo.rpc.model.FrameworkModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PreferSerializationProviderImpl implements PreferSerializationProvider {
    private final String preferSerialization;

    public PreferSerializationProviderImpl(FrameworkModel frameworkModel) {
        List<String> defaultSerializations = Arrays.asList("hessian2", "fastjson2");
        this.preferSerialization = defaultSerializations.stream()
                .filter(s ->
                        frameworkModel.getExtensionLoader(Serialization.class).hasExtension(s))
                .collect(Collectors.joining(","));
    }

    @Override
    public String getPreferSerialization() {
        return preferSerialization;
    }
}
