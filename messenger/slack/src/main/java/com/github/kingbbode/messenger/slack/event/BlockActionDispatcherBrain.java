package com.github.kingbbode.messenger.slack.event;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;

import java.util.regex.Pattern;

public interface BlockActionDispatcherBrain {
    Pattern getActionId();
    DispatcherBrain dispatcher();
}
