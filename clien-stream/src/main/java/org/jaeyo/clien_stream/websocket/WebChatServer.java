package org.jaeyo.clien_stream.websocket;

import java.net.InetSocketAddress;

import org.jaeyo.clien_stream.mq.ActiveMQAdapter;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;
import org.lasti.PresentationChat.mq.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class WebChatServer extends WebSocketServer {
	private static final Logger logger = LoggerFactory.getLogger(WebChatServer.class);
	private static Table<WebSocket, String, String> attrsPerSocket = HashBasedTable.create();

	public WebChatServer(int port) {
		super(new InetSocketAddress(port));
	} // INIT

	@Override
	public void onOpen(final WebSocket conn, ClientHandshake handshake) {
		logger.info("open from {}", conn.getRemoteSocketAddress().getAddress().toString());
		try {
			ActiveMQAdapter.listen("chatMsg", new WebSocketMessageListener(conn));
		} catch (Exception e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
		} // catch
	} // onOpen

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		logger.info("close from {}, code : {}, reason : {}, remote : {}", conn.getRemoteSocketAddress().getAddress().toString(), code, reason, remote);
	} // onClose

	@Override
	public void onMessage(WebSocket conn, String message) {
		logger.info("message : {} from {}", message, conn.getRemoteSocketAddress().getAddress().toString());

		try{
			JSONObject json=new JSONObject(message);
			String kind=json.getString("kind");
			
			switch(kind){
			case "chatMsg":{
				String msg=json.getString("msg");
				String nick=attrsPerSocket.get(conn, "nick");
				Preconditions.checkArgument(msg!=null);
				Preconditions.checkArgument(nick!=null);
				ActiveMQAdapter.produceMessage("chatMsg", new ChatMessage(nick, msg));
				break;
			} //chatMsg
			case "setNick":{
				String nick=json.getString("nick");
				Preconditions.checkArgument(nick!=null);
				attrsPerSocket.put(conn, "nick", nick);
				break;
			} //setNick
			default:
				logger.error("unknown kind : {}", kind);
			} //switch
		} catch(JSONException e){
			logger.error(String.format("illegal json msg : %s", message), e);
		} catch(Exception e){
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
		} //catch
	} // onMessage

	@Override
	public void onError(WebSocket conn, Exception ex) {
		logger.error(String.format("%s, errmsg : %s", ex.getClass().getSimpleName(), ex.getMessage()), ex);
	} // onError

	public static WebChatServer startServer(int port) {
		WebChatServer server = new WebChatServer(port);
		server.start();
		logger.info("started");
		return server;
	} // startServer

	public static void stopServer(WebChatServer server) {
		logger.info("stop on port : {}", server.getPort());
		try {
			server.stop();
		} catch (Exception e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName()), e);
		} // catch
	} // stopServer
} // class