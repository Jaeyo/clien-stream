package org.jaeyo.clien_stream.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.jaeyo.clien_stream.repo.mongodb.MongoDbAdapter;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.mongodb.DBCollection;

public class BbsParserParkTest {

	@Test
	public void test_park() {
		BbsParser parser=new BbsParserPark();
		ArrayList<BbsItem> items=parser.parseBbs(BbsNames.PARK, 1);
		assertNotNull(items);
		assertTrue(items.size()!=0);
		
//		for(BbsItem item : items)
//			assertNotNull(parser.parseArticle(BbsNames.PARK, item.getNum()));
	} //test_park
	
	@Test
	public void test_news() {
		BbsParser parser=new BbsParserPark();
		ArrayList<BbsItem> items=parser.parseBbs(BbsNames.PARK, 1);
		assertNotNull(items);
		assertTrue(items.size()!=0);
		
//		for(BbsItem item : items)
//			assertNotNull(parser.parseArticle(BbsNames.PARK, item.getNum()));
	} //test_news
	
	@Test
	public void test_useful() {
		BbsParser parser=new BbsParserPark();
		ArrayList<BbsItem> items=parser.parseBbs(BbsNames.USEFUL, 1);
		assertNotNull(items);
		assertTrue(items.size()!=0);
		
//		for(BbsItem item : items)
//			assertNotNull(parser.parseArticle(BbsNames.USEFUL, item.getNum()));
	} //test_useful
} //class