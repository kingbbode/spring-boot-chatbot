package com.github.kingbbode.messenger.slack.result;

import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import com.slack.api.model.block.LayoutBlock;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SlackBlockBrainResult implements BrainResult {

    private final String room;
    private final List<LayoutBlock> blocks;

    @Builder
    public SlackBlockBrainResult(String room, List<LayoutBlock> blocks) {
        this.room = room;
        this.blocks = blocks;
    }
}
