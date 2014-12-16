package org.jaeyo.clien_stream.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.jaeyo.clien_stream.entity.BbsItem;
import org.junit.Test;

public class BbsParserImplTest {

	@Test
	public void test() {
		BbsParser parser=new BbsParserImpl();
		ArrayList<BbsItem> result=parser.parseBbs("park", 1);
		assertNotNull(result);
		assertTrue(result.size()!=0);
	} //test
} //class