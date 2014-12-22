package org.jaeyo.clien_stream.websocket;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.jaeyo.clien_stream.entity.BbsItem;
import org.jaeyo.clien_stream.mq.ActiveMQAdapter;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;

public class WebSocketMessageListener implements MessageListener {
	private static final Logger logger=LoggerFactory.getLogger(WebSocketMessageListener.class);
	private WebSocket socket;

	public WebSocketMessageListener(@NotNull WebSocket socket) throws NullPointerException {
		Preconditions.checkNotNull(socket);
		this.socket = socket;
	} // INIT

	@Override
	public void onMessage(Message message) {
		try {
			if (socket.isClosed()) {
				ActiveMQAdapter.unlisten(this);
				return;
			} // if
	
			BbsItem bbsItem= (BbsItem) ((ObjectMessage) message).getObject();
			socket.send(bbsItem.toJSON());
		} catch (JMSException e) {
			e.printStackTrace();
		} // catch
	} // onMessage
} // class