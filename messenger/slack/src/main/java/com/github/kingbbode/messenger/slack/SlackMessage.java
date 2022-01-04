package com.github.kingbbode.messenger.slack;

import com.slack.api.model.Attachment;
import com.slack.api.model.block.LayoutBlock;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SlackMessage {
    private String channel;
    private String threadTs;
    private String text;
    private List<LayoutBlock> blocks;
    private List<Attachment> attachments;

    @Builder
    public SlackMessage(String channel, String threadTs, String text, List<LayoutBlock> blocks, List<Attachment> attachments) {
        this.channel = channel;
        this.threadTs = threadTs;
        this.text = text;
        this.blocks = blocks;
        this.attachments = attachments;
    }
}
