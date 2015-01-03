package org.jaeyo.clien_stream.service;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.jaeyo.clien_stream.common.CookieUtil;
import org.jaeyo.clien_stream.common.ReflectionUtil;
import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.ArticleItem;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.jaeyo.clien_stream.parser.BbsParser;
import org.jaeyo.clien_stream.parser.BbsParserPark;
import org.jaeyo.clien_stream.repo.mongodb.MongoDbAdapter;
import org.jaeyo.clien_stream.schedule.ParseTask;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import com.mongodb.DBCollection;

public class HomeServiceTest {

	private void init(){
		Conf.set(ConfKey.MONGODB_IP, "192.168.0.10");
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
			BbsParser parser=new BbsParserPark();
			ArrayList<BbsItem> items=parser.parseBbs(BbsNames.PARK, 1);
			
			ParseTask parseTask=new ParseTask(parser, BbsNames.PARK);
			ReflectionUtil.invokePrivateMethod(parseTask, ParseTask.class, "insertBbsItems", void.class, items);
			assertTrue(coll.count()==items.size());
			
			HomeService homeService=new HomeService();
			List<BbsItem> result=homeService.selectArticles(BbsNames.PARK, 3);
			assertTrue(result.size()==3);
		} finally{
			coll.drop();
		} //finally
	} //test_selectArticle1
} //class