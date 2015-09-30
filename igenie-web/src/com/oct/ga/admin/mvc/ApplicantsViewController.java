package com.oct.ga.admin.mvc;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.domain.apply.GaApplicantCell;
import com.oct.ga.comm.domain.apply.GaApplicantDetailInfo;
import com.oct.ga.comm.domain.apply.GaApplicantTemplate;
import com.oct.ga.comm.domain.apply.GaApplicantTemplateCell;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaApplyService;
import com.oct.ga.service.GaGroupService;

@Controller
public class ApplicantsViewController
{
	@RequestMapping("/invite/applicants")
	public ModelAndView getPages(HttpServletRequest request) 
			throws UnsupportedEncodingException
	{
		request.setCharacterEncoding("UTF-8");//设置客户端浏览器输出到服务器"UTF-8"字符编码
		
		String activityId = request.getParameter("id");
		logger.debug("activityId: " + activityId);

		ModelAndView model = new ModelAndView();
		model.setViewName("/invite/applicants");

		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		GaActivityService activityService = (GaActivityService) ctx.getBean("clubActivityService");
		GaApplyService applyService = (GaApplyService) ctx.getBean("gaApplyService");
		GaGroupService groupService = (GaGroupService) ctx.getBean("gaGroupService");

		GaApplicantTemplate template = applyService.queryApplicantTemplate(activityId);
		String contactJson = template.getContactJson();
		String participationJson = template.getParticipationJson();
		List<GaApplicantTemplateCell> templateContactCells = null;
		List<GaApplicantTemplateCell> templateParticipationCells = null;

		Gson gson = new Gson();
		if (contactJson != null && contactJson.length() > 0) {
			templateContactCells = gson.fromJson(contactJson, new TypeToken<List<GaApplicantTemplateCell>>()
			{
			}.getType());
		}
		if (participationJson != null && participationJson.length() > 0) {
			templateParticipationCells = gson.fromJson(participationJson,
					new TypeToken<List<GaApplicantTemplateCell>>()
					{
					}.getType());
		}

		List<String> columnNames = new ArrayList<String>();
		for (GaApplicantTemplateCell templateParticipationCell : templateParticipationCells) {
			String name = templateParticipationCell.getName();
			columnNames.add(name);
		}
		for (GaApplicantTemplateCell templateContactCell : templateContactCells) {
			String name = templateContactCell.getName();
			columnNames.add(name);
		}
		model.addObject("columnNames", columnNames);

		String activityName = groupService.queryGroupName(activityId);
		model.addObject("activityName", activityName);

		List<List<String>> datas = new ArrayList<List<String>>();

		List<GaApplicantDetailInfo> applicants = applyService.query(activityId);
		for (GaApplicantDetailInfo applicant : applicants) {
			List<String> row = new ArrayList<String>();
			List<GaApplicantCell> applicantCells = applicant.getApplicant();
			for (GaApplicantCell applicantCell : applicantCells) {
				String column = applicantCell.getVal();
				row.add(column);
			}

			String memberId = applicant.getAccountId();
			List<GaApplicantCell> contactCells = applyService.queryApplicantContact(activityId, memberId);
			for (GaApplicantCell contactCell : contactCells) {
				String column = contactCell.getVal();
				row.add(column);
			}

			datas.add(row);
		}
		model.addObject("datas", datas);

		return model;
	}

	private final static Logger logger = LoggerFactory.getLogger(ApplicantsViewController.class);
}
