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

import org.apache.camel.language.Bean;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * I'm being used as a {@link Bean} in a ".bean" method reference
 *
 * @author Ivo Woltring
 */
@Component("guid")
public class GuidGenerator {

    /**
     * Just a dummy generator.
     */
    public int generate() {
        final Random ran = new Random();
        return ran.nextInt(10000000);
    }
}

