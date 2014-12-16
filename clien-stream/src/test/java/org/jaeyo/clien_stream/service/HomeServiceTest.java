package org.jaeyo.clien_stream.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.jaeyo.clien_stream.common.ReflectionUtil;
import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.ArticleItem;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.jaeyo.clien_stream.parser.BbsParser;
import org.jaeyo.clien_stream.parser.BbsParserImpl;
import org.jaeyo.clien_stream.repo.mongodb.MongoDbAdapter;
import org.jaeyo.clien_stream.schedule.ParseTask;
import org.junit.Test;

import com.mongodb.DBCollection;

public class HomeServiceTest {

	private void init(){
		Conf.set(ConfKey.MONGODB_IP, "180.231.38.153");
		Conf.set(ConfKey.MONGODB_PORT, "27017");
		Conf.set(ConfKey.MONGODB_DBNAME, "test_db");	
	} //init	
	
	@Test
	public void test_selectArticle1() {
		init();
		DBCollection coll=MongoDbAdapter.getInstance().getCollection(String.format("bbsItem_%s", BbsNames.PARK));

		coll.drop();

		try{
			BbsParser parser=new BbsParserImpl();
			ArrayList<BbsItem> items=parser.parseBbs(BbsNames.PARK.toString().toLowerCase(), 1);
			
			ParseTask parseTask=new ParseTask(parser, BbsNames.PARK);
			ReflectionUtil.invokePrivateMethod(parseTask, ParseTask.class, "insertBbsItems", void.class, items);
			assertTrue(coll.count()==items.size());
			
			List<Long> unstoredArticleNums=ReflectionUtil.invokePrivateMethod(parseTask, ParseTask.class, "findUnstoredArticle", List.class, null, null);
			assertTrue(unstoredArticleNums.size()==items.size());
			
			for(Long num : unstoredArticleNums){
				boolean result=ReflectionUtil.invokePrivateMethod(parseTask, ParseTask.class, "updateArticle", boolean.class, new Class<?>[]{long.class, ArticleItem.class}, new Object[]{num, new ArticleItem("articleHtml", new ArrayList())});
				assertTrue(result);
			} //for num
			
			unstoredArticleNums=ReflectionUtil.invokePrivateMethod(parseTask, ParseTask.class, "findUnstoredArticle", List.class, null, null);
			assertTrue(unstoredArticleNums.size()==0);
			
			HomeService homeService=new HomeService();
			List<BbsItem> result=homeService.selectArticles(BbsNames.PARK, 3);
			assertTrue(result.size()==3);
			
			for (BbsItem item : result){
				System.out.println(item.getTitle());
			} //for item
		} finally{
			coll.drop();
		} //finally
	} //test_selectArticle1
} //class