package com.github.kingbbode.chatbot.core.common.result;

/**
 * Created by YG on 2016-06-22.
 */
public class CommonResult {
    
    CommonResult(boolean success, String message){
        this.success = success;
        this.message = message;
    }
    
    private boolean success;

    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public static CommonResult createSuccess(String message){
        return new CommonResult(true, message);
    }
    
    public static CommonResult createFail(String message){
        return new CommonResult(false, message);
    }
}
