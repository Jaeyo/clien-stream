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
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
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
		ArrayList<BbsItem> items= parser.parseBbs(bbsName.toString().toLowerCase(), 1);
		
		if(items==null || items.size()==0){
			logger.warn("parse bbs ({}), but return nothing", bbsName);
			return;
		} //if
		
		insertBbsItems(items);
		
		for(Long unstoredNum : findUnstoredArticle()){
			ArticleItem parsedArticle=parser.parseArticle(bbsName, unstoredNum);
			if(parsedArticle==null)
				continue;
			updateArticle(unstoredNum, parsedArticle);
		} //for unstoredNum
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
	
		List<DBObject> inserts=new ArrayList<DBObject>();
		for(BbsItem item : items)
			inserts.add(item);
		coll.insert(inserts);
	} //insertBbsItem
	
	private List<Long> findUnstoredArticle(){
		String collectionName=String.format("bbsItem_%s", bbsName);
		DBCollection coll=MongoDbAdapter.getInstance().getCollection(collectionName);	
		
		List<Long> unstoredArticleNums=new ArrayList<Long>();
		for(DBObject unstoredArticleNum : coll.find(new BasicDBObject("article", null), new BasicDBObject("num", 1)))
			unstoredArticleNums.add((Long)unstoredArticleNum.get("num"));
		return unstoredArticleNums;
	} //findUnstoredArticle
	
	private void updateArticle(long num, ArticleItem article){
		String collectionName=String.format("bbsItem_%s", bbsName);
		DBCollection coll=MongoDbAdapter.getInstance().getCollection(collectionName);
		
		DBObject whereQueryDbObj=(DBObject)JSON.parse(String.format("{num : { $eq : %s }}", num));
		BasicDBObject storedDbObj=(BasicDBObject) coll.find(whereQueryDbObj).next();
		BbsItem storedBbsItem=new BbsItem(storedDbObj);
		
		storedBbsItem.setArticle(article);
		coll.update(whereQueryDbObj, storedBbsItem);
	} //updateArticle
} // class