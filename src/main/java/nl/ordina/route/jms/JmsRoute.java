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

package nl.ordina.route.jms;

import lombok.extern.slf4j.Slf4j;
import nl.ordina.context.CamelDemoContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**
 * JMS ActiveMQ demo.
 * <p>
 * ActiveMQ needs to be configured to work.
 * The {@link org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration} class will
 * take care of the auto configuration of the {@link org.apache.activemq.ActiveMQConnectionFactory}.
 * The endpoint provided in the application.yml (or application.properties) file will tell the
 * autoconfigure with which url to create the connectionFactory.
 * <p>
 * In the first route a file test-data/JmsRoute folder will be routed to the
 * JmsRoute Queue in ActiveMQ as configured by the spring.activemq.broker-url property
 * in the application.yml file.
 * <p>
 * For this route to work you need the the following dependencies:
 * <ul>
 * <li>org.apache.activemq:activemq-all or in our case the org.springframework.boot:spring-boot-starter-activemq</li>
 * <li>org.apache.camel:camel-jms</li>
 * </ul>
 *
 * @author Ivo Woltring
 */
@Slf4j
@Component
public class JmsRoute extends RouteBuilder {

    private final String projectBaseLocation;

    @Autowired
    public JmsRoute(final CamelDemoContext context) {
        this.projectBaseLocation = context.projectBaseLocation();
    }


    @Override
    public void configure() throws Exception {
        final String name = this.getClass().getSimpleName();

// Create a route that get all files from the test-data/JmsRoute folder and give it an id corresponding to the name of the class
// if the file is called "shakespeare.txt" special possessing is needed
//    - the body needs to be split into its separate books and put on the shakespeare.<yourname> queue
//    - log from all found books the title (get from the body)
// if xml files you can put them on the `xml.<yourname>` queue
// if other files you can put them on the 'dump.<yourname>' queue


        from(format("file://%s/test-data/JmsRoute/?noop=true", this.projectBaseLocation))
                .routeId(name)
                .log("Found file [$simple{header.CamelFileName}]")
                .choice()
                .when(header("CamelFileName").isEqualTo("shakespeare.txt"))
                .log("Found file [$simple{header.CamelFileName}] => Special processing")
                .split(body().regexTokenize("16[0-9]{2}\n"))
                .process(exchange -> {
                    final String[] lines = exchange.getIn().getBody(String.class).trim().split("\n");
                    if (lines.length > 0) {
                        log.info(lines[0]);
                    }
                })
                .to("jms:queue:shakespeare.ivo2")
                .endChoice()
                .when(header("CamelFileName").endsWith(".xml"))
                .log("Found file [$simple{header.CamelFileName}] => xml queue")
                .to("jms:queue:xml.ivo")
                .otherwise()
                .log("Found file [$simple{header.CamelFileName}] => dump queue")
                .to("jms:queue:dump.ivo")
                .end();

// Extra:
// - why would it be better to get the endpoints from a property file?
//   - Think about testability.
//   - central place of endpoint registration.

    }
}
