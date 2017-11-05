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

package nl.ordina.route.eip.message_routing.recipient_list.boundary;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.RecipientList;
import org.apache.camel.language.XPath;
import org.springframework.stereotype.Component;

/**
 * I decide where messages are being sent to for the EIP RecipientList demo.
 *
 * @author Ivo Woltring
 */
@Slf4j
@Component
public class AnnotatedRecipientList {

    /**
     * As you can see is the return type an array of strings.
     * These strings are the endpoints to the places where the messages need to be delivered.
     * in this example I only put in one destination per list but you can of course play around with this.
     *
     * @param attribute the "test" attribute in the message element
     * @return an array of endpoints
     */
    @RecipientList
    public String[] route(@XPath("/message/@test") final String attribute) {

        if (isATestMessage(attribute)) {
            log.info("Is a test message");
            return new String[]{"jms:test"};
        } else {
            log.info("Is not a test message");
            return new String[]{"jms:production"};
        }
    }

    private boolean isATestMessage(final String input) {
        return "true".equals(input);
    }
}
