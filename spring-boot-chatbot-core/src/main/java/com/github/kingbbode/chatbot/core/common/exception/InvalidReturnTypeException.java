package com.github.kingbbode.chatbot.core.common.exception;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * Created by YG on 2017-02-08.
 */
public class InvalidReturnTypeException extends UndeclaredThrowableException {

    public InvalidReturnTypeException(Throwable undeclaredThrowable) {
        super(undeclaredThrowable);
    }

    public InvalidReturnTypeException(Throwable undeclaredThrowable, String s) {
        super(undeclaredThrowable, s);
    }
}
