package com.github.kingbbode.chatbot.core.common.interfaces;

/**
 * Created by YG on 2017-07-10.
 */
public interface Dispatcher<T, K> {
    K dispatch(T event);
}
