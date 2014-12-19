package org.jaeyo.clien_stream.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.service.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	private static final Logger logger=LoggerFactory.getLogger(HomeController.class);
	
	@Inject
	private HomeService homeService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView root(HttpServletRequest request) {
		return home(request);
	} // test
	
	@RequestMapping(value="/home", method=RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request){
		return home(request, BbsNames.PARK.toString().toLowerCase());
	} //home
	
	@RequestMapping(value="/home/{bbsName}", method=RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request, @PathVariable(value="bbsName") String bbsName){
		ModelAndView mv=new ModelAndView("home");
		mv.addObject("wsPort", Conf.get(ConfKey.WEB_SOCKET_PORT));
		mv.addObject("bbsName", bbsName);
		return mv;
	} //home
} //class