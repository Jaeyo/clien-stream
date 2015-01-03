package org.jaeyo.clien_stream.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jaeyo.clien_stream.common.CookieUtil;
import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.jaeyo.clien_stream.repo.mongodb.MongoDbAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

@Service
public class HomeService extends AbstractService {
	private static final Logger logger = LoggerFactory.getLogger(HomeService.class);

	public boolean putFixedItems(BbsNames bbsName, BbsItem bbsItem, HttpServletResponse response, HttpServletRequest request) {
		try {
			String persistenceSessionid = new CookieUtil(request).getValue("persistenceSessionId");
			
			if (persistenceSessionid == null) {
				persistenceSessionid=generatePersistenceSessionId();
				response.addCookie(CookieUtil.createCookie("persistenceSessionId", persistenceSessionid, "/", 60*60*24*365));
			} // if
			
			String collectionName = String.format("bbsItem_%s_fixed_%s", bbsName, persistenceSessionid);
			DBCollection coll = MongoDbAdapter.getInstance().getCollection(collectionName);

			WriteResult result=coll.insert(bbsItem.toDBObject());
			Object okObj=result.getLastError().get("ok");
			return okObj!=null && okObj.equals(1);
		} catch (UnsupportedEncodingException e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
			e.printStackTrace();
			return false;
		} // catch
	} // putFixedItems

	public List<BbsItem> getFixedItems(BbsNames bbsName, HttpServletRequest request) {
		try {
			String persistenceSessionid = new CookieUtil(request).getValue("persistenceSessionId");
			if (persistenceSessionid == null)
				return null;

			String collectionName = String.format("bbsItem_%s_fixed_%s", bbsName, persistenceSessionid);
			DBCollection coll = MongoDbAdapter.getInstance().getCollection(collectionName);

			if (coll.count() == 0)
				return null;

			List<BbsItem> retList = new ArrayList<BbsItem>();
			for (DBObject bbsItemObj : coll.find())
				retList.add(new BbsItem((BasicDBObject) bbsItemObj));
			return retList;
		} catch (UnsupportedEncodingException e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName()), e);
			e.printStackTrace();
			return null;
		} // catch
	} // getFixedItems

	public boolean removeFixedItem(BbsNames bbsName, long bbsItemNum, HttpServletRequest request) {
		try {
			String persistenceSessionid = new CookieUtil(request).getValue("persistenceSessionId");
			if (persistenceSessionid == null)
				return false;

			String collectionName = String.format("bbsItem_%s_fixed_%s", bbsName, persistenceSessionid);
			DBCollection coll = MongoDbAdapter.getInstance().getCollection(collectionName);

			if (coll.count() == 0)
				return false;

			WriteResult result=coll.remove(new BasicDBObject("num", bbsItemNum));
			Object okObj=result.getLastError().get("ok");
			return okObj!=null && okObj.equals(1);
		} catch (UnsupportedEncodingException e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
			e.printStackTrace();
			return false;
		} // catch
	} // removeFixedItem

	public List<BbsItem> selectArticles(BbsNames bbsName, int limit) {
		return selectArticles(bbsName, -1, limit);
	} // selectArticles

	public List<BbsItem> selectArticles(BbsNames bbsName, long startNum, int limit) {
		String collectionName = String.format("bbsItem_%s", bbsName);
		DBCollection coll = MongoDbAdapter.getInstance().getCollection(collectionName);

		if (coll.count() < limit)
			limit = (int) coll.count();

		List<BbsItem> retList = new ArrayList<BbsItem>();
		String whereQuery = String.format("{$query : { num : { $gt : %s} }, $orderby : {num : 1} }", startNum);
		for (DBObject bbsItemObj : coll.find((DBObject) JSON.parse(whereQuery)).limit(limit))
			retList.add(new BbsItem((BasicDBObject) bbsItemObj));
		return retList;
	} // selectArticle
} // class