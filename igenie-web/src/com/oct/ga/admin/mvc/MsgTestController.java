package com.oct.ga.admin.mvc;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.gatekeeper.SupServerInfo;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.redoct.ga.sup.client.socket.SupSocketConnectionManager;
import com.redoct.ga.sup.message.SupMessageService;

@Controller
public class MsgTestController
{
	@RequestMapping("/invite/sendMsgAction")
	public ModelAndView getPages(HttpServletRequest request)
			throws IOException
	{
		request.setCharacterEncoding("UTF-8");

		String fromAccountId = "33d1dfdf-c11b-42bf-90a6-2b43e1f16f4e";
		String fromAccountName = "张璟天";
		String fromAccountAvatarUrl = "http://tripc2c-person-face.b0.upaiyun.com/2015/03/06/b890e261f3f3377d7c5dec90baa460bf.jpg";
		String leaderId = "33d1dfdf-c11b-42bf-90a6-2b43e1f16f4e";
		String channelId = "199fe38c-8e38-414d-9306-42ac49b8d33d";
		String groupName = "活动测试";

		int currentTimestamp = DatetimeUtil.currentTimestamp();

		ApplicationContext context = ApplicationContextProvider.getApplicationContext();
		SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");

		// send this apply message to online leader
		try {
			SupServerInfo server = new SupServerInfo();
			server.setId("68df84ba-9ec0-4df5-b62d-dc57d997529f");
			server.setIp("siriusa");
			server.setPort(15106);
			server.setType(GlobalArgs.SUP_TYPE_MESSAGE_SERVER);
			server.setActive(true);
			
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			socketConnectionManager.add(server);

			MsgFlowBasicInfo msgFlowBasicInfo = new MsgFlowBasicInfo();
			msgFlowBasicInfo.setLogId(UUID.randomUUID().toString());
			msgFlowBasicInfo.setFromAccountId(fromAccountId);
			msgFlowBasicInfo.setFromAccountName(fromAccountName);
			msgFlowBasicInfo.setFromAccountAvatarUrl(fromAccountAvatarUrl);
			msgFlowBasicInfo.setToActionAccountId(leaderId);
			msgFlowBasicInfo.setToActionId(channelId);
			msgFlowBasicInfo.setActionTag(GlobalArgs.TASK_ACTION_JOIN);
			msgFlowBasicInfo.setChannelId(channelId);
			msgFlowBasicInfo.setChannelName(groupName);

			supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
		} catch (Exception e) {
			logger.error(LogErrorMessage.getFullInfo(e));
		}

		ModelAndView model = new ModelAndView();
		model.setViewName("/invite/sendMsgSuccess");

		return model;
	}

	private final static Logger logger = LoggerFactory.getLogger(MsgTestController.class);
}
