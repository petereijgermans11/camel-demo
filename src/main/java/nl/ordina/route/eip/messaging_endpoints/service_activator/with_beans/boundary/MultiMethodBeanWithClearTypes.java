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

import org.springframework.stereotype.Component;

/**
 * Used in a route with a ".bean" without a method reference.
 * The resolving of which method to use is done by finding the method with the correct parameter type.
 *
 * @author Ivo Woltring
 */
@Component
public class MultiMethodBeanWithClearTypes {

    /**
     * Chosen by route ServiceActivatorRoutesWithBeanResolving_1 because body is a string.
     */
    public String sayHello(final String name) {
        return "hello " + name;
    }

    /**
     * Chosen by route ServiceActivatorRoutesWithBeanResolving_5 because body is a byte[].
     */
    public String sayHello(final byte[] chars) {
        return "hello route 5: " + new String(chars);
    }
}
