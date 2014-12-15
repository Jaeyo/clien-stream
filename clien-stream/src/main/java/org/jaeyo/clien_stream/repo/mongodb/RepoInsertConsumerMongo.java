package org.jaeyo.clien_stream.repo.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.jaeyo.clien_stream.entity.BbsItem;
import org.jaeyo.clien_stream.mq.ActiveMQAdapter;
import org.jaeyo.clien_stream.repo.RepoInsertConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class RepoInsertConsumerMongo implements RepoInsertConsumer {
	private static final Logger logger = LoggerFactory.getLogger(RepoInsertConsumerMongo.class);

	private MessageListener parseBbsListener = new MessageListener() {
		@Override
		public void onMessage(Message message) {
			try {
				ObjectMessage objectMessage = (ObjectMessage) message;
				ArrayList<BbsItem> items = (ArrayList<BbsItem>) objectMessage.getObject();

				if (items == null || items.size() == 0)
					return;

				Map<Long, BbsItem> itemMap = new HashMap<Long, BbsItem>();
				for (BbsItem item : items)
					itemMap.put(item.getNum(), item);

				String collectionName = String.format("bbsItem_%s", items.get(0).getBbsName());

				synchronized (RepoInsertConsumerMongo.class) {
					insertBbsItems(itemMap, collectionName);
				} // sync
			} catch (JMSException e) {
				logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
			} // catch
		} // onMessage
	};

	private void insertBbsItems(Map<Long, BbsItem> itemMap, String collectionName) {
		DBCollection coll = MongoDbAdapter.getInstance().getCollection(collectionName);
		
		BasicDBList nums = new BasicDBList();
		for (Long num : itemMap.keySet())
			nums.add(num);

		DBCursor resultCursor = coll.find(new BasicDBObject("_id", new BasicDBObject("$in", nums)));

		while (resultCursor.hasNext()) {
			DBObject next = resultCursor.next();
			Long existNum = (Long) next.get("_id");
			itemMap.remove(existNum);
		} // while

		List<DBObject> inserts = new ArrayList<DBObject>();
		for (BbsItem item : itemMap.values())
			inserts.add(item.toDBObject());

		coll.insert(inserts);
	} // insertBbsItems

	@Override
	public void listen() {
		try {
			ActiveMQAdapter.listen("parse-bbs", parseBbsListener);
		} catch (JMSException e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
		} // catch
	} // listen
} // class