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

package nl.ordina.config;

import org.apache.camel.component.leveldb.LevelDBAggregationRepository;
import org.apache.camel.spi.AggregationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Repository Configs have a place here.
 *
 * @author Ivo Woltring
 */
@Component
public class RepositoryConfig {

    @Bean
    public AggregationRepository aggregationRepository() {
        final LevelDBAggregationRepository levelDB = new LevelDBAggregationRepository("aggregator",
                                                                                      "target/aggregator.db");
        levelDB.setUseRecovery(true);
        levelDB.setMaximumRedeliveries(5);
        levelDB.setDeadLetterUri("jms:queue:dead-letter");
        levelDB.setRecoveryInterval(3000);

        return levelDB;
    }
}
