package com.github.kingbbode.chatbot.core.common.exception;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * Created by YG on 2017-02-08.
 */
public class BrainException extends UndeclaredThrowableException {
    
    

    public BrainException(Throwable undeclaredThrowable) {
        super(undeclaredThrowable);
    }

    public BrainException(Throwable undeclaredThrowable, String s) {
        super(undeclaredThrowable, s);
    }
}
