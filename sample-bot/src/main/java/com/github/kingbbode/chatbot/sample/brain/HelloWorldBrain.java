package com.github.kingbbode.chatbot.sample.brain;

import com.github.kingbbode.chatbot.core.common.annotations.Brain;
import com.github.kingbbode.chatbot.core.common.annotations.BrainCell;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.messenger.slack.result.SlackMessageBrainResult;
import com.slack.api.model.block.ActionsBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.ButtonElement;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Brain
public class HelloWorldBrain {

    @BrainCell(key="안녕", function = "hello")
    public String hello(BrainRequest brainRequest) {
        return  "안녕~~";
    }


    @BrainCell(key = "따라해봐", function = "echo-start")
    public String echo(BrainRequest brainRequest) {
        return  "말해봐";
    }

    @BrainCell(function = "echo-end", parent = "echo-start")
    public String echo2(BrainRequest brainRequest) {
        return  brainRequest.getContent();
    }


    private Map<String, String> map = new HashMap<>();

    @BrainCell(key = "기록", function = "record-start")
    public String record(BrainRequest brainRequest) {
        return  "조회? 저장?";
    }

    @BrainCell(function = "record-read", key="조회", parent = "record-start")
    public String record2(BrainRequest brainRequest) {
        return  "무엇을?";
    }

    @BrainCell(function = "record-read-2", parent = "record-read")
    public String record21(BrainRequest brainRequest) {
        return  map.getOrDefault(brainRequest.getContent(), "저장된 내용이 없다");
    }

    @BrainCell(function = "record-save", key="저장", parent = "record-start")
    public String record3(BrainRequest brainRequest) {
        return  "무엇을?";
    }

    @BrainCell(function = "record-save-2", parent = "record-save")
    public String record4(BrainRequest brainRequest) {
        brainRequest.getConversation().put("key", brainRequest.getContent());
        return  "내용은?";
    }

    @BrainCell(function = "record-save-3", parent = "record-save-2")
    public String record5(BrainRequest brainRequest) {
        map.put(brainRequest.getConversation().getParam().get("key"), brainRequest.getContent());
        return  "저장했다";
    }



    @BrainCell(key="슬랙테스트", function = "slack")
    public SlackMessageBrainResult slack(BrainRequest brainRequest) {
        return SlackMessageBrainResult.builder()
            .room(brainRequest.getRoom())
            .blocks(
                Arrays.asList(
                    ActionsBlock.builder()
                        .elements(
                            Collections.singletonList(
                                ButtonElement.builder()
                                    .text(PlainTextObject.builder()
                                        .text("Farmhouse")
                                        .emoji(true)
                                        .build()
                                    )
                                    .value("king")
                                    .actionId("ACTION1")
                                    .build()
                            )
                        )
                        .build(),
                    SectionBlock.builder()
                        .text(
                            MarkdownTextObject.builder()
                            .text("This is a section block with a button.")
                            .build()
                        )
                        .accessory(
                            ButtonElement.builder()
                                .text(PlainTextObject.builder()
                                    .text("Click Me")
                                    .emoji(true)
                                    .build()
                                )
                                .value("bbode")
                                .actionId("ACTION2")
                                .build()
                        )
                        .build()
                )
            )
            .build();
    }
}
