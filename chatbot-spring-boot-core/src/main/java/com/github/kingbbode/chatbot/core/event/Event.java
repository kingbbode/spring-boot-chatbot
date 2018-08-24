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
public class Event<T> {
    private Dispatcher<T> dispatcher;
    private T item;

    public Event(Dispatcher<T> dispatcher, T item) {
        this.dispatcher = dispatcher;
        this.item = item;
    }

    //TODO: 결과를 반환한다.
    public void execute(){
        try {
            dispatcher.onMessage(dispatcher.dispatch(item));
        } catch (EmptyResultException e) {
        }
    }
}
