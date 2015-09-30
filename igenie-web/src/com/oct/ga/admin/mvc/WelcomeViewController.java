package com.oct.ga.admin.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WelcomeViewController
{
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView getPages()
	{
		ModelAndView model = new ModelAndView("login");
		return model;
	}

}
