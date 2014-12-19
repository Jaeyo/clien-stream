package org.jaeyo.clien_stream.controller;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.jaeyo.clien_stream.common.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProxyController {
	private static final Logger logger = LoggerFactory.getLogger(ProxyController.class);

	@RequestMapping(value = "/proxy/{bbsName}/{num}", method = RequestMethod.GET)
	public @ResponseBody String home(HttpServletRequest request, 
			@PathVariable(value = "bbsName") String bbsName, 
			@PathVariable(value="num")String num) {
		String url=String.format("http://www.clien.net/cs2/bbs/board.php?bo_table=%s&wr_id=%s", bbsName, num);
		try {
			return StreamUtil.readAllLine(new URL(url).openStream());
		} catch (IOException e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
			return null;
		} //catch
	} // home
} // class