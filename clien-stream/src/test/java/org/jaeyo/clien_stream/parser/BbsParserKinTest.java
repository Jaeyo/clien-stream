package org.jaeyo.clien_stream.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.junit.Test;

public class BbsParserKinTest {

	@Test
	public void test_kin() {
		BbsParser parser=new BbsParserKin();
		ArrayList<BbsItem> items=parser.parseBbs(BbsNames.KIN, 1);
		assertNotNull(items);
		assertTrue(items.size()!=0);
		
		for(BbsItem item : items)
			assertNotNull(parser.parseArticle(BbsNames.KIN, item.getNum()));
	} //test_kin
	
	@Test
	public void test_lecture() {
		BbsParser parser=new BbsParserKin();
		ArrayList<BbsItem> items=parser.parseBbs(BbsNames.LECTURE, 1);
		assertNotNull(items);
		assertTrue(items.size()!=0);
		
		for(BbsItem item : items)
			assertNotNull(parser.parseArticle(BbsNames.LECTURE, item.getNum()));
	} //test_lecture
	
	@Test
	public void test_use() {
		BbsParser parser=new BbsParserKin();
		ArrayList<BbsItem> items=parser.parseBbs(BbsNames.USE, 1);
		assertNotNull(items);
		assertTrue(items.size()!=0);
		
		for(BbsItem item : items)
			assertNotNull(parser.parseArticle(BbsNames.USE, item.getNum()));
	} //test_use
} //class