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

package nl.ordina.route.eip.message_routing.recipient_list;

import lombok.extern.slf4j.Slf4j;
import nl.ordina.context.CamelDemoContext;
import nl.ordina.route.eip.message_routing.recipient_list.boundary.AnnotatedRecipientList;
import nl.ordina.route.file.FileCopyRoute;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A RecipientList EIP demo.
 *
 * In this demo we monitor the test-data/eip/recipient-list folder for XML files.
 * The {@link FileCopyRoute} will copy the xml files found in this folder.
 * If it is a XML file then we ask the {@link AnnotatedRecipientList} class to decide where to sent the message.
 * If it is not an XML file we will not do anything with the file.
 *
 * We also read from the routes decided by the {@link  AnnotatedRecipientList} class to log the message put on those queues
 * just to show that it happened as we expected.
 * In these rotes we get the actual message from the xml file by XPath and put them in the body to log them later.
 *
 * You can also follow in the <a href="http://localhost:8161/">ActiveMQ console</a> what happens (user: admin / pwd: secret)
 *
 * @author Ivo Woltring
 */
@Slf4j
//@Component
public class RecipientListRoute extends RouteBuilder {

    private final CamelDemoContext context;

    @Autowired
    public RecipientListRoute(final CamelDemoContext context) {
        this.context = context;
    }

    @Override
    public void configure() throws Exception {
        final String projectBaseLocation = this.context.projectBaseLocation();
        final String name = this.getClass().getSimpleName();

        from(String.format("file://%s/test-data/eip/message_routing/recipient_list/", projectBaseLocation))
              .routeId(name)
              .choice()
              .when(header("CamelFileName").endsWith(".xml"))
              .log("Found file [$simple{header.CamelFileName}] processing xml files in this route.")
              .bean("annotatedRecipientList")
              .recipientList(header("recipients"))
              .end()
              .otherwise()
              .log("Found file [$simple{header.CamelFileName}] will not process")
              .stop();

        from("jms:test")
              .routeId(name + "_test")
              .setBody()
              .xpath("//message/text()")
              .log("${body}");

        from("jms:production")
              .routeId(name + "_prod")
              .setBody()
              .xpath("//message/text()")
              .log("${body}");

    }
}
