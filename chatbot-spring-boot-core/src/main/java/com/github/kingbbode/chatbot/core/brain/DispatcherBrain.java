package com.github.kingbbode.chatbot.core.brain;

import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;

public interface DispatcherBrain {
    BrainResult execute(BrainRequest brainRequest);
}
