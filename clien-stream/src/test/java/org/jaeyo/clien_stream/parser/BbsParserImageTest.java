package org.jaeyo.clien_stream.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.junit.Test;

public class BbsParserImageTest {

	@Test
	public void test() {
		BbsParser parser=new BbsParserImage();
		ArrayList<BbsItem> items=parser.parseBbs(BbsNames.IMAGE, 1);
		assertNotNull(items);
		assertTrue(items.size()!=0);
		
		for(BbsItem item : items)
			assertNotNull(parser.parseArticle(BbsNames.IMAGE, item.getNum()));
	} //test
} //class