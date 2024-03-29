package com.github.kingbbode.messenger.slack;

import com.github.kingbbode.chatbot.core.common.interfaces.Dispatcher;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import com.github.kingbbode.chatbot.core.common.result.SimpleMessageBrainResult;
import com.github.kingbbode.messenger.slack.event.SlackEvent;
import com.github.kingbbode.messenger.slack.result.SlackMessageBrainResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
public class SlackDispatcher implements Dispatcher<SlackEvent>, InitializingBean {
    private static final String MESSENGER = "SLACK";
    private final SlackBotClient slackBotClient;

    public SlackDispatcher(SlackBotClient slackRTMClient) {
        this.slackBotClient = slackRTMClient;
    }

    @Override
    public BrainRequest dispatch(SlackEvent message) {
        return BrainRequest.builder()
            .messenger(MESSENGER)
            .messageNo(message.getClientMsgId())
            .user(message.getUser())
            .room(message.getChannel())
            .thread(message.getThead())
            .content(message.getValue())
            .build();
    }

    @Override
    public void onMessage(BrainRequest brainRequest, BrainResult result) {
        if (result instanceof SimpleMessageBrainResult) {
            slackBotClient.sendMessage(SlackMessage.builder()
                .channel(extractChannel(result, brainRequest))
                .threadTs(result.getThread())
                .text("<@" + brainRequest.getUser() + ">\n" + ((SimpleMessageBrainResult) result).getMessage())
                .build()
            );
        }
        else if (result instanceof SlackMessageBrainResult) {
            SlackMessageBrainResult slackMessageResult = (SlackMessageBrainResult) result;
            slackBotClient.sendMessage(
                SlackMessage.builder()
                    .channel(extractChannel(result, brainRequest))
                    .threadTs(result.getThread())
                    .text(slackMessageResult.getText())
                    .blocks(slackMessageResult.getBlocks())
                    .attachments(slackMessageResult.getAttachments())
                    .build()
            );
        }
        else {
            log.warn("not support result type. {}", result.getClass().getSimpleName());
        }
    }

    private String extractChannel(BrainResult result, BrainRequest brainRequest) {
        return StringUtils.isEmpty(result.getRoom()) ? brainRequest.getRoom() : result.getRoom();
    }

    @Override
    public void afterPropertiesSet() {
        log.info("[BOT] Registered SlackDispatcher.");
    }
}
