package com.oct.ga.comm.cmd.template;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class MakeProject2TemplateResp
		extends RespCommand
{
	public MakeProject2TemplateResp()
	{
		this.setTag(Command.MAKE_PROJECT_TO_TEMPLATE_RESP);
	}

	public MakeProject2TemplateResp(short respState, String taskId, String templateId)
	{
		this();

		this.setRespState(respState);
		this.setTaskId(taskId);
		this.setTemplateId(templateId);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tRespState = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tTaskId = new TlvObject(3, this.getTaskId());
		TlvObject tTemplateId = new TlvObject(4, this.getTemplateId());

		TlvObject tlv = new TlvObject(Command.MAKE_PROJECT_TO_TEMPLATE_RESP);
		tlv.push(tSequence);
		tlv.push(tRespState);
		tlv.push(tTaskId);
		tlv.push(tTemplateId);

		logger.info("from command to tlv package:(tag=" + Command.MAKE_PROJECT_TO_TEMPLATE_RESP + ", child=4, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String taskId;
	private String templateId;

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	private final static Logger logger = LoggerFactory.getLogger(MakeProject2TemplateResp.class);
}
