package com.github.kingbbode.chatbot.core.common.interfaces;

import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import com.github.kingbbode.chatbot.core.event.EmptyResultException;

/**
 * Created by YG on 2017-07-10.
 */
public interface Dispatcher<T> {
    BrainResult dispatch(T event);
    void onMessage(BrainResult result);

    default BrainResult skip() {
        throw new EmptyResultException();
    }
}
