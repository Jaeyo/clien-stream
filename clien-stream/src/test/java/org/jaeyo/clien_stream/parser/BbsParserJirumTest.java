package org.jaeyo.clien_stream.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.ArticleItem;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.junit.Test;

public class BbsParserJirumTest {

	@Test
	public void test_jirum() {
		BbsParser parser=new BbsParserJirum();
		ArrayList<BbsItem> items=parser.parseBbs(BbsNames.JIRUM, 1);
		assertNotNull(items);
		assertTrue(items.size()!=0);
		
		for(BbsItem item : items)
			assertNotNull(parser.parseArticle(BbsNames.JIRUM, item.getNum()));
	} //test_jirum
	
	@Test
	public void test_coupon() {
		BbsParser parser=new BbsParserJirum();
		ArrayList<BbsItem> items=parser.parseBbs(BbsNames.COUPON, 1);
		assertNotNull(items);
		assertTrue(items.size()!=0);
		
		for(BbsItem item : items)
			assertNotNull(parser.parseArticle(BbsNames.COUPON, item.getNum()));
	} //test_coupon
} //class