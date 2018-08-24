package com.github.kingbbode.chatbot.core.common.interfaces;

import com.github.kingbbode.chatbot.core.event.Event;

import java.util.List;

public interface EventSensor {
    List<Event> sensingEvent();
}
