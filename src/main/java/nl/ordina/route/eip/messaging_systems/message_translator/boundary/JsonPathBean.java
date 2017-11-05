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

package nl.ordina.route.eip.messaging_systems.message_translator.boundary;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.jsonpath.JsonPath;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**
 * Just to demonstrate the usage of the {@link JsonPath} annotation.
 * I'm used in {@link nl.ordina.route.eip.messaging_systems.message_translator.using_transform.MessageTranslatorUsingTransformRoute}
 * for the rest this method will only return the body unmodified.
 *
 * will produce something like the following in the console log:
 * <pre>
 * 2017-07-26 17:41:46.306  INFO 13452 --- [latorUsingBean/] n.i.r.e.m.m.boundary.JsonPathBean        : Zip = 5261BH
 * </pre>
 *
 * In order for this annotation to work you need to add the org.apache.camel:camel-jsonpath dependency to your pom.
 *
 * @author Ivo Woltring
 */
@Slf4j
@Component
public class JsonPathBean {

    public String printFirstOrderlineZip(@Body final String body,
                                         @JsonPath("$.orderLine[0].address.zip") final String zip) {

        log.info(format("Zip = %s", zip));
        return body;
    }
}
