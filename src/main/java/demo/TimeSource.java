/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package demo;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import demo.messages.MessagesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Dave Syer
 * @author Glenn Renfro
 */
@EnableBinding(Source.class)
@EnableConfigurationProperties(TimeSourceOptionsMetadata.class)
public class TimeSource {
    private Logger logger = LoggerFactory.getLogger(TimeSource.class);

    @Autowired
    private TimeSourceOptionsMetadata options;
    @Autowired
    private Collection<MessagesProvider> messageProviders;
    @Autowired
    private MessageChannel output;


    @Scheduled(fixedDelayString = "1000")
    public void timerMessageSource() {
        logger.info("Queue empty, fetching new items");
        Collection<Map<String, Object>> allMessages = new ArrayList<>();
        for (MessagesProvider messagesProvider : messageProviders) {
            Collection<Map<String, Object>> messages = messagesProvider.getMessages();
            allMessages.addAll(messages);
        }
        logger.info("Fetched {} items from greeen", allMessages.size());

        allMessages.forEach(stringObjectMap -> output.send(MessageBuilder.withPayload(stringObjectMap).build()));
    }

}
