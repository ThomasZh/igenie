package com.oct.ga.admin.mvc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.club.ActivityDetailInfo;

@Controller
public class MemberViewController
{
	@RequestMapping("/member")
	public ModelAndView login(HttpServletRequest request)
	{
		String accountId = request.getParameter("account_id");
		System.out.println("accountId: " + accountId);

		ModelAndView model = new ModelAndView();
		model.setViewName("member");

		model.addObject("leaderId", "33d1dfdf-c11b-42bf-90a6-2b43e1f16f4e");
		model.addObject("leaderName", "Doris");
		model.addObject("leaderImageUrl",
				"http://tripc2c-person-face.b0.upaiyun.com/2015/03/06/b890e261f3f3377d7c5dec90baa460bf.jpg");

		ActivityDetailInfo activity = new ActivityDetailInfo();
		activity.setId("1d554c82-ac2a-4d05-ab2c-7517377fc720");
		activity.setPublishType(GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PUBLIC);
		activity.setName("�쵼����ѵ");
		activity.setDesc("�ڹ�Ͻ��Χ�ڳ�ֵ����������Ϳ͹���������С�ɱ�����£���������Ŷӵ�Ч�ʡ���Ȩ�������빵ͨ����������ѡ");
		activity.setStartTime(0);
		activity.setEndTime(0);
		activity.setLocDesc("�廪����");

		ActivityDetailInfo activity2 = new ActivityDetailInfo();
		activity2.setId("1d554c82-ac2a-4d05-ab2c-7517377fc720");
		activity2.setPublishType(GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PUBLIC);
		activity2.setName("�쵼����ѵ");
		activity2.setDesc("�ڹ�Ͻ��Χ�ڳ�ֵ����������Ϳ͹���������С�ɱ�����£���������Ŷӵ�Ч�ʡ���Ȩ�������빵ͨ����������ѡ");
		activity2.setStartTime(0);
		activity2.setEndTime(0);
		activity2.setLocDesc("�廪����");

		List<ActivityDetailInfo> array = new ArrayList<ActivityDetailInfo>();
		array.add(activity);
		array.add(activity2);
		model.addObject("activities", array);

		return model;
	}
}
