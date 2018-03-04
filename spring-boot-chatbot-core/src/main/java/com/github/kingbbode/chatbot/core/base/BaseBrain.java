package com.github.kingbbode.chatbot.core.base;

import com.github.kingbbode.chatbot.core.base.knowledge.component.KnowledgeComponent;
import com.github.kingbbode.chatbot.core.base.stat.StatComponent;
import com.github.kingbbode.chatbot.core.brain.cell.CommonBrainCell;
import com.github.kingbbode.chatbot.core.brain.factory.BrainFactory;
import com.github.kingbbode.chatbot.core.common.annotations.Brain;
import com.github.kingbbode.chatbot.core.common.annotations.BrainCell;
import com.github.kingbbode.chatbot.core.common.request.BrainRequest;
import com.github.kingbbode.chatbot.core.common.util.BrainUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

/**
 * Created by YG-MAC on 2017. 1. 27..
 */
@Brain
public class BaseBrain {

    @Autowired
    private BrainFactory brainFactory;

    @Autowired(required = false)
    private KnowledgeComponent knowledgeComponent;

    @Autowired
    private StatComponent statComponent;

    @BrainCell(key = "기능", explain = "기능 목록 출력", function = "1")
    public String explain(BrainRequest brainRequest) {
        String explain =   "**** 기능 목록 **** \n" +
                BrainUtil.explainDetail(brainFactory.findBrainCellByType(CommonBrainCell.class));

        if(!ObjectUtils.isEmpty(this.knowledgeComponent)) {
            explain += "\n**** 학습 목록 **** \n" + BrainUtil.explainForKnowledge(knowledgeComponent.getCommands());
        }
        return explain;
    }
       
    @BrainCell(key = "고유정보", explain = "고유 정보 추출",function = "2")
    public String info(BrainRequest brainRequest){
        return brainRequest.toString();
    }
    
    @BrainCell(key = "기능통계", explain = "뭘 많이 쓰는지..", function = "3")
    public String stat(BrainRequest brainRequest) {
        return statComponent.toString();
    }
}
