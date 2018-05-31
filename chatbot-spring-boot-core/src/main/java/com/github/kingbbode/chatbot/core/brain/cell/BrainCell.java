package com.github.kingbbode.chatbot.core.brain.cell;


import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by YG on 2017-01-26.
 */
public interface BrainCell {
    String explain();
    BrainResult execute(BrainRequest brainRequest) throws InvocationTargetException, IllegalAccessException;
}
