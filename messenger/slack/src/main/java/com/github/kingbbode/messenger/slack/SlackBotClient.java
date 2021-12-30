package com.github.kingbbode.messenger.slack;

import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.rtm.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
public class SlackBotClient {
	private final MethodsClient methodsClient;

	public SlackBotClient(MethodsClient methodsClient) throws IOException {
		this.methodsClient = methodsClient;
	}

	public void sendMessage(Message message) {
		if(StringUtils.isEmpty(message.getChannel())) {
			return;
		}

		try {
			methodsClient.chatPostMessage(ChatPostMessageRequest.builder()
				.channel(message.getChannel())
				.text(message.getText())
				.blocks(message.getBlocks())
				.attachments(message.getAttachments())
				.build()
			);
		} catch (IOException | SlackApiException e) {
			log.warn("slack chat post failed. {}", e.getMessage());
		}
	}
}
