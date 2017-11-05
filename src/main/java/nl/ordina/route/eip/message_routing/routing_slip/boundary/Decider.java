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

package nl.ordina.route.eip.message_routing.routing_slip.boundary;

import lombok.extern.slf4j.Slf4j;
import nl.ordina.route.eip.message_routing.routing_slip.RoutingSlipRoute;
import org.springframework.stereotype.Component;

/**
 * The single method in this class is used to decide where the next message
 * should be send to in the {@link RoutingSlipRoute} class.
 *
 * @author Ivo Woltring
 */
@Slf4j
@Component
public class Decider {

    public String decide(final String body) {
        log.info(body);
        if (body.startsWith("A")) {
            return "direct:A";
        }
        if (body.startsWith("B")) {
            return "direct:B";
        }
        return "direct:other";
    }
}
