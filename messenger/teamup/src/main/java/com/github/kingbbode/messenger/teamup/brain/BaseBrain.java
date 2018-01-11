package com.github.kingbbode.messenger.teamup.brain;

import com.github.kingbbode.chatbot.core.common.annotations.Brain;
import com.github.kingbbode.chatbot.core.common.annotations.BrainCell;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;

@Brain
public class BaseBrain {
    @BrainCell(key = "나가", explain = "쫓아내기", function = "getout", type = "out")
    public String outRoom(BrainRequest brainRequest) {
        return "바이바이~";
    }
}
