/*
 * Copyright 2017 Ivo Woltring <WebMaster@ivonet.nl>
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
 */

package nl.ordina.route.eip.messaging_endpoints.service_activator.with_beans.boundary;

import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

/**
 * Only the method containing the {@link Handler} annotation
 * is the one called by the multiAmbiguousMethodsWithHandlerAnnotation_3 route.
 *
 * All the methods have the same signature and without the annotation it would throw an
 * {@link org.apache.camel.component.bean.AmbiguousMethodCallException}. Just try it out
 * by commenting out the annotation.
 *
 * @author Ivo Woltring
 */
@Component
@SuppressWarnings("unused")
public class MultiAmbiguousMethodsWithHandlerAnnotation {

    @Handler
    public String sayHello(final String name) {
        return "hello " + name;
    }

    public String sayHello2(final String name) {
        return "WRONG! " + name;
    }

    public String sayHello3(final String name) {
        return "WRONG! " + name;
    }

    public String sayHello4(final String name) {
        return "WRONG! " + name;
    }

    public String sayHello5(final String name) {
        return "Hello route 4 only: " + name;
    }

}
