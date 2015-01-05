package org.jaeyo.clien_stream.websocket;

import java.net.InetSocketAddress;

import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.jaeyo.clien_stream.mq.ActiveMQAdapter;
import org.jaeyo.clien_stream.service.HomeService;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class WebMessagingServer extends WebSocketServer {
	private static final Logger logger = LoggerFactory.getLogger(WebMessagingServer.class);
	private static Table<WebSocket, String, Object> attrsPerSocket = HashBasedTable.create();

	public WebMessagingServer(int port) {
		super(new InetSocketAddress(port));
	} // INIT

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		logger.info("open from {}", conn.getRemoteSocketAddress().getAddress().toString());
	} // onOpen

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		logger.info("close from {}, code : {}, reason : {}, remote : {}", conn.getRemoteSocketAddress().getAddress().toString(), code, reason, remote);
		try{
			WebSocketMessageListener mqListener=(WebSocketMessageListener) attrsPerSocket.remove(conn, "mqListener");
			ActiveMQAdapter.unlisten(mqListener);
		} catch(Exception e){
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
		} //catch
	} // onClose

	@Override
	public void onMessage(WebSocket conn, String message) {
		logger.info("message : {} from {}", message, conn.getRemoteSocketAddress().getAddress().toString());

		try{
			JSONObject json=new JSONObject(message);
			String bbsName=json.getString("bbsName");
	
			for(BbsItem item : new HomeService().selectArticles(BbsNames.valueOf(bbsName.toUpperCase()), 100))
				conn.send(item.toJSON().toString());
			
			String topic=String.format("topic-%s", bbsName);
			WebSocketMessageListener mqListener=new WebSocketMessageListener(conn);
			attrsPerSocket.put(conn, "mqListener", mqListener);
			ActiveMQAdapter.listen(topic, mqListener);
		} catch(Exception e){
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
		} //catch
	} // onMessage

	@Override
	public void onError(WebSocket conn, Exception ex) {
		logger.error(String.format("%s, errmsg : %s", ex.getClass().getSimpleName(), ex.getMessage()), ex);
	} // onError

	public static WebMessagingServer startServer(int port) {
		WebMessagingServer server = new WebMessagingServer(port);
		server.start();
		logger.info("started");
		return server;
	} // startServer

	public static void stopServer(WebMessagingServer server) {
		logger.info("stop on port : {}", server.getPort());
		try {
			server.stop();
		} catch (Exception e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName()), e);
		} // catch
	} // stopServer
} // class