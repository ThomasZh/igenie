package com.oct.ga.comm.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;
import com.oct.ga.comm.domain.template.AttachmentJsonBean;
import com.oct.ga.comm.domain.template.ChecknameJsonBean;
import com.oct.ga.comm.domain.template.ClwcJsonBean;
import com.oct.ga.comm.domain.template.TemplateDefineJsonBean;

public class JsonParser
{
	public static String[] json2StringArray(String jsonStr)
	{
		JSONArray jsonArray = JSONArray.fromObject(jsonStr);
		String[] array = (String[]) jsonArray.toArray(new String[jsonArray.size()]);

		return array;
	}

	public static TemplateDefineJsonBean json2TemplateDefine(String jsonStr, int extAttrType)
	{
		Map<String, Class<?>> m = new HashMap<String, Class<?>>();
		m.put("templateId", String.class);
		m.put("templateName", String.class);
		m.put("templateType", String.class);
		m.put("templatePid", String.class);
		m.put("templateDesc", String.class);
		m.put("version", String.class);
		m.put("startTime", Integer.class);
		m.put("endTime", Integer.class);
		m.put("createTime", Integer.class);
		m.put("lastUpdateTime", Integer.class);
		m.put("copys", Integer.class);
		m.put("extAttrType", String.class);
		switch (extAttrType) {
		case GlobalArgs.TEMPLATE_EXTATTR_TYPE_CHECK_LIST:
			m.put("extAttr", ChecknameJsonBean.class);
			break;
		case GlobalArgs.TEMPLATE_EXTATTR_TYPE_CLWC_LIST:
			m.put("extAttr", ClwcJsonBean.class);
			break;
		case GlobalArgs.TEMPLATE_EXTATTR_TYPE_NORMAL_TASK:
		default:
			break;
		}
		m.put("children", TemplateDefineJsonBean.class);
		m.put("attachments", AttachmentJsonBean.class);
		m.put("permissionMode", Integer.class);
		m.put("splitForEachMember", Boolean.class);
		m.put("feedbackInvite", Boolean.class);
		m.put("feedbackCompleted", Boolean.class);

		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		TemplateDefineJsonBean templateDefine = (TemplateDefineJsonBean) JSONObject.toBean(jsonObject,
				TemplateDefineJsonBean.class, m);

		return templateDefine;
	}

	public static String templateDefine2Json(TemplateDefineJsonBean templateDefine)
	{
		JSONObject jsonObject = JSONObject.fromObject(templateDefine);
		return jsonObject.toString();
	}

	public static String serverList2Json(List<StpServerInfoJsonBean> serverList)
	{
		JSONArray jsonArray = JSONArray.fromObject(serverList);
		return jsonArray.toString();
	}

	public static List<StpServerInfoJsonBean> json2ServerList(String jsonArrayStr)
	{
		JSONArray jsonArray = JSONArray.fromObject(jsonArrayStr);
		List<StpServerInfoJsonBean> serverList = (List<StpServerInfoJsonBean>) JSONArray.toCollection(jsonArray,
				StpServerInfoJsonBean.class);

		return serverList;
	}

	public static List<ClwcJsonBean> json2Clwclist(String jsonArrayStr)
	{
		JSONArray jsonArray = JSONArray.fromObject(jsonArrayStr);
		List<ClwcJsonBean> clwclist = (List<ClwcJsonBean>) JSONArray.toCollection(jsonArray, ClwcJsonBean.class);

		return clwclist;
	}

	public static List<ChecknameJsonBean> json2Checklist(String jsonArrayStr)
	{
		JSONArray jsonArray = JSONArray.fromObject(jsonArrayStr);
		List<ChecknameJsonBean> checklist = (List<ChecknameJsonBean>) JSONArray.toCollection(jsonArray,
				ChecknameJsonBean.class);

		return checklist;
	}

	public static String template2Json(TemplateDefineJsonBean template)
	{
		JSONObject jsonObject = JSONObject.fromObject(template);
		return jsonObject.toString();
	}

	private static String templateBeanToJsonStr(TemplateDefineJsonBean template, String jsonArrayStr)
	{
		JSONArray jsonArray = JSONArray.fromObject(jsonArrayStr);
		List<ChecknameJsonBean> checklist = (List<ChecknameJsonBean>) JSONArray.toCollection(jsonArray,
				ChecknameJsonBean.class);

		template.setExtAttr(checklist);

		JSONObject jsonObject = JSONObject.fromObject(template);
		return jsonObject.toString();
	}

	public static void main(String[] argv)
	{
		TemplateDefineJsonBean templateDefine = new TemplateDefineJsonBean();
		templateDefine.setTemplateId("25316887-bace-42b2-9816-442ab97fb7a8");
		templateDefine.setTemplateName("demo");

		TemplateDefineJsonBean templateDefineChild1 = new TemplateDefineJsonBean();
		templateDefineChild1.setTemplateId("a2b110da-b7f1-43d3-8901-6002390f9e91");
		templateDefineChild1.setTemplateName("check list");

		TemplateDefineJsonBean templateDefineChild2 = new TemplateDefineJsonBean();
		templateDefineChild2.setTemplateId("5c78e550-a6a6-4847-8d64-3b13fcd823f9");
		templateDefineChild2.setTemplateName("clwc");

		AttachmentJsonBean attachment1 = new AttachmentJsonBean();
		attachment1.setName("trip day1");
		attachment1.setFilename("aaa.jpg");

		AttachmentJsonBean attachment2 = new AttachmentJsonBean();
		attachment2.setName("trip day2");
		attachment2.setFilename("bbb.jpg");

		List attachments = new ArrayList();
		attachments.add(attachment1);
		attachments.add(attachment2);
		templateDefine.setAttachments(attachments);

		List children = new ArrayList();
		children.add(templateDefineChild1);
		children.add(templateDefineChild2);
		templateDefine.setChildren(children);

		String jsonStr4 = templateDefine2Json(templateDefine);
		System.out.println("json: " + jsonStr4);

		TemplateDefineJsonBean templateDefine2 = JsonParser.json2TemplateDefine(jsonStr4,
				GlobalArgs.TEMPLATE_EXTATTR_TYPE_NORMAL_TASK);
		System.out.println(templateDefine2.getTemplateId());
		System.out.println(templateDefine2.getTemplateName());

		for (TemplateDefineJsonBean childTemplateDefine : templateDefine2.getChildren()) {
			System.out.println(childTemplateDefine.getTemplateId());
			System.out.println(childTemplateDefine.getTemplateName());
		}

		for (AttachmentJsonBean attachment : templateDefine2.getAttachments()) {
			System.out.println(attachment.getName());
			System.out.println(attachment.getFilename());
		}

		String jsonArrayStr = "[{'checkname':'123','displayStepper':'true'},{'checkname':'456','displayStepper':'false'}]";

		List<ClwcJsonBean> clwclist = JsonParser.json2Clwclist(jsonArrayStr);

		TemplateDefineJsonBean template = new TemplateDefineJsonBean();
		template.setTemplateId("templateId");
		template.setTemplateName("templateName");
		template.setExtAttrType((short) 312);

		String jsonArrayStr2 = "[{'serverIp':'182.92.71.66','port':13103},{'serverIp':'182.92.71.66','port':13105}]";
		List<StpServerInfoJsonBean> serverList = json2ServerList(jsonArrayStr2);

		for (StpServerInfoJsonBean server : serverList) {
			System.out.println(server.getServerIp());
			System.out.println(server.getPort());
			System.out.println(server.isActive());
		}

		String jsonStr2 = serverList2Json(serverList);
		System.out.println("json: " + jsonStr2);
	}
}
