package org.jaeyo.clien_stream.repo.mongodb;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.jaeyo.clien_stream.common.ReflectionUtil;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.junit.Test;

import com.mongodb.DBCollection;

public class RepoInsertConsumerMongoTest {
	private void init() {
		Conf.set(ConfKey.MONGODB_IP, "192.168.0.10");
		Conf.set(ConfKey.MONGODB_PORT, "27017");
		Conf.set(ConfKey.MONGODB_DBNAME, "test_db");
	} // init

	@Test
	public void test_insertBbsItems() {
		init();
		HashMap<Long, BbsItem> itemMap=new HashMap<Long, BbsItem>();
		String collectionName="testcollectionname";
		
		for (int i = 0; i < 10; i++)
			itemMap.put((long) (i+100), new BbsItem(i+100, "title"+i, "nick"+i, "imgNick"+i, System.currentTimeMillis(), 1, "test"));
		
		MongoDbAdapter.getInstance().getCollection(collectionName).drop();
		
		ReflectionUtil.invokePrivateMethod(RepoInsertConsumerMongo.class, "insertBbsItems", void.class, new Class<?>[]{Map.class, String.class}, itemMap, collectionName);
		
		assertTrue(MongoDbAdapter.getInstance().getCollection(collectionName).find().size()==10);
		
		itemMap.put((long) (99), new BbsItem(99, "title", "nick", "imgNick", System.currentTimeMillis(), 1, "test"));
		ReflectionUtil.invokePrivateMethod(RepoInsertConsumerMongo.class, "insertBbsItems", void.class, new Class<?>[]{Map.class, String.class}, itemMap, collectionName);
		
		assertTrue(MongoDbAdapter.getInstance().getCollection(collectionName).find().size()==11);
		
		MongoDbAdapter.getInstance().getCollection(collectionName).drop();
	} // test_insertBbsItems
	
	@Test
	public void test() throws JMSException{
		init();
		
		String bbsName="test";
		String collectionName = String.format("bbsItem_%s", bbsName);
		
		ArrayList<BbsItem> items=new ArrayList<BbsItem>();
		for(int i=0; i<10; i++)
			items.add(new BbsItem(i+100, "title", "nick", "asdf", System.currentTimeMillis(), 1, bbsName));
		
		ObjectMessage objectMessage=new ActiveMQObjectMessage();
		objectMessage.setObject(items);
				
		MessageListener msgListener=ReflectionUtil.getPrivateField(new RepoInsertConsumerMongo(), "parseBbsListener", MessageListener.class);
		assertNotNull(msgListener);
		msgListener.onMessage(objectMessage);
		
		DBCollection coll=MongoDbAdapter.getInstance().getCollection(collectionName);
		assertTrue(coll.find().size()==10);
		coll.drop();
	} //test
} // class