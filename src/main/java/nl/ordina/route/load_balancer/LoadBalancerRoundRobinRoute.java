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

package nl.ordina.route.load_balancer;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;

/**
 * Simple demo of a load balancer round robin route.
 *
 * @author Ivo Woltring
 */
@Slf4j
//@Component
public class LoadBalancerRoundRobinRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        final String name = this.getClass()
                                .getSimpleName();

        from("jms:topic:names")
              .routeId(name + "_from")
              .loadBalance()
              .roundRobin()
              .to("jms:loadBalanced:a")
              .to("jms:loadBalanced:b")
              .end();

        from("jms:loadBalanced:a")
              .log("A received: ${body}")
              .to("jms:loadBalanced:out");

        from("jms:loadBalanced:b")
              .log("B received: ${body}")
              .to("jms:loadBalanced:out");

    }
}
