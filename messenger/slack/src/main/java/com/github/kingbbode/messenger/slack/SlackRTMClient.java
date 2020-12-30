package com.github.kingbbode.messenger.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.event.Event;
import com.slack.api.model.event.GoodbyeEvent;
import com.slack.api.rtm.RTMClient;
import com.slack.api.rtm.RTMEventHandler;
import com.slack.api.rtm.RTMEventsDispatcher;
import com.slack.api.rtm.RTMEventsDispatcherFactory;
import com.slack.api.rtm.message.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.tyrus.core.l10n.LocalizationMessages;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

@Slf4j
public class SlackRTMClient extends RTMEventHandler<GoodbyeEvent> implements InitializingBean, DisposableBean {
	private final RTMClient rtm;
	private final MethodsClient methodsClient;

	public SlackRTMClient(String token) throws IOException, DeploymentException {
		this.rtm = Slack.getInstance().rtm(token);
		this.methodsClient = Slack.getInstance().methods(token);
		rtm.connect();
		log.info("[BOT] Connect Slack BOT. {}", token);
	}

	@Override
	public void destroy() throws Exception {
		rtm.close();
	}

	public void addMessageHandler(RTMEventHandler<? extends Event> handler) {
		RTMEventsDispatcher dispatcher = RTMEventsDispatcherFactory.getInstance();
		dispatcher.register(handler);
		rtm.addMessageHandler(dispatcher.toMessageHandler());
	}

	public void sendMessage(Message message) {
		if(Objects.isNull(message)  || StringUtils.isEmpty(message.getChannel())) {
			return;
		}
		if(!CollectionUtils.isEmpty(message.getBlocks()) || !CollectionUtils.isEmpty(message.getAttachments())) {
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
		} else {
			try {
				rtm.sendMessage(message.toJSONString());
			} catch (IllegalStateException e) {
				if(LocalizationMessages.CONNECTION_HAS_BEEN_CLOSED().equals(e.getMessage())) {
					handle(null);
					sendMessage(message);
				}
			}
		}
	}

	@Override
	@SneakyThrows
	public void handle(GoodbyeEvent event) {
		reconnect();
	}

	private void reconnect() throws InterruptedException {
		try {
			rtm.reconnect();
			log.info("reconnect succeed.");
		} catch (IOException | DeploymentException | URISyntaxException | SlackApiException e) {
			log.error("reconnect failed. message={}", e.getMessage(), e);
			Thread.sleep(1_000);
			reconnect();
		}
	}

	@Override
	public void afterPropertiesSet() {
		addMessageHandler(this);
	}
}
