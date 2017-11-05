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

package nl.ordina.route.eip.message_routing.routing_slip;

import nl.ordina.route.eip.message_routing.routing_slip.boundary.Decider;
import org.apache.camel.builder.RouteBuilder;

/**
 * Most of the routing patterns presented in this section route incoming
 * messages to one or more destinations based on a set of rules.
 * Sometimes, though, we need to route a message not just to a single
 * component, but through a whole series of components. Let's assume, for example,
 * that we use a Pipes and Filters architecture to process incoming messages that
 * have to undergo a sequence of processing steps and business rule validations.
 * Since the nature of the validations varies widely and may depend on external
 * systems (e.g., credit card validations), we implement each type of step as
 * a separate filter. Each filter inspects the incoming message, and applies
 * the business rule(s) to the message. If the message does not fulfill
 * the conditions specified by the rules it is routed to an exception channel.
 * The channels between the filters determine the sequence of validations that
 * the message needs to undergo.
 *
 * @author Ivo Woltring
 */
//@Component
public class RoutingSlipRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        final String name = this.getClass()
                                .getSimpleName();


        from("jms:topic:names")
              .routeId(name + "_1")
              .setHeader("ivonetRoutingSlip")
              .method(Decider.class)
              .routingSlip(header("ivonetRoutingSlip"));

        from("direct:A")
              .routeId(name + "_A")
              .log("ROUTING: A -> ${body}");

        from("direct:B")
              .routeId(name + "_B")
              .log("ROUTING: B -> ${body}");

        from("direct:other")
              .routeId(name + "_O")
              .log("ROUTING: O -> ${body}");
    }
}
