package com.redoct.ga.sup.client.socket;

import java.net.InetSocketAddress;

import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.cmd.RespCommand;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;

public interface SupSocketClient
{
	public SupRespCommand send(InetSocketAddress addr, SupReqCommand request)
			throws SupSocketException;

	public RespCommand sendStpCommand(InetSocketAddress addr, ReqCommand request)
			throws SupSocketException;
}
