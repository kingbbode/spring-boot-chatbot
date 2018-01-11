package com.github.kingbbode.chatbot.core.common.exception;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * Created by YG on 2017-02-08.
 */
public class ArgumentInvalidException extends UndeclaredThrowableException {
    
    

    public ArgumentInvalidException(Throwable undeclaredThrowable) {
        super(undeclaredThrowable);
    }

    public ArgumentInvalidException(Throwable undeclaredThrowable, String s) {
        super(undeclaredThrowable, s);
    }
}
