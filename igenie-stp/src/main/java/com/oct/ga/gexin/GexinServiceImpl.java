package com.oct.ga.gexin;

import java.io.IOException;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.msg.GaInviteFeedback;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.domain.msg.NotifyTaskLog;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.service.GaOfflineNotifyService;

public class GexinServiceImpl
		implements GaOfflineNotifyService
{
	static String appId = "5uFP9qKqgV99Ms2RwiQKE5";
	static String appkey = "hoBkqwGvhu5X2QNcVqBn46";
	static String master = "DuKdNGJGd08zKs6JPBV5p3";
	// static String CID = "873ffc4fec7bfd43d4705639eacb41d0";
	static String host = "http://sdk.open.api.igexin.com/apiex.htm";

	@Override
	public void sendMessage(boolean isOnline, String notifyToken, int badgenum, MessageInlinecast msg)
	{
		IGtPush push = new IGtPush(host, appkey, master);
		try {
			push.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LinkTemplate template = linkTemplateDemo();
		SingleMessage gexinMessage = new SingleMessage();
		gexinMessage.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		gexinMessage.setOfflineExpireTime(24 * 3600 * 1000);
		gexinMessage.setData(template);
		// message.setPushNetWorkType(1);
		// //判断是否客户端是否wifi环境下推送，1为在WIFI环境下，0为不限制网络环境。
		Target target = new Target();

		target.setAppId(appId);
		target.setClientId(notifyToken);
		// 用户别名推送，cid和用户别名只能2者选其一
		// String alias = "个";
		// target.setAlias(alias);
		IPushResult ret = push.pushMessageToSingle(gexinMessage, target);
		System.out.println(ret.getResponse().toString());
	}

	@Override
	public void sendInvite(boolean isOnline, String notifyToken, int badgenum, GaInvite invite)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void sendInviteFeedback(boolean isOnline, String notifyToken, int badgenum, GaInviteFeedback feedback)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void sendActivityJoin(boolean isOnline, String notifyToken, int badgenum, String activityName,
			String memberName)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void sendTaskLog(boolean isOnline, String notifyToken, int badgenum, NotifyTaskLog log)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void sendApplyState(boolean isOnline, String notifyToken, int badgenum, GaApplyStateNotify notify)
	{
		// TODO Auto-generated method stub

	}

	private static LinkTemplate linkTemplateDemo()
	{
		LinkTemplate template = new LinkTemplate();
		// 设置APPID与APPKEY
		template.setAppId(appId);
		template.setAppkey(appkey);
		// 设置通知栏标题与内容
		template.setTitle("请输入通知栏标题");
		template.setText("请输入通知栏内容");
		// 配置通知栏图标
		template.setLogo("icon.png");
		// 配置通知栏网络图标
		template.setLogoUrl("");
		// 设置通知是否响铃，震动，或者可清除
		template.setIsRing(true);
		template.setIsVibrate(true);
		template.setIsClearable(true);
		// 设置打开的网址地址
		template.setUrl("http://www.baidu.com");
		return template;
	}

	@Override
	public void sendTaskLog(boolean isOnline, String notifyToken, int badgenum, MsgFlowBasicInfo nofity)
	{
		// TODO Auto-generated method stub

	}

}
