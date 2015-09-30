package com.oct.ga.admin.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.SupSocketException;
import com.redoct.ga.sup.account.SupAccountService;

@Controller
public class LoginViewController
{
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request)
			throws SupSocketException
	{
		String loginname = request.getParameter("inputEmail");
		System.out.println("loginname:" + loginname);
		String password = request.getParameter("inputPassword");
		// System.out.println(password);

		if (loginname != null && password != null) {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			SupAccountService accountService = (SupAccountService) ctx.getBean("supAccountService");

			String accountId = accountService.verifyLogin(GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, loginname,
					EcryptUtil.md5(password));

			if (accountId != null && accountId.length() > 0) {
				HttpSession session = request.getSession();
				UserSession user = new UserSession();
				user.setId("id");
				user.setUsername(loginname);
				session.setAttribute("user", user);

				return new ModelAndView("redirect:/loginSuccess");
			} else {
				return new ModelAndView("login", "message", "Wrong username or pasword!");
			}
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/loginSuccess", method = RequestMethod.GET)
	public String success()
	{
		return "admin-summary";
	}

	@RequestMapping(value = "/loginFailure", method = RequestMethod.GET)
	public String failure()
	{
		return "login";
	}

	ApplicationContext applicationContext = null;
}
