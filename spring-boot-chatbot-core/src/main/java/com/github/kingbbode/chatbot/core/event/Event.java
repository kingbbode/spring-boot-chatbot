package com.github.kingbbode.chatbot.core.event;

import com.github.kingbbode.chatbot.core.common.interfaces.Dispatcher;
import lombok.Data;

/**
 * Created by YG on 2017-07-10.
 */
@Data
public class Event<T, K> {
    private Dispatcher<T, K> dispatcher;
    private T item;

    public Event(Dispatcher<T, K> dispatcher, T item) {
        this.dispatcher = dispatcher;
        this.item = item;
    }

    public void execute(){
        dispatcher.dispatch(item);
    }
}
