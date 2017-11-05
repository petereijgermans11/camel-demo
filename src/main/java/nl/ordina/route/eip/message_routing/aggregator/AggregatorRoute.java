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

package nl.ordina.route.eip.message_routing.aggregator;

import lombok.extern.slf4j.Slf4j;
import nl.ordina.config.RepositoryConfig;
import nl.ordina.route.eip.message_routing.aggregator.boundary.MyAggregationStrategy;
import nl.ordina.route.eip.message_routing.aggregator.boundary.MyAggregationStrategyPojo;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.leveldb.LevelDBAggregationRepository;
import org.apache.camel.spi.AggregationRepository;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * An Aggregator EIP demo.
 * <p/>
 * Routes:
 * <p/>
 * 1) In this demo we first get all the names from the names topic and send them to an new topic (names_with_generated_correlation_ids) with randomly generated
 * correlation ids between 1 and 4.
 * <p/>
 * 2) In this demo we aggregate multiple messages from the jms:topic:names_with_generated_correlation_ids to one message again based on the
 * JMSCorrelationID in the header as generated in the first route.
 * <p/>
 * 3) does the same as route 2 but then with a POJO as the aggregator strategy.
 * <p/>
 * the {@link MyAggregationStrategy} is used to aggregate the messages to one message.
 * A new message is finished when we have a size of 3 aggregated messages or if the timeout has expired.
 * In the end one to four correlationIds may not have sufficient names to complete on the completionSize and will then
 * end on the timeout. Less then 3 names will then be in the message.
 * <p/>
 * Every time a completed aggregation has occurred it will be logged to console.
 * <p/>
 * The aggregator has been made persistent by using a {@link AggregationRepository} and in this case a {@link LevelDBAggregationRepository} defined in {@link RepositoryConfig}
 * and injected as a Bean. If you remove that line it will be an in memory aggregation.
 *
 * @author Ivo Woltring
 */
@Slf4j
//@Component
public class AggregatorRoute extends RouteBuilder {

    private final MyAggregationStrategy myAggregationStrategy;
    private final MyAggregationStrategyPojo myAggregationStrategyPojo;
    private final AggregationRepository aggregationRepository;

    @Autowired
    public AggregatorRoute(final MyAggregationStrategy myAggregationStrategy,
                           final MyAggregationStrategyPojo myAggregationStrategyPojo,
                           final AggregationRepository aggregationRepository) {
        this.myAggregationStrategy = myAggregationStrategy;
        this.myAggregationStrategyPojo = myAggregationStrategyPojo;
        this.aggregationRepository = aggregationRepository;
    }

    @Override
    public void configure() throws Exception {
        final String name = this.getClass()
                                .getSimpleName();

        from("jms:topic:names")
              .routeId(name + "_1")
              .setHeader("JMSCorrelationID", method("correlationIdGenerator", "next"))
              .log("CorrelationId: ${header[JMSCorrelationID]}")
              .to("jms:topic:names_with_generated_correlation_ids");

        from("jms:topic:names_with_generated_correlation_ids")
              .routeId(name + "_2")
              .log("Received: ${body}")
              .log("CorrelationId: ${header[JMSCorrelationID]}")
              .aggregate(header("JMSCorrelationID"), this.myAggregationStrategy)
              .completionSize(3)
              .completionTimeout(2000)
              .aggregationRepository(this.aggregationRepository)
              .log("Aggregated in route 2 with id ${header[JMSCorrelationID]}: ${body}");

        from("jms:topic:names_with_generated_correlation_ids")
              .routeId(name + "_3")
              .log("Received: ${body}")
              .log("CorrelationId: ${header[JMSCorrelationID]}")
              .aggregate(header("JMSCorrelationID"), AggregationStrategies.bean(this.myAggregationStrategyPojo))
              .completionSize(3)
              .completionTimeout(2000)
              .aggregationRepository(this.aggregationRepository)
              .log("Aggregated in route 3 with id ${header[JMSCorrelationID]}: ${body}");

    }
}
