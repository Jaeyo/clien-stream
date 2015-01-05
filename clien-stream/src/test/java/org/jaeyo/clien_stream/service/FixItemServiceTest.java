package org.jaeyo.clien_stream.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.jaeyo.clien_stream.common.CookieUtil;
import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class FixItemServiceTest {

	private void init(){
		Conf.set(ConfKey.MONGODB_IP, "192.168.0.10");
		Conf.set(ConfKey.MONGODB_IP, "180.231.38.153");
		Conf.set(ConfKey.MONGODB_PORT, "27017");
		Conf.set(ConfKey.MONGODB_DBNAME, "test_db");	
	} //init	
	
	@Test
	public void test_fixedItems(){
		init();
		FixItemService service=new FixItemService();
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
} //class