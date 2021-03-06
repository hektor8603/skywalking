/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package org.skywalking.apm.collector.agentregister.jetty.handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.skywalking.apm.collector.agentregister.servicename.ServiceNameService;
import org.skywalking.apm.collector.server.jetty.ArgumentsParseException;
import org.skywalking.apm.collector.server.jetty.JettyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author peng-yongsheng
 */
public class ServiceNameDiscoveryServiceHandler extends JettyHandler {

    private final Logger logger = LoggerFactory.getLogger(ServiceNameDiscoveryServiceHandler.class);

    private ServiceNameService serviceNameService = new ServiceNameService();
    private Gson gson = new Gson();

    @Override public String pathSpec() {
        return "/servicename/discovery";
    }

    private static final String APPLICATION_ID = "ai";
    private static final String SERVICE_NAME = "sn";
    private static final String SERVICE_ID = "si";
    private static final String ELEMENT = "el";

    @Override protected JsonElement doGet(HttpServletRequest req) throws ArgumentsParseException {
        throw new UnsupportedOperationException();
    }

    @Override protected JsonElement doPost(HttpServletRequest req) throws ArgumentsParseException {
        JsonArray responseArray = new JsonArray();
        try {
            JsonArray services = gson.fromJson(req.getReader(), JsonArray.class);
            for (JsonElement service : services) {
                int applicationId = service.getAsJsonObject().get(APPLICATION_ID).getAsInt();
                String serviceName = service.getAsJsonObject().get(SERVICE_NAME).getAsString();

                int serviceId = serviceNameService.getOrCreate(applicationId, serviceName);
                if (serviceId != 0) {
                    JsonObject responseJson = new JsonObject();
                    responseJson.addProperty(SERVICE_ID, serviceId);
                    responseJson.add(ELEMENT, service);
                    responseArray.add(responseJson);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return responseArray;
    }
}
