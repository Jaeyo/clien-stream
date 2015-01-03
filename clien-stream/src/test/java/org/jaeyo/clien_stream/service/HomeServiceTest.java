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
	public void test_fixedItems(){
		init();
		HomeService service=new HomeService();
		long bbsItemNum=1234;
		BbsItem item=new BbsItem(bbsItemNum, "test_title", "test_nick", null, System.currentTimeMillis(), 0, BbsNames.PARK.toString().toLowerCase());
		
		HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response=Mockito.mock(HttpServletResponse.class);
		
		try {
			String persistenceSessionid=service.generatePersistenceSessionId();
			Cookie cookie=CookieUtil.createCookie("persistenceSessionId", persistenceSessionid, "/", 60*60*24*365);
			response.addCookie(cookie);
			BDDMockito.given(request.getCookies()).willReturn(new Cookie[]{cookie});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail();
		} //catch
				
		assertTrue(service.putFixedItems(BbsNames.PARK, item, response, request));
		
		List<BbsItem> fixedItems=service.getFixedItems(BbsNames.PARK, request);
		assertTrue(fixedItems!=null && fixedItems.size()!=0);
		
		assertTrue(service.removeFixedItem(BbsNames.PARK, bbsItemNum, request));
		
		fixedItems=service.getFixedItems(BbsNames.PARK, request);
		assertNull(fixedItems);
	} //test_fixedItems
	
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