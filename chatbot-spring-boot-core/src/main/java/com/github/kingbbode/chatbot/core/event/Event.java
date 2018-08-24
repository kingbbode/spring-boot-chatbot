package com.github.kingbbode.chatbot.core.event;

import com.github.kingbbode.chatbot.core.common.interfaces.Dispatcher;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by YG on 2017-07-10.
 */
@Getter
@Setter
@NoArgsConstructor
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
