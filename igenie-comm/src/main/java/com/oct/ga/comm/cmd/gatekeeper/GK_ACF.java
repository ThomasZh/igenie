package com.oct.ga.comm.cmd.gatekeeper;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

//GK_ACF: sessionToken,STPServer:port
public class GK_ACF
		extends RespCommand
{
	public GK_ACF()
	{
		this.setTag(Command.GK_ACF);
	}

	public GK_ACF(short respState, String gateToken, String serverIp, int port)
	{
		this();

		this.setRespState(respState);
		this.setGateToken(gateToken);
		this.setServerIp(serverIp);
		this.setPort(port);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tRespState = new TlvObject(1, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tSessionToken = new TlvObject(2, this.getGateToken());
		TlvObject tServerIp = new TlvObject(3, this.getServerIp());
		TlvObject tPort = new TlvObject(4, 4, TlvByteUtil.int2Byte(this.getPort()));

		TlvObject tlv = new TlvObject(Command.GK_ACF);
		tlv.push(tRespState);
		tlv.push(tSessionToken);
		tlv.push(tServerIp);
		tlv.push(tPort);

		logger.info("from command to tlv package:(tag=" + Command.GK_ACF + ", child=4, length=" + tlv.getLength() + ")");

		return tlv;
	}

    @Override
    public GK_ACF decode(TlvObject tlv)
            throws UnsupportedEncodingException{

        TlvParser.decodeChildren(tlv, 4);

        TlvObject tRespState = tlv.getChild(0);
        Short respState = TlvByteUtil.byte2Short(tRespState.getValue());

        TlvObject tSessionToken = tlv.getChild(1);
        String token = new String(tSessionToken.getValue(), "UTF-8");

        TlvObject tServerIp = tlv.getChild(2);
        String ip = new String(tServerIp.getValue(), "UTF-8");

        TlvObject tPort = tlv.getChild(3);
        int port = TlvByteUtil.byte2Int(tPort.getValue());

        this.setRespState(respState);
        this.setGateToken(token);
        this.setServerIp(ip);
        this.setPort(port);

        return this;
    }

	private String gateToken;
	private String serverIp;
	private int port;

	public String getServerIp()
	{
		return serverIp;
	}

	public void setServerIp(String serverIp)
	{
		this.serverIp = serverIp;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getGateToken()
	{
		return gateToken;
	}

	public void setGateToken(String gateToken)
	{
		this.gateToken = gateToken;
	}

	private final static Logger logger = LoggerFactory.getLogger(GK_ACF.class);
}
