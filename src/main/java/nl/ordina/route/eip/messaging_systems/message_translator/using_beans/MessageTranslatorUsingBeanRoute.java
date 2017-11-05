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

package nl.ordina.route.eip.messaging_systems.message_translator.using_beans;

import lombok.extern.slf4j.Slf4j;
import nl.ordina.context.CamelDemoContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**
 * A.k.a the Adapter pattern.
 * <p>
 * This route demo's the MessageTranslator pattern by using a Bean for the mapper.
 * This route monitors the folder where the {@link nl.ordina.route.eip.messaging_systems.message_translator.using_processor.MessageTranslatorUsingProcessorRoute}
 * print its Csv entries to console.
 * Then it will marshall the csv to a list of lists of strings by using the csv marshaller provided by Camel.
 * The bean demo-ed here will convert the unmarshal-ed entries to Json using the {@link CsvToJson} bean.
 * It will write its json result to the target/MessageTranslatorUsingBean folder to view or use in another demo.
 *
 * @author Ivo Woltring
 */
@Slf4j
@Component
public class MessageTranslatorUsingBeanRoute extends RouteBuilder {

    private final CamelDemoContext context;

    @Autowired
    public MessageTranslatorUsingBeanRoute(final CamelDemoContext context) {
        this.context = context;
    }

    @Override
    public void configure() throws Exception {
        final String projectBaseLocation = this.context.projectBaseLocation();
        final String name = this.getClass().getSimpleName();

// Create a route that reads from the target/csv folder
// - using unmarshal and the build in csv data format
// - log the unmarshalled body
// - using the CsvToJson bean to process to json
// - log the json body
// - routing it to file in target/json with the original name with `.json` added to it

        from(format("file://%s/target/csv/?noop=true", projectBaseLocation))
                .routeId(name)
                .log("Found file [$simple{header.CamelFileName}] processing csv to json in this route.")
                .log("Input message:\n${body}")
                .unmarshal()
                .csv()
                .log("CSV unmarshalled:\n${body}")
                .bean("csvToJson", "map")
//                .to("bean:csvToJson?method=map") //also correct
                .log("Bean mapped to json:\n${body}")
                .to(format("file://%s/target/json?fileName=${header.CamelFileName}.json", projectBaseLocation));
    }
}
