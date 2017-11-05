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

package nl.ordina.route.file;

import lombok.extern.slf4j.Slf4j;
import nl.ordina.context.CamelDemoContext;
import nl.ordina.route.eip.message_routing.recipient_list.boundary.AnnotatedRecipientList;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**
 * When running this example and you have configured the project.base.folder
 * correctly it will copy all files from the test-data/SimpleFileCopyRoute folder
 * to test-data/ftp/admin folder which is also the 'home' folder for the ftp admin user.
 * <p>
 * It is named to illustrate that and it adds some logs and a bit of Simple language
 * usage.
 * <p>
 * If you remove '?noop=true' the copy will become a move.
 * <p>
 * In this example a kind of filter is build in.
 * This specific route takes all files but send them to different end-points.
 * The xml files will be send to the eip/recipient-list. In the RecipientList demo I will do the same but illustrate more.
 * The {@link AnnotatedRecipientList} class is being reused here.
 *
 * @author Ivo Woltring
 */
@Slf4j
@Component
public class FileCopyRoute extends RouteBuilder {

    private final CamelDemoContext context;

    @Autowired
    public FileCopyRoute(final CamelDemoContext context) {
        this.context = context;
    }

    @Override
    public void configure() throws Exception {
        final String projectBaseLocation = this.context.projectBaseLocation();
        final String name = this.getClass().getSimpleName();

        from(format("file://%s/test-data/startingPoint/?noop=true", projectBaseLocation))
                .routeId(name)
                .choice()
                .when(header("CamelFileName").endsWith(".xml"))
                .log("Found file [$simple{header.CamelFileName}] and will copy to target/xml.")
                .to(format("file://%s/target/xml/", projectBaseLocation))
                .otherwise()
                .log("Found file [$simple{header.CamelFileName}] and copying it to target/txt")
                .to(format("file://%s/target/txt/", projectBaseLocation));
    }
}
