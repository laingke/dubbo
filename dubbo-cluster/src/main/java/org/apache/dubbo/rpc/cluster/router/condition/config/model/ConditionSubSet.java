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
package org.apache.dubbo.rpc.cluster.router.condition.config.model;

import org.apache.dubbo.rpc.cluster.router.condition.matcher.ConditionMatcher;

import java.util.HashMap;
import java.util.Map;

import static org.apache.dubbo.rpc.cluster.Constants.DefaultRouteConditionSubSetWeight;

public class ConditionSubSet {
    private Map<String, ConditionMatcher> condition = new HashMap<>();
    private Integer subSetWeight;

    public ConditionSubSet() {}

    public ConditionSubSet(Map<String, ConditionMatcher> condition, Integer subSetWeight) {
        this.condition = condition;
        this.subSetWeight = subSetWeight;
        if (subSetWeight <= 0) {
            this.subSetWeight = DefaultRouteConditionSubSetWeight;
        }
    }

    public Map<String, ConditionMatcher> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, ConditionMatcher> condition) {
        this.condition = condition;
    }

    public Integer getSubSetWeight() {
        return subSetWeight;
    }

    public void setSubSetWeight(int subSetWeight) {
        this.subSetWeight = subSetWeight;
    }

    @Override
    public String toString() {
        return "ConditionSubSet{" + "cond=" + condition + ", subSetWeight=" + subSetWeight + '}';
    }
}
