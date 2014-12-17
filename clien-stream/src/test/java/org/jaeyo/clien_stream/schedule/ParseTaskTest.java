package org.jaeyo.clien_stream.schedule;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.jaeyo.clien_stream.common.ReflectionUtil;
import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.ArticleItem;
import org.jaeyo.clien_stream.entity.ArticleReplyItem;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.jaeyo.clien_stream.parser.BbsParserPark;
import org.jaeyo.clien_stream.repo.mongodb.MongoDbAdapter;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class ParseTaskTest {

	private void init(){
		Conf.set(ConfKey.MONGODB_IP, "180.231.38.153");
		Conf.set(ConfKey.MONGODB_PORT, "27017");
		Conf.set(ConfKey.MONGODB_DBNAME, "test_db");	
	} //init
	
	@Test
	public void test_insertBbsItems() {
		init();
		DBCollection coll=MongoDbAdapter.getInstance().getCollection("bbsItem_" + BbsNames.PARK.toString());
		coll.drop();

		try{
			ParseTask parseTask=new ParseTask(new BbsParserPark(), BbsNames.PARK);

			ArrayList<BbsItem> items=new ArrayList<BbsItem>();
			for (int i = 0; i < 10; i++) 
				items.add(new BbsItem(i, "title", "nick", "imgNickPath", System.currentTimeMillis(), 1, BbsNames.PARK.toString().toLowerCase()));

			ReflectionUtil.invokePrivateMethod(parseTask, ParseTask.class, "insertBbsItems", void.class, items);

			assertTrue(coll.find().size()==10);

			items.add(new BbsItem(10, "title", "nick", "imgNickPath", System.currentTimeMillis(), 1, BbsNames.PARK.toString().toLowerCase()));
			
			ReflectionUtil.invokePrivateMethod(parseTask, ParseTask.class, "insertBbsItems", void.class, items);
			assertTrue(coll.find().size()==11);
		} finally{
			coll.drop();
		} //finally
	} //test_insertBbsItems
	
	@Test
	public void test_findUnstoredArticle(){
		init();
		DBCollection coll=MongoDbAdapter.getInstance().getCollection("bbsItem_" + BbsNames.PARK.toString());
		coll.drop();
	
		try{
			ParseTask parseTask=new ParseTask(new BbsParserPark(), BbsNames.PARK);
			
			ArrayList<BbsItem> items=new ArrayList<BbsItem>();
			for (int i = 0; i < 10; i++) 
				items.add(new BbsItem(i, "title", "nick", "imgNickPath", System.currentTimeMillis(), 1, BbsNames.PARK.toString().toLowerCase()));

			ReflectionUtil.invokePrivateMethod(parseTask, ParseTask.class, "insertBbsItems", void.class, items);
			
			assertTrue(coll.find().size()==10);
			
			List result=ReflectionUtil.invokePrivateMethod(parseTask, ParseTask.class, "findUnstoredArticle", List.class, null, null);
			assertTrue(result.size()==10);
		} finally{
			coll.drop();
		} //finally
	} //test_findUnstoredArticle
	
	@Test
	public void test_updateArticle(){
		init();
		DBCollection coll=MongoDbAdapter.getInstance().getCollection("bbsItem_" + BbsNames.PARK.toString());
		coll.drop();
		
		try{
			ParseTask parseTask=new ParseTask(new BbsParserPark(), BbsNames.PARK);
			
			ArrayList<BbsItem> items=new ArrayList<BbsItem>();
			for (int i = 0; i < 10; i++) 
				items.add(new BbsItem(i, "title", "nick", "imgNickPath", System.currentTimeMillis(), 1, BbsNames.PARK.toString().toLowerCase()));

			ReflectionUtil.invokePrivateMethod(parseTask, ParseTask.class, "insertBbsItems", void.class, items);
	
			List result=ReflectionUtil.invokePrivateMethod(parseTask, ParseTask.class, "findUnstoredArticle", List.class, null, null);
			assertTrue(result.size()==10);
			
			ArticleItem article=new ArticleItem("articleHtml", new ArrayList<ArticleReplyItem>());
			ReflectionUtil.invokePrivateMethod(parseTask, ParseTask.class, "updateArticle", boolean.class, new Class<?>[]{long.class, ArticleItem.class}, new Object[]{(long)0, article});
			
			result=ReflectionUtil.invokePrivateMethod(parseTask, ParseTask.class, "findUnstoredArticle", List.class, null, null);
			assertTrue(result.size()==9);
			
			DBObject whereQueryObj=(DBObject)JSON.parse("{num : {$eq : 0} }");
			BasicDBObject resultObj=(BasicDBObject) coll.find(whereQueryObj).next();
			BasicDBObject articleObj=(BasicDBObject) resultObj.get("article");
			assertNotNull(articleObj);
			assertTrue(articleObj.getString("articleHtml").equals("articleHtml"));
		} finally{
			coll.drop();
		} //finally
	} //test_updateArticle
} //class