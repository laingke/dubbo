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
package org.apache.dubbo.rpc.protocol.tri.rest.support.jaxrs.compatible.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/demoService")
public interface HttpMethodService {

    @POST
    @Path("/sayPost")
    @Consumes({javax.ws.rs.core.MediaType.TEXT_PLAIN})
    String sayHelloPost(@QueryParam("name") String name);

    @DELETE
    @Path("/sayDelete")
    @Consumes({javax.ws.rs.core.MediaType.TEXT_PLAIN})
    String sayHelloDelete(@QueryParam("name") String name);

    @HEAD
    @Path("/sayHead")
    @Consumes({javax.ws.rs.core.MediaType.TEXT_PLAIN})
    String sayHelloHead();

    @GET
    @Path("/sayGet")
    @Consumes({javax.ws.rs.core.MediaType.TEXT_PLAIN})
    String sayHelloGet(@QueryParam("name") String name);

    @PUT
    @Path("/sayPut")
    @Consumes({javax.ws.rs.core.MediaType.TEXT_PLAIN})
    String sayHelloPut(@QueryParam("name") String name);

    @PATCH
    @Path("/sayPatch")
    @Consumes({javax.ws.rs.core.MediaType.TEXT_PLAIN})
    String sayHelloPatch(@QueryParam("name") String name);

    @OPTIONS
    @Path("/sayOptions")
    @Consumes({javax.ws.rs.core.MediaType.TEXT_PLAIN})
    String sayHelloOptions(@QueryParam("name") String name);
}
