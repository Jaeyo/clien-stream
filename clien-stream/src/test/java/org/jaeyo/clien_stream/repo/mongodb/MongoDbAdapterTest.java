package org.jaeyo.clien_stream.repo.mongodb;

import static org.junit.Assert.*;

import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.junit.Test;

public class MongoDbAdapterTest {

	private void init(){
		Conf.set(ConfKey.MONGODB_IP, "192.168.0.10");
		Conf.set(ConfKey.MONGODB_PORT, "27017");
		Conf.set(ConfKey.MONGODB_DBNAME, "test_db");
	} //init
	
	@Test
	public void test() {
		init();
		assertNotNull(MongoDbAdapter.getInstance().getCollection("test"));
	} //test
} //class