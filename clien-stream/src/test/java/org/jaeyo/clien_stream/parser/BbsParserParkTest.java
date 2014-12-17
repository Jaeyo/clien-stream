package org.jaeyo.clien_stream.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.jaeyo.clien_stream.repo.mongodb.MongoDbAdapter;
import org.junit.Test;

import com.mongodb.DBCollection;

public class BbsParserParkTest {

	@Test
	public void test() {
		BbsParser parser=new BbsParserPark();
		ArrayList<BbsItem> items=parser.parseBbs(BbsNames.PARK, 1);
		assertNotNull(items);
		assertTrue(items.size()!=0);
		
		for(BbsItem item : items)
			assertNotNull(parser.parseArticle(BbsNames.PARK, item.getNum()));
	} //test
} //class