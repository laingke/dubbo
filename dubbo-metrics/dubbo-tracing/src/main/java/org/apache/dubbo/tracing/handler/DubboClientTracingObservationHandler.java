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
package org.apache.dubbo.tracing.handler;

import org.apache.dubbo.tracing.context.DubboClientContext;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.tracing.Tracer;

public class DubboClientTracingObservationHandler<T extends DubboClientContext> implements ObservationHandler<T> {
    private final Tracer tracer;

    public DubboClientTracingObservationHandler(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void onScopeOpened(T context) {}

    @Override
    public boolean supportsContext(Observation.Context context) {
        return context instanceof DubboClientContext;
    }
}
