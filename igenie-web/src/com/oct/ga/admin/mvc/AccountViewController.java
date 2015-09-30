package com.oct.ga.admin.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AccountViewController
{
	@RequestMapping(value = "/admin-account", method = RequestMethod.GET)
	public String login()
	{
		System.out.println("admin-account");
		return "admin-account";
	}
}
