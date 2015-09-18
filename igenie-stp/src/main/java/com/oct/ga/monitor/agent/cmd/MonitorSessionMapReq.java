package com.oct.ga.monitor.agent.cmd;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.oct.ga.monitor.agent.ByteRecvCounter;
import com.oct.ga.monitor.agent.ByteSendCounter;
import com.oct.ga.monitor.agent.CommandCounter;
import com.oct.ga.monitor.agent.PkgRecvCounter;
import com.oct.ga.monitor.agent.PkgSendCounter;
import com.oct.ga.stp.ApplicationContextUtil;
import com.oct.ga.stp.GlobalConfigurationVariables;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.oct.ga.stp.parser.StpCommandParser;

public class MonitorSessionMapReq
		extends StpReqCommand
{
	public MonitorSessionMapReq()
	{
		super();

		this.setTag(Command.MONITOR_SESSION_MAP_REQ);
	}

	public MonitorSessionMapReq(int timestamp)
	{
		this();

		this.setCurrentTimestamp(timestamp);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tTimestamp = new TlvObject(1, 4, TlvByteUtil.int2Byte(this.getCurrentTimestamp()));

		// 6 + 4
		int pkgLen = 10;
		logger.info("from command to tlv package:(tag=" + Command.MONITOR_SESSION_MAP_REQ + ", child=1, length="
				+ pkgLen + ")");
		TlvObject tlv = new TlvObject(Command.MONITOR_SESSION_MAP_REQ, pkgLen);
		tlv.add(tTimestamp);
		return tlv;
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.MONITOR_SESSION_MAP_REQ + ", child=1) to command");

		TlvParser.decodeChildren(tlv, 1);

		TlvObject tTimestamp = tlv.getChild(0);
		int timestamp = TlvByteUtil.byte2Int(tTimestamp.getValue());
		logger.debug("timestamp: " + timestamp);

		this.setCurrentTimestamp(timestamp);
		return this;
	}

	@Override
	public StpReqCommand execute()
			throws Exception
	{
		GlobalConfigurationVariables gcv = ApplicationContextUtil.getGlobalConfigurationVariables();

		String line = "---------------------------------------------------\n";
		MonitorContextResp respCmd = new MonitorContextResp(false, line);
		TlvObject tResp = StpCommandParser.encode(respCmd);
		session.write(tResp);

		// version
		String version = String.format("%-30s:%20s\n", "version", gcv.getStpVersion());
		respCmd = new MonitorContextResp(false, version);
		tResp = StpCommandParser.encode(respCmd);
		session.write(tResp);

		// monitor ������
		PkgRecvCounter pkgRecvCounter = GenericSingleton.getInstance(PkgRecvCounter.class);
		long pkgRecv = pkgRecvCounter.getCount();
		ByteRecvCounter byteRecvCounter = GenericSingleton.getInstance(ByteRecvCounter.class);
		long bytRecv = byteRecvCounter.getCount();

		// monitor ������
		PkgSendCounter pkgSendCounter = GenericSingleton.getInstance(PkgSendCounter.class);
		long pkgSend = pkgSendCounter.getCount();
		ByteSendCounter byteSendCounter = GenericSingleton.getInstance(ByteSendCounter.class);
		long bytSend = byteSendCounter.getCount();

		String count = String.format("%-30s:%20d\n", "pkgRecv", pkgRecv);
		respCmd = new MonitorContextResp(false, count);
		tResp = StpCommandParser.encode(respCmd);
		session.write(tResp);

		count = String.format("%-30s:%20d\n", "bytRecv", bytRecv);
		respCmd = new MonitorContextResp(false, count);
		tResp = StpCommandParser.encode(respCmd);
		session.write(tResp);

		count = String.format("%-30s:%20d\n", "pkgSend", pkgSend);
		respCmd = new MonitorContextResp(false, count);
		tResp = StpCommandParser.encode(respCmd);
		session.write(tResp);

		count = String.format("%-30s:%20d\n", "bytSend", bytSend);
		respCmd = new MonitorContextResp(false, count);
		tResp = StpCommandParser.encode(respCmd);
		session.write(tResp);

		respCmd = new MonitorContextResp(false, line);
		tResp = StpCommandParser.encode(respCmd);
		session.write(tResp);

		// monitor counting...
		CommandCounter commandCounter = GenericSingleton.getInstance(CommandCounter.class);
		Hashtable<Short, Long> commandMap = commandCounter.getCommandMap();
		for (Iterator<Short> itr = commandMap.keySet().iterator(); itr.hasNext();) {
			Short cmdTag = itr.next();
			Long cmdCounter = commandMap.get(cmdTag);

			count = String.format("%-30d:%20d\n", cmdTag, cmdCounter);
			respCmd = new MonitorContextResp(false, count);
			tResp = StpCommandParser.encode(respCmd);
			session.write(tResp);
		}

		line = "----------------------------------------------------------------------------------------------------------------------------\n";
		respCmd = new MonitorContextResp(false, line);
		tResp = StpCommandParser.encode(respCmd);
		session.write(tResp);

		// String title = String.format("session context: (account=" +
		// this.getSessionService().getDeviceSetMap().size()
		// + "/device=" + getSessionService().getSessionMap().size() + ")\n");
		// logger.info(title);
		// respCmd = new MonitorContextResp(false, title);
		// tResp = BaseCommandParser.encode(respCmd);
		// session.write(tResp);
		//
		// respCmd = new MonitorContextResp(false, line);
		// tResp = BaseCommandParser.encode(respCmd);
		// session.write(tResp);
		//
		// String columns = String.format("%-36s %-36s %-14s %-14s %-20s\n",
		// "deviceId", "myAccountId", "create",
		// "lastIo", "email");
		// respCmd = new MonitorContextResp(false, columns);
		// tResp = BaseCommandParser.encode(respCmd);
		// session.write(tResp);
		//
		// // deviceSetMap
		// for (Iterator<String> itr =
		// getSessionService().getDeviceSetMap().keySet().iterator();
		// itr.hasNext();) {
		// String key = itr.next();
		// String context = String.format("%-36s %-36s %-14s %-14s %-20s\n",
		// key, "", "", "", "");
		// Set<String> value = (Set<String>)
		// getSessionService().getDeviceSetMap().get(key);
		// if (value != null) {
		// for (String deviceId : value) {
		// IoSession clientSession =
		// getSessionService().getSessionMap().get(deviceId);
		// if (clientSession == null) {
		// context = String.format("%-36s %-36s %-14s %-14s %-20s\n", deviceId,
		// "", "", "", "", "");
		// } else {
		// String accountId = (String)
		// clientSession.getAttribute("myAccountId");
		// String email = (String) clientSession.getAttribute("email");
		// String createTime =
		// GaUtils.formatShortTime(clientSession.getCreationTime());
		// String lastIoTime =
		// GaUtils.formatShortTime(clientSession.getLastIoTime());
		//
		// context = String.format("%-36s %-36s %-14s %-14s %-20s\n", deviceId,
		// accountId, createTime,
		// lastIoTime, email);
		// }
		// respCmd = new MonitorContextResp(false, context);
		// tResp = BaseCommandParser.encode(respCmd);
		// session.write(tResp);
		// }
		// }
		// }

		respCmd = new MonitorContextResp(false, line);
		tResp = StpCommandParser.encode(respCmd);
		session.write(tResp);

		respCmd = new MonitorContextResp(true, null);
		tResp = StpCommandParser.encode(respCmd);
		session.write(tResp);

		// Warning: OldStpEventHandler do not response anything.
		return null;
	}

	private final static Logger logger = LoggerFactory.getLogger(MonitorSessionMapReq.class);
}
