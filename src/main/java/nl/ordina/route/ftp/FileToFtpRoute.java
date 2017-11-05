
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

package nl.ordina.route.ftp;

import lombok.extern.slf4j.Slf4j;
import nl.ordina.context.CamelDemoContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**
 * This exercise lets you get acquainted with ftp routing.
 *
 * @author Ivo Woltring
 */
@Slf4j
@Component
public class FileToFtpRoute extends RouteBuilder {

    private final CamelDemoContext context;

    @Autowired
    public FileToFtpRoute(final CamelDemoContext context) {
        this.context = context;
    }

    @Override
    public void configure() throws Exception {
        final String projectBaseLocation = this.context.projectBaseLocation();
        final String name = this.getClass().getSimpleName();

        // Copy all the files from the target/txt folder to the ftp site under the user account
        // log what you are doing
        // make sure you get the ftp information from the application.yml file of its profile equivalent

        from(format("file://%s/target/txt/", projectBaseLocation))
                .routeId(name)
                .log("Found file [$simple{header.CamelFileName}]")
                .to("ftp://{{ftp.host}}:{{ftp.port}}?username={{ftp.user.name}}&password={{ftp.user.password}}&passiveMode={{ftp.passiveMode}}");

    }
}
