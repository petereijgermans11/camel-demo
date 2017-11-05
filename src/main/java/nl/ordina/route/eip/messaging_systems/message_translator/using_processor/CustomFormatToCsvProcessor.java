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
import nl.ordina.route.eip.messaging_systems.message_translator.boundary.Address;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Processes a custom format to CSV values.
 * The custom formatted file is a completely arbitrarily chosen format for demo purposes.
 * <p>
 * This is a very dirty csv implementation, and only for demo purposes!
 *
 *          1         2         3         4
 * 1234567890123456789012345678901234567890
 * ----------------------------------------
 * 10000000012017070213h33m345261BH10 A0001
 * 10000000022017070215h55m011000AA69 A0003
 *
 * @author Ivo Woltring
 */
@Slf4j
@Component
public class CustomFormatToCsvProcessor implements Processor {

    @Override
    public void process(final Exchange exchange) throws Exception {
        final String body = exchange.getIn().getBody(String.class);
        final String[] lines = body.split("\\n");
        assert lines.length > 3;
        final StringBuilder csv = new StringBuilder();
        for (int i = 3; i < lines.length; i++) {
            final String line = lines[i];
            final String orderId = processOrderId(line);
            csv.append(String.format("\"%s\",", orderId));
            final String dateTime = processDate(line);
            csv.append(String.format("\"%s\",", dateTime));
            final Address address = processAddress(line);
            csv.append(address.toCSV());
            final Integer items = processNumberOfItems(line);
            csv.append(String.format(",\"%s\"\n", items));
        }
        exchange.getIn().setBody(csv.toString());
    }

    private Integer processNumberOfItems(final String input) {
        return Integer.valueOf(input.substring(36, 40));
    }

    private Address processAddress(final String input) {
        return new Address(input.substring(26, 32), Integer.valueOf(input.substring(32, 35).trim()), input.substring(35, 36));
    }

    private String processDate(final String input) {
        final String dateStr = input.substring(10, 26);
        return LocalDateTime.of(Integer.valueOf(dateStr.substring(0, 4)), Integer.valueOf(dateStr.substring(4, 6)), Integer.valueOf(dateStr.substring(6, 8)), Integer.valueOf(dateStr.substring(8, 10)), Integer.valueOf(dateStr.substring(11, 12)), Integer.valueOf(dateStr.substring(14, 16))).toString();
    }

    private String processOrderId(final String input) {
        return input.substring(0, 10);
    }
}
