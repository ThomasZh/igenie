package com.oct.ga.stp.http.club.controller;

import java.nio.charset.Charset;

import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class EmailController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

	public String create(Request request, Response response) {
		ByteBuf byteBuf = request.getBody();
		if (byteBuf.isReadable(2)) {
			short tag = byteBuf.readShort();
			LOGGER.info("tag:{}", tag);
		} else {
			LOGGER.error("read tag error");
			response.setResponseStatus(HttpResponseStatus.BAD_REQUEST);
			return null;
		}
		int length = 0;
		if (byteBuf.isReadable(4)) {
			length = byteBuf.readInt();
			LOGGER.info("length:{}", length);
		} else {
			LOGGER.error("read length error");
			response.setResponseStatus(HttpResponseStatus.BAD_REQUEST);
			return null;
		}
		if (byteBuf.isReadable(length)) {
			byte[] bytes = new byte[length];
			byteBuf.readBytes(bytes);
			String body = new String(bytes, Charset.forName("UTF-8"));
			LOGGER.info("body:{}", body);
			response.setResponseStatus(HttpResponseStatus.OK);
			return body;
		} else {
			LOGGER.error("read body error");
			response.setResponseStatus(HttpResponseStatus.BAD_REQUEST);
			return null;
		}
	}

}
