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

package nl.ordina.route.eip.messaging_endpoints.service_activator.with_beans.boundary;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.apache.camel.language.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.lang.String.format;

/**
 * Just a small demo of calling a bean with multiple parameters in a route.
 *
 * @author Ivo Woltring
 */
@Slf4j
@Component
public class BeanMultiParamMethods {

    public String sayAll(@Body final String body,
                         @Headers final Map<String, Object> headers,
                         @Bean(ref = "guid", method = "generate") final int id) {

        for (final String key : headers.keySet()) {
            log.info(format("%-25s = %s", key, headers.get(key)));
        }

        return format("hello [%s] with id [%s]", body, id);
    }
}
