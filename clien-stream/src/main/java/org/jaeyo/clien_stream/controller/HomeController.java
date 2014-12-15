package org.jaeyo.clien_stream.controller;

import java.util.Random;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jaeyo.clien_stream.service.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sun.istack.internal.Nullable;

@Controller
public class HomeController {
	private static final Logger logger=LoggerFactory.getLogger(HomeController.class);
	
	@Inject
	private HomeService homeService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView root(HttpServletRequest request) {
		return home(request, null);
	} // test
	
	@RequestMapping(value="/home", method=RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request, @RequestParam(required=false, value="nick") @Nullable String nickname){
		if(nickname==null)
			nickname="guest" + new Random().nextInt();
		return new ModelAndView("test").addObject("nick", nickname);
	} //home
} //class