package com.github.kingbbode.messenger.slack.event;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;

public interface BlockActionDispatcherBrain {
    String getActionId();
    DispatcherBrain dispatcher();
}
