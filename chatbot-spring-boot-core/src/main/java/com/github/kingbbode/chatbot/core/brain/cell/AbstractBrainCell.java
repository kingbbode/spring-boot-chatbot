package com.github.kingbbode.chatbot.core.brain.cell;


import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import com.github.kingbbode.chatbot.core.common.result.DefaultBrainResult;

/**
 * Created by YG on 2017-01-26.
 */


public abstract class AbstractBrainCell implements BrainCell {
    @Override
    public String explain() {
        return "";
    }

    public static AbstractBrainCell NOT_FOUND_BRAIN_CELL = new AbstractBrainCell() {
        @Override
        public BrainResult execute(BrainRequest brainRequest) {
            return DefaultBrainResult.NONE;
        }

        @Override
        public String explain() {
            return "존재하지 않는 기능입니다.";
        }
    };
}
