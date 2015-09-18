package org.restexpress.example.echo.controller;

import org.restexpress.Request;
import org.restexpress.Response;

public class ApiController
{
	private static final String COMMAND_TAG_HEADER = "command_tag";
	private static final String SESSION_TICKET_HEADER = "session_ticket";

	public Object create(Request request, Response response)
	{
		long delayms = 0;
		int status = Integer.valueOf(request.getHeader(COMMAND_TAG_HEADER));
		response.setResponseCode(status);
		String message = request.getHeader("echo");
		return new DelayResponse("create", delayms, message);
	}

	public Object read(Request request, Response response)
	{
		System.out.println(request.getRemoteAddress());

		long delayms = 0;
		
		int commandTag = Integer.valueOf(request.getHeader(COMMAND_TAG_HEADER));
		String sessionTicket = request.getHeader(SESSION_TICKET_HEADER);
		System.out.println(commandTag);
		System.out.println(sessionTicket);

		response.setResponseCode(200);
		return new DelayResponse("read", delayms, sessionTicket);
	}

	public Object update(Request request, Response response)
	{
		long delayms = 0;
		int status = Integer.valueOf(request.getHeader(COMMAND_TAG_HEADER));
		response.setResponseCode(status);
		String message = request.getHeader("echo");
		return new DelayResponse("update", delayms, message);
	}

	public Object delete(Request request, Response response)
	{
		long delayms = 0;
		int status = Integer.valueOf(request.getHeader(COMMAND_TAG_HEADER));
		response.setResponseCode(status);
		String message = request.getHeader("echo");
		return new DelayResponse("delete", delayms, message);
	}

}
