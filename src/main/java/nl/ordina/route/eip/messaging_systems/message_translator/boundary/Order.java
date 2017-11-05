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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * I'm an Order and I have {@link OrderLine}s.
 *
 * I'm marshallable to and from Json by the provided methods and to XML by JaxB through the
 * {@link XmlRootElement} annotation.
 *
 * @author Ivo Woltring
 */
@Data
@XmlRootElement
public class Order {
    private List<OrderLine> orderLine;

    public void add(final OrderLine orderLine) {
        if (this.orderLine == null) {
            this.orderLine = new ArrayList<>();
        }
        this.orderLine.add(orderLine);
    }

    public String asJson() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        try {
            return mapper.writeValueAsString(this);
        } catch (final JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Order fromJson(final String json) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        try {
            return mapper.readValue(json, Order.class);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }
}

