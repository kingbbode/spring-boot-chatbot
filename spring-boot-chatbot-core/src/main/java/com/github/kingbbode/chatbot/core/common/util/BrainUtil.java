package com.github.kingbbode.chatbot.core.common.util;

import com.github.kingbbode.chatbot.core.brain.cell.AbstractBrainCell;
import com.github.kingbbode.chatbot.core.brain.factory.BrainFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by YG on 2016-04-12.
 */
public class BrainUtil {

    public static <T extends AbstractBrainCell> String explainDetail(Set<BrainFactory.BrainCellInfo<T>> entrySet) {
        StringBuilder stringBuilder = new StringBuilder();
        entrySet
                .forEach(info -> {
                            stringBuilder.append(info.toString());
                            stringBuilder.append("\n");
                        }
                );
        return stringBuilder.toString();
    }

    public static String explainForKnowledge(Set<Map.Entry<String, List<String>>> entrySet) {
        StringBuilder stringBuilder = new StringBuilder();
        entrySet
                .forEach(entry -> {
                    stringBuilder.append(entry.getKey());
                    stringBuilder.append(" - 지식깊이 : ");
                    stringBuilder.append(entry.getValue().size());
                    stringBuilder.append("\n");
                });

        return stringBuilder.toString();
    }
}
