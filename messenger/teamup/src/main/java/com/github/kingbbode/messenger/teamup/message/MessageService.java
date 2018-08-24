package com.github.kingbbode.messenger.teamup.message;

import com.github.kingbbode.chatbot.core.common.result.BrainResult;
import com.github.kingbbode.messenger.teamup.request.FileRequest;
import com.github.kingbbode.messenger.teamup.response.EventResponse;
import com.github.kingbbode.messenger.teamup.response.FileUploadResponse;
import com.github.kingbbode.messenger.teamup.response.MessageResponse;
import com.github.kingbbode.messenger.teamup.response.RoomCreateResponse;
import com.github.kingbbode.messenger.teamup.templates.template.EdgeTemplate;
import com.github.kingbbode.messenger.teamup.templates.template.FileTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

/**
 * Created by YG on 2016-03-28.
 */
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    
    private final EdgeTemplate edgeTemplate;
    
    private final FileTemplate fileTemplate;

    /*public String excuteMessageForChat(String content, String command) {
        return getMessageResult("999999999999", "8170", content, command);
    }*/

    public MessageResponse.Message readMessage(EventResponse.Event.Chat chat) {
        MessageResponse readResponse = edgeTemplate.readMessage(chat.getMsg(), chat.getRoom());
        if (!ObjectUtils.isEmpty(readResponse) && readResponse.getMsgs().size() > 0) {
            return  readResponse.getMsgs().get(0);
        }
        return null;
    }

    public void sendMessage(BrainResult result) {
        edgeTemplate.sendMessage(result.getMessage(), result.getRoom());
    }
    
    public RoomCreateResponse openRoom(BrainResult result) {
        return edgeTemplate.openRoom(result.getMessage());
    }

    public void sendImage(BrainResult result) {
        edgeTemplate.sendImage(result.getMessage(), result.getRoom());
    }

    public void writeFeed(BrainResult result) {
        edgeTemplate.writeFeed(result.getMessage(), result.getRoom());
    }
    
    public FileUploadResponse writeImage(FileRequest fileRequest) {
        byte[] bytes = fileTemplate.download(fileRequest);
        if(bytes == null){
            return null;
        }
        return fileTemplate.upload(fileRequest, bytes);
    }
    
    public void outRoom(BrainResult result) {
        edgeTemplate.sendMessage(result.getMessage(), result.getRoom());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            log.warn("InterruptedException");
        }
        edgeTemplate.outRoom(result.getRoom());
    }
}
