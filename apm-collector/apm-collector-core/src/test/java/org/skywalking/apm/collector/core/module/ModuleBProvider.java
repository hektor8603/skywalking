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

package org.skywalking.apm.collector.core.module;

import java.util.Properties;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import org.junit.Test;
import org.skywalking.apm.collector.core.module.instrument.ServiceMetricCollector;
import org.skywalking.apm.collector.core.module.instrument.TracedService;

import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.not;

/**
 * @author wu-sheng
 */
public class ModuleBProvider extends ModuleProvider {
    @Override public String name() {
        return "P-B";
    }

    @Override public Class<? extends Module> module() {
        return BaseModuleB.class;
    }

    @Override public void prepare(Properties config) throws ServiceNotProvidedException {
        this.registerServiceImplementation(BaseModuleB.ServiceBBusiness1.class, new Business1());
    }

    @Override public void start(Properties config) throws ServiceNotProvidedException {
        this.registerServiceImplementation(BaseModuleB.ServiceBBusiness2.class, new Business2());
    }

    @Override public void notifyAfterCompleted() throws ServiceNotProvidedException {

    }

    @Override public String[] requiredModules() {
        return new String[0];
    }

    public class Business1 implements BaseModuleB.ServiceBBusiness1 {

    }

    public class Business2 implements BaseModuleB.ServiceBBusiness2 {

    }
}