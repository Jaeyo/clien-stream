package org.jaeyo.clien_stream;

import javax.inject.Inject;

import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.jaeyo.clien_stream.repo.RepoInsertConsumer;
import org.jaeyo.clien_stream.schedule.ScheduleManager;
import org.jaeyo.clien_stream.websocket.WebChatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private static final Logger logger=LoggerFactory.getLogger(App.class);
	
	@Inject
	private static RepoInsertConsumer repoInsertConsumer;
	
	public static void main(String[] args) throws InterruptedException {
		logger.info("started");
		
		Conf.set(ConfKey.PORT, "1234");
		Conf.set(ConfKey.WEB_SOCKET_PORT, "1235");
		Conf.set(ConfKey.MONGODB_IP, "192.168.0.10");
		Conf.set(ConfKey.MONGODB_PORT, "27017");
		Conf.set(ConfKey.MONGODB_DBNAME, "clienstream");
	
		repoInsertConsumer.listen();
		
		ScheduleManager.schedule();
		
		WebChatServer.startServer(Integer.parseInt(Conf.get(ConfKey.WEB_SOCKET_PORT)));
		
		JettyServer jetty=new JettyServer();
		jetty.start();
		jetty.join();
	} //main
} //class