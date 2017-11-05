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

package nl.ordina.route.eip.message_routing.aggregator.boundary;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

/**
 * An {@link AggregationStrategy} example for simply adding two messages together.
 *
 * If the oldExchange is null then it is the first message in the aggregation.
 *
 * @author Ivo Woltring
 */
@Component
public class MyAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(final Exchange oldExchange,
                              final Exchange newExchange) {

        if (isFirstRecord(oldExchange)) {
            return newExchange;
        }

        final String oldEx = oldExchange.getIn().getBody(String.class).trim();
        final String newEx = newExchange.getIn().getBody(String.class).trim();

        oldExchange.getIn().setBody(oldEx + ", " + newEx);

        return oldExchange;
    }

    private boolean isFirstRecord(final Exchange exchange) {
        return exchange == null;
    }
}
