package com.redoct.ga.web.talent.mvc.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EditProfileViewController
{
	@RequestMapping(value = "/edit-profile", method = RequestMethod.GET)
	public ModelAndView getPages()
	{
		ModelAndView model = new ModelAndView("edit-profile");
		return model;
	}
}
