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
package org.apache.dubbo.rpc.protocol.tri.rest.support.jaxrs;

import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.remoting.http12.HttpRequest;
import org.apache.dubbo.remoting.http12.HttpResponse;
import org.apache.dubbo.remoting.http12.rest.ParamType;
import org.apache.dubbo.rpc.protocol.tri.rest.RestConstants;
import org.apache.dubbo.rpc.protocol.tri.rest.mapping.meta.NamedValueMeta;
import org.apache.dubbo.rpc.protocol.tri.rest.util.RequestUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

@Activate(onClass = "javax.ws.rs.MatrixParam")
public class MatrixParamArgumentResolver extends AbstractJaxrsArgumentResolver {

    @Override
    public Class<Annotation> accept() {
        return Annotations.MatrixParam.type();
    }

    @Override
    protected ParamType getParamType(NamedValueMeta meta) {
        return ParamType.MatrixVariable;
    }

    @Override
    protected Object resolveValue(NamedValueMeta meta, HttpRequest request, HttpResponse response) {
        return CollectionUtils.first(doResolveCollectionValue(meta, request));
    }

    @Override
    protected Object resolveCollectionValue(NamedValueMeta meta, HttpRequest request, HttpResponse response) {
        return doResolveCollectionValue(meta, request);
    }

    private static List<String> doResolveCollectionValue(NamedValueMeta meta, HttpRequest request) {
        Map<String, String> variableMap = request.attribute(RestConstants.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return RequestUtils.parseMatrixVariableValues(variableMap, meta.name());
    }
}
