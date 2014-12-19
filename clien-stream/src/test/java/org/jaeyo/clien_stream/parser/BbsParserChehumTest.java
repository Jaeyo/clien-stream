package org.jaeyo.clien_stream.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.junit.Test;

public class BbsParserChehumTest {

	@Test
	public void test_chehum() {
		BbsParser parser=new BbsParserChehum();
		ArrayList<BbsItem> items=parser.parseBbs(BbsNames.CHEHUM, 1);
		assertNotNull(items);
		assertTrue(items.size()!=0);
		
//		for(BbsItem item : items)
//			assertNotNull(parser.parseArticle(BbsNames.CHEHUM, item.getNum()));
	} //test_chehum
} //class