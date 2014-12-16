package org.jaeyo.clien_stream.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.jaeyo.clien_stream.repo.mongodb.MongoDbAdapter;
import org.junit.Test;

import com.mongodb.DBCollection;

public class BbsParserImplTest {

	@Test
	public void test() {
		BbsParser parser=new BbsParserImpl();
		ArrayList<BbsItem> result=parser.parseBbs("park", 1);
		assertNotNull(result);
		assertTrue(result.size()!=0);
	} //test
} //class