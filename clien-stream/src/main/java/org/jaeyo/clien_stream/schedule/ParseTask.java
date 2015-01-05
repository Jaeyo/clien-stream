package org.jaeyo.clien_stream.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Predicate;

import javax.jms.JMSException;

import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.ArticleItem;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.jaeyo.clien_stream.mq.ActiveMQAdapter;
import org.jaeyo.clien_stream.parser.BbsParser;
import org.jaeyo.clien_stream.repo.mongodb.MongoDbAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class ParseTask extends TimerTask {
	private static final Logger logger = LoggerFactory.getLogger(ParseTask.class);
	private BbsParser parser = null;
	private BbsNames bbsName;

	public ParseTask(BbsParser parser, BbsNames bbsName) {
		this.parser = parser;
		this.bbsName = bbsName;
	} // INIT
	
	@Override
	public void run() {
		ArrayList<BbsItem> items= parser.parseBbs(bbsName, 1);
		
		if(items==null || items.size()==0){
			logger.warn("parse bbs ({}), but return nothing", bbsName);
			return;
		} //if
		
		insertBbsItems(items);
		
		String topic=String.format("topic-%s", bbsName.toString().toLowerCase()); 
		for(BbsItem item : items){
			try {
				ActiveMQAdapter.produceMessage(topic, item);
			} catch (JMSException e) {
				logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName()));
			} //catch
		} //for item
	} // run
	
	private void insertBbsItems(ArrayList<BbsItem> items){
		String collectionName=String.format("bbsItem_%s", bbsName);
		DBCollection coll=MongoDbAdapter.getInstance().getCollection(collectionName);
		
		BasicDBList nums=new BasicDBList();
		for(BbsItem item : items)
			nums.add(item.getNum());
	
		final ArrayList<Long> existsNums=new ArrayList<Long>();
		for(DBObject existsNumObj: coll.find(new BasicDBObject("num", new BasicDBObject("$in", nums))))
			existsNums.add((Long) existsNumObj.get("num"));
		
		items.removeIf(new Predicate<BbsItem>() {
			@Override
			public boolean test(BbsItem t) {
				if(existsNums.contains(t.getNum()))
					return true;
				return false;
			} //test
		});
	
		BulkWriteOperation bulk=coll.initializeUnorderedBulkOperation();
		if(items==null || items.size()==0)
			return;
		
		for(BbsItem item : items)
			bulk.insert(item.toDBObject());
		bulk.execute();
	} //insertBbsItem
} // class