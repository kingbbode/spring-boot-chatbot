package com.github.kingbbode.chatbot.core.event;

import com.github.kingbbode.chatbot.core.brain.DispatcherBrain;
import com.github.kingbbode.chatbot.core.common.interfaces.Dispatcher;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

/**
 * Created by YG on 2017-07-10.
 */
@Getter
@Setter
@NoArgsConstructor
public class Event<T> {
    private Dispatcher<T> dispatcher;
    private T item;
    private DispatcherBrain brain;

    public Event(Dispatcher<T> dispatcher, T item, DispatcherBrain brain) {
        this.dispatcher = dispatcher;
        this.item = item;
        this.brain = brain;
    }

    protected void setBrain(DispatcherBrain brain) {
        this.brain = brain;
    }

    public void execute(){
        try {
            BrainRequest request = dispatcher.dispatch(item);
            if(ObjectUtils.isEmpty(request)) {
                return;
            }
            BrainResult result = brain.execute(request);
            if(ObjectUtils.isEmpty(result)) {
                return;
            }
            dispatcher.onMessage(request, result);
        } catch (EmptyResultException e) {
            //noop
        }
    }
}
