package org.jaeyo.clien_stream.repo.mongodb;

import static org.junit.Assert.assertNotNull;

import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MongoDbAdapterTest {

	private void init(){
//		Conf.set(ConfKey.MONGODB_IP, "180.231.38.153");
		Conf.set(ConfKey.MONGODB_IP, "180.231.38.153");
		Conf.set(ConfKey.MONGODB_PORT, "27017");
		Conf.set(ConfKey.MONGODB_DBNAME, "test_db");
	} //init
	
	@Test
	public void test() {
		init();
		assertNotNull(MongoDbAdapter.getInstance().getCollection("test"));
	} //test
} //class