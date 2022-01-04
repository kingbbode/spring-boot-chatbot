package com.github.kingbbode.messenger.slack.result;

import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import com.slack.api.model.Attachment;
import com.slack.api.model.block.LayoutBlock;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SlackMessageBrainResult implements BrainResult {

    private final String room;
    private final String thread;
    private final String text;
    private final List<LayoutBlock> blocks;
    private final List<Attachment> attachments;

    @Builder
    public SlackMessageBrainResult(String room, String thread, String text, List<LayoutBlock> blocks, List<Attachment> attachments) {
        this.room = room;
        this.thread = thread;
        this.text = text;
        this.blocks = blocks;
        this.attachments = attachments;
    }
}
