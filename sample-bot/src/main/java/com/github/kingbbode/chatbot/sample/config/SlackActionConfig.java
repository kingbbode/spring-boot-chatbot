package com.github.kingbbode.chatbot.sample.config;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.chatbot.core.common.result.DefaultBrainResult;
import com.github.kingbbode.messenger.slack.event.BlockActionDispatcherBrain;
import com.github.kingbbode.messenger.slack.result.SlackMessageBrainResult;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SlackActionConfig {

    @Bean
    public BlockActionDispatcherBrain slackTest1() {
        return new BlockActionDispatcherBrain() {

            @Override
            public String getActionId() {
                return "ACTION1";
            }

            @Override
            public DispatcherBrain dispatcher() {
                return brainRequest -> DefaultBrainResult.builder()
                    .message(brainRequest.getContent())
                    .build();
            }
        };
    }

    @Bean
    public BlockActionDispatcherBrain slackTest2() {
        return new BlockActionDispatcherBrain() {

            @Override
            public String getActionId() {
                return "ACTION2";
            }

            @Override
            public DispatcherBrain dispatcher() {
                return brainRequest -> SlackMessageBrainResult.builder()
                    .room(brainRequest.getRoom())
                    .blocks(
                        Collections.singletonList(
                            SectionBlock.builder()
                                .text(
                                    MarkdownTextObject.builder()
                                        .text("Action2!")
                                        .build()
                                )
                                .build()
                        )
                    )
                    .build();
            }
        };
    }
}
