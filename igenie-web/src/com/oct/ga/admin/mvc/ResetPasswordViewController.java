package com.oct.ga.admin.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResetPasswordViewController
{
	@RequestMapping("/resetpwd")
	public ModelAndView getPages(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		request.setCharacterEncoding("UTF-8");// ���ÿͻ�������������������"UTF-8"�ַ����
		response.setCharacterEncoding("UTF-8");// ���ý��ַ���"UTF-8"����������ͻ��������

		String ekey = request.getParameter("ekey");
		logger.debug("ekey: " + ekey);

		ModelAndView model = new ModelAndView();
		model.setViewName("/resetpwd");
		model.addObject("ekey", ekey);

		return model;
	}
	
	private final static Logger logger = LoggerFactory.getLogger(ResetPasswordViewController.class);
}
