package com.redoct.ga.web.talent.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.redoct.ga.web.wechat.WechatUserSession;

@Controller
public class MyFriendsViewController
{
	@RequestMapping(value = "/my-friends", method = RequestMethod.GET)
	public ModelAndView getPages(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		WechatUserSession user = (WechatUserSession) session.getAttribute("user");

		ModelAndView model = new ModelAndView("my-friends");
		model.addObject("unionid", user.getUnionid());
		model.addObject("nickname", user.getNickname());
		model.addObject("avatarUrl", user.getHeadimgurl());
		return model;
	}
}
