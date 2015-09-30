package com.oct.ga.admin.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.SupSocketException;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.domain.LostPwdEkey;

@Controller
public class ResetPasswordActionController
{
	@RequestMapping("/resetpwdAction")
	public ModelAndView getPages(HttpServletRequest request, HttpServletResponse response)
			throws IOException, InterruptedException, SupSocketException
	{
		request.setCharacterEncoding("UTF-8");// ���ÿͻ�������������������"UTF-8"�ַ����
		response.setCharacterEncoding("UTF-8");// ���ý��ַ���"UTF-8"����������ͻ��������

		String ekey = request.getParameter("hidden_ekey");
		logger.debug("ekey: " + ekey);
		String pwd = request.getParameter("inputPassword");
		//logger.debug("pwd: " + pwd);
		String pwd2 = request.getParameter("inputPassword2");
		//logger.debug("pwd2: " + pwd2);

		ModelAndView model = new ModelAndView();
		if (!pwd.equals(pwd2)) {
			model.setViewName("/resetpwd");
			model.addObject("message", "Two types not same!");
			model.addObject("ekey", ekey);
			return model;
		} else {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			SupAccountService accountService = (SupAccountService) ctx.getBean("supAccountService");

			int currentTimestamp = DatetimeUtil.currentTimestamp();
			LostPwdEkey ekeyInfo = accountService.queryEkey(ekey);
			if (ekeyInfo.getTtl() < currentTimestamp) {
				logger.warn("ErrorCode=[" + ErrorCode.RESET_PWD_EKEY_EXPIRY_TIME + "]|ekey=[" + ekey + "] expiry time");

				model.setViewName("/resetpwd");
				model.addObject("message", "This ekey expiry time!");
				model.addObject("ekey", ekey);
				return model;
			} else {
				String md5pwd = EcryptUtil.md5(pwd);
					accountService.resetPwd(ekeyInfo.getLoginType(), ekeyInfo.getLoginName(), md5pwd, currentTimestamp);
			}
			return new ModelAndView("/resetpwdSuccess");
		}
	}

	private final static Logger logger = LoggerFactory.getLogger(ResetPasswordActionController.class);
}
