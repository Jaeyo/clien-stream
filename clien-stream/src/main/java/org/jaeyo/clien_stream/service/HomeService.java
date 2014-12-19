package org.jaeyo.clien_stream.service;

import java.util.ArrayList;
import java.util.List;

import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.jaeyo.clien_stream.repo.mongodb.MongoDbAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Service
public class HomeService {
	private static final Logger logger=LoggerFactory.getLogger(HomeService.class);
	
	public List<BbsItem> selectArticles(BbsNames bbsName, int limit){
		return selectArticles(bbsName, -1, limit);
	} //selectArticles
	
	public List<BbsItem> selectArticles(BbsNames bbsName, long startNum, int limit){
		String collectionName=String.format("bbsItem_%s", bbsName);
		DBCollection coll=MongoDbAdapter.getInstance().getCollection(collectionName);
		
		if(coll.count() < limit)
			limit=(int) coll.count();
		
		List<BbsItem> retList=new ArrayList<BbsItem>();
		String whereQuery=String.format("{$query : { article : { $ne : null}, num : { $gt : %s} }, $orderby : {num : 1} }", startNum);
		for(DBObject bbsItemObj: coll.find((DBObject)JSON.parse(whereQuery)).limit(limit))
			retList.add(new BbsItem((BasicDBObject) bbsItemObj));
		return retList;
	} //selectArticle
} //class