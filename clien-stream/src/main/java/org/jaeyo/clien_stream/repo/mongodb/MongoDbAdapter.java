package org.jaeyo.clien_stream.repo.mongodb;

import java.net.UnknownHostException;

import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class MongoDbAdapter {
	private static final Logger logger=LoggerFactory.getLogger(MongoDbAdapter.class);
	private static MongoDbAdapter INSTANCE = null;
	
	private MongoClient mongoClient;
	private DB db=null;

	private MongoDbAdapter() {
		try {
			this.mongoClient=new MongoClient(new ServerAddress(Conf.get(ConfKey.MONGODB_IP), Integer.parseInt(Conf.get(ConfKey.MONGODB_PORT))));
			this.db=mongoClient.getDB(Conf.get(ConfKey.MONGODB_DBNAME));
		} catch (NumberFormatException | UnknownHostException e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
		} //catch
	} // INIT

	public static MongoDbAdapter getInstance() {
		synchronized (MongoDbAdapter.class) {
			if (INSTANCE == null)
				INSTANCE = new MongoDbAdapter();
		} // sync
		return INSTANCE;
	} // getInstance
	
	public DBCollection getCollection(String collName){
		return db.getCollection(collName);
	} //getCollection
} // class