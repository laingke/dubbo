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

import org.apache.dubbo.common.constants.RegistryConstants;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.support.Parameter;
import org.apache.dubbo.rpc.model.ApplicationModel;

import java.util.Map;

/**
 * Configuration for the monitor.
 *
 * @export
 */
public class MonitorConfig extends AbstractConfig {

    private static final long serialVersionUID = -1184681514659198203L;

    /**
     * The protocol of the monitor. If the value is "registry" it will search the monitor address from the registry center.
     * Otherwise, it will directly connect to the monitor center.
     */
    private String protocol;

    /**
     * The monitor address
     */
    private String address;

    /**
     * The monitor username
     */
    private String username;

    /**
     * The monitor password
     */
    private String password;

    /**
     * The monitor group
     */
    private String group;

    /**
     * The monitor version
     */
    private String version;

    /**
     * The monitor reporting interval
     */
    private String interval;

    /**
     * Customized parameters
     */
    private Map<String, String> parameters;

    public MonitorConfig() {}

    public MonitorConfig(ApplicationModel applicationModel) {
        super(applicationModel);
    }

    public MonitorConfig(String address) {
        this.address = address;
    }

    public MonitorConfig(ApplicationModel applicationModel, String address) {
        super(applicationModel);
        this.address = address;
    }

    @Parameter(excluded = true)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Parameter(excluded = true)
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Parameter(excluded = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Parameter(excluded = true)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    @Override
    @Parameter(excluded = true, attribute = false)
    public boolean isValid() {
        return StringUtils.isNotEmpty(address) || RegistryConstants.REGISTRY_PROTOCOL.equals(protocol);
    }
}
