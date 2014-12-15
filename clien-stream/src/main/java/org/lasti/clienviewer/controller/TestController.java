package org.lasti.clienviewer.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.lasti.clienviewer.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Inject
	private TestService testService;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ModelAndView test(HttpServletRequest request) {
		return new ModelAndView("test").addObject("msg", testService.getTestValue());
	} // test
} // class