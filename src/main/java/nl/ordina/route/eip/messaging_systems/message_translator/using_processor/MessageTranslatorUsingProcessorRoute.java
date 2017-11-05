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

package nl.ordina.route.eip.messaging_systems.message_translator.using_processor;

import lombok.extern.slf4j.Slf4j;
import nl.ordina.context.CamelDemoContext;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**
 * A.k.a the Adapter pattern.
 * <p>
 * In this exercise the Adapter (MessageTranslator) pattern is shown by using a {@link Processor} ({@link CustomFormatToCsvProcessor})
 * What this route does is getting a custom message from the test-data/eip/messaging_systems/message_translator
 * location and translating it to Csv format.
 * The custom formatted file is completely arbitrarily chosen by me to demo that it could be anything.
 * It will write the created csv file to the target/MessageTranslatorUsingProcessor folder and also log it to console.
 *
 * @author Ivo Woltring
 */
@Slf4j
@Component
public class MessageTranslatorUsingProcessorRoute extends RouteBuilder {

    private final CamelDemoContext context;
    private final CustomFormatToCsvProcessor customFormatToCsvProcessor;

    @Autowired
    public MessageTranslatorUsingProcessorRoute(final CamelDemoContext context, final CustomFormatToCsvProcessor customFormatToCsvProcessor) {
        this.context = context;
        this.customFormatToCsvProcessor = customFormatToCsvProcessor;
    }

    @Override
    public void configure() throws Exception {
        final String projectBaseLocation = this.context.projectBaseLocation();
        final String name = this.getClass().getSimpleName();

        from(format("file://%s/test-data/eip/messaging_systems/message_translator/?noop=true", projectBaseLocation))
                .routeId(name)

// complete this route by:
// - logging the body of the special formatted file
// - using the CustomFormatToCsvProcessor class as processor for the custom format. First inject this Component into your class and then use it
// - logging the transformed message (csv)
// - routing it to file in target/csv  with the original name with `.csv` added to it

                .log("Found file [$simple{header.CamelFileName}] processing custom format to csv in this route.")
                .log("Custom formatted file:\n${body}")
                .process(this.customFormatToCsvProcessor) //used the DI from Spring to reference this bean
                .log("Csv formatted:\n${body}")
                .to(format("file://%s/target/csv?fileName=${header.CamelFileName}.csv", projectBaseLocation));
    }
}
