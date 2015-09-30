package com.oct.ga.stp;

import java.util.Arrays;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.BadRequestException;
import org.restexpress.exception.MethodNotAllowedException;
import org.restexpress.exception.ServiceException;
import org.restexpress.exception.UnsupportedMediaTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

public class TlvWrapperController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TlvWrapperController.class);
	private BaseEventHandler baseEventHandler = new BaseEventHandler();
	private static final String CONTENT_TYPE = "application/octet-stream";

	public ByteBuf create(Request request, Response response) {
		if (!CONTENT_TYPE.equals(request.getMediaType())) {
			throw new UnsupportedMediaTypeException();
		}
		String sessionId = request.getHeader("X-Session-Id");
		LOGGER.debug("Session id: {}", sessionId);
		IoSessionAdapter ioSessionAdapter = new IoSessionAdapter();
		if (sessionId != null && sessionId.trim().length() > 0) {
			ioSessionAdapter.setHttpSessionId(sessionId);
		}
		TlvObject requestTlvObject = parseFromBody(request);
		LOGGER.debug("Request received, tag: {}, length: {}", requestTlvObject.getTag(), requestTlvObject.getLength());
		try {
			baseEventHandler.messageReceived(ioSessionAdapter, requestTlvObject);
		} catch (ServiceException e) {
			LOGGER.error("Method 'messageReceived' failed", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Method 'messageReceived' failed", e);
			throw new ServiceException(e);
		}
		TlvObject responseTlvObject = ioSessionAdapter.getResponse();
		ByteBuf byteBuf = null;
		if (responseTlvObject != null) {
			byteBuf = Unpooled.copiedBuffer(responseTlvObject.toBytes());
			LOGGER.debug("Response ready to send, tag: {}, length: {}", responseTlvObject.getTag(),
					responseTlvObject.getLength());
		} else {
			LOGGER.debug("Response body has no content");
		}
		response.setContentType(CONTENT_TYPE);
		response.setResponseStatus(HttpResponseStatus.OK);
		return byteBuf;
	}

	public Object read(Request request, Response response) {
		throw new MethodNotAllowedException(Arrays.asList(HttpMethod.POST));
	}

	public Object update(Request request, Response response) {
		throw new MethodNotAllowedException(Arrays.asList(HttpMethod.POST));
	}

	public Object delete(Request request, Response response) {
		throw new MethodNotAllowedException(Arrays.asList(HttpMethod.POST));
	}

	private TlvObject parseFromBody(Request request) {
		ByteBuf byteBuf = request.getBody();
		short tag;
		if (byteBuf.isReadable(TlvObject.TAG_LENGTH)) {
			tag = byteBuf.readShort();
			LOGGER.debug("Tag: {}", tag);
		} else {
			LOGGER.error("Read tag error");
			throw new BadRequestException();
		}
		int length;
		if (byteBuf.isReadable(TlvObject.HEADER_LENGTH - TlvObject.TAG_LENGTH)) {
			length = byteBuf.readInt();
			LOGGER.debug("Length: {}", length);
		} else {
			LOGGER.error("Read length error");
			throw new BadRequestException();
		}
		if (byteBuf.isReadable(length)) {
			byte[] bytes = new byte[length];
			byteBuf.readBytes(bytes);
			TlvObject tlvObject = new TlvObject(tag, length, bytes);
			return tlvObject;
		} else {
			LOGGER.error("Read body error");
			throw new BadRequestException();
		}
	}
}
