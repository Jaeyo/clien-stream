package org.jaeyo.clien_stream.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.ArticleItem;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.junit.Test;

public class BbsParserHongboTest {

	@Test
	public void test_hongbo() {
		BbsParser parser=new BbsParserHongbo();
		ArrayList<BbsItem> items=parser.parseBbs(BbsNames.HONGBO, 1);
		assertNotNull(items);
		assertTrue(items.size()!=0);
		
//		for(BbsItem item : items)
//			assertNotNull(parser.parseArticle(BbsNames.HONGBO, item.getNum()));
	} //test_hongbo
} //class