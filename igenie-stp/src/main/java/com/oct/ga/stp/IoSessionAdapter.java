package com.oct.ga.stp;

import java.net.SocketAddress;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestQueue;
import org.restexpress.exception.ServiceException;
import org.restexpress.exception.UnauthorizedException;

import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

public class IoSessionAdapter implements IoSession {

	private String httpSessionId;
	private TlvObject response;
	private StpSession stpSession;
	private AccountBasic accountBasic;

	@Override
	public long getId() {
		return 0;
	}

	@Override
	public IoService getService() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IoHandler getHandler() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IoSessionConfig getConfig() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IoFilterChain getFilterChain() {
		throw new UnsupportedOperationException();
	}

	@Override
	public WriteRequestQueue getWriteRequestQueue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TransportMetadata getTransportMetadata() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ReadFuture read() {
		throw new UnsupportedOperationException();
	}

	@Override
	public WriteFuture write(Object message) {
		if (message instanceof TlvObject) {
			response = (TlvObject) message;
		} else if (message == null) {
			throw new NullPointerException();
		} else {
			throw new IllegalArgumentException(message.getClass().getName());
		}
		return new WriteFutureImpl();
	}

	@Override
	public WriteFuture write(Object message, SocketAddress destination) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CloseFuture close(boolean immediately) {
		return null;
	}

	@Override
	public CloseFuture close() {
		return null;
	}

	@Override
	public Object getAttachment() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object setAttachment(Object attachment) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getAttribute(Object key) {
		if (key instanceof String) {
			String attribute = null;
			String strKey = (String) key;
			switch (strKey) {
			case "accountId":
				attribute = getStpSession().getAccountId();
				break;
			case "accountName":
				attribute = getAccountBasic().getNickname();
				break;
			case "deviceId":
				attribute = getStpSession().getDeviceId();
				break;
			case "avatarUrl":
				attribute = getAccountBasic().getAvatarUrl();
				break;
			default:
				throw new IllegalArgumentException("Unsupport key: " + key);
			}
			return attribute;
		} else {
			throw new IllegalArgumentException("Key must be a string, key: " + key);
		}
	}

	private StpSession getStpSession() {
		if (stpSession != null) {
			return stpSession;
		} else if (httpSessionId != null) {
			SupSessionService supSessionService = (SupSessionService) ApplicationContextUtil.getContext()
					.getBean("supSessionService");
			try {
				stpSession = supSessionService.queryStpSessionByTicket(httpSessionId);
			} catch (SupSocketException e) {
				throw new ServiceException(e);
			}
			if (stpSession == null) {
				throw new UnauthorizedException("No stpSession found for sessionId:" + httpSessionId);
			}
		} else {
			throw new UnauthorizedException("SessionId is null");
		}
		return stpSession;
	}

	private AccountBasic getAccountBasic() {
		if (accountBasic != null) {
			return accountBasic;
		} else {
			StpSession stpSession = getStpSession();
			SupAccountService supAccountService = (SupAccountService) ApplicationContextUtil.getContext()
					.getBean("supAccountService");
			try {
				accountBasic = supAccountService.queryAccount(stpSession.getAccountId());
			} catch (SupSocketException e) {
				throw new ServiceException(e);
			}
			if (accountBasic == null) {
				throw new ServiceException("No accountBasic found for accountId: " + stpSession.getAccountId());
			}
		}
		return accountBasic;
	}

	@Override
	public Object getAttribute(Object key, Object defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object setAttribute(Object key, Object value) {
		if (key instanceof String) {
			String strKey = (String) key;
			switch (strKey) {
			case "accountId":
			case "accountName":
			case "deviceId":
			case "avatarUrl":
				// discard key and value, do nothing
				break;
			default:
				throw new IllegalArgumentException("Unsupport key: " + key);
			}
			return null;
		} else {
			throw new IllegalArgumentException("Key must be a string, key: " + key);
		}
	}

	@Override
	public Object setAttribute(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object setAttributeIfAbsent(Object key, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object setAttributeIfAbsent(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object removeAttribute(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAttribute(Object key, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean replaceAttribute(Object key, Object oldValue, Object newValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAttribute(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Object> getAttributeKeys() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isConnected() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isClosing() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CloseFuture getCloseFuture() {
		throw new UnsupportedOperationException();
	}

	@Override
	public SocketAddress getRemoteAddress() {
		throw new UnsupportedOperationException();
	}

	@Override
	public SocketAddress getLocalAddress() {
		throw new UnsupportedOperationException();
	}

	@Override
	public SocketAddress getServiceAddress() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCurrentWriteRequest(WriteRequest currentWriteRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void suspendRead() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void suspendWrite() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void resumeRead() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void resumeWrite() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isReadSuspended() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWriteSuspended() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateThroughput(long currentTime, boolean force) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getReadBytes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getWrittenBytes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getReadMessages() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getWrittenMessages() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getReadBytesThroughput() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getWrittenBytesThroughput() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getReadMessagesThroughput() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getWrittenMessagesThroughput() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getScheduledWriteMessages() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getScheduledWriteBytes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getCurrentWriteMessage() {
		throw new UnsupportedOperationException();
	}

	@Override
	public WriteRequest getCurrentWriteRequest() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getCreationTime() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLastIoTime() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLastReadTime() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLastWriteTime() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isIdle(IdleStatus status) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isReaderIdle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWriterIdle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isBothIdle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getIdleCount(IdleStatus status) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getReaderIdleCount() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getWriterIdleCount() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getBothIdleCount() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLastIdleTime(IdleStatus status) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLastReaderIdleTime() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLastWriterIdleTime() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLastBothIdleTime() {
		throw new UnsupportedOperationException();
	}

	public TlvObject getResponse() {
		return response;
	}

	public void setHttpSessionId(String httpSessionId) {
		this.httpSessionId = httpSessionId;
	}

	private class WriteFutureImpl implements WriteFuture {

		@Override
		public IoSession getSession() {
			return IoSessionAdapter.this;
		}

		@Override
		public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
			return true;
		}

		@Override
		public boolean await(long timeoutMillis) throws InterruptedException {
			return true;
		}

		@Override
		public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
			return true;
		}

		@Override
		public boolean awaitUninterruptibly(long timeoutMillis) {
			return true;
		}

		@Override
		public void join() {

		}

		@Override
		public boolean join(long timeoutMillis) {
			return true;
		}

		@Override
		public boolean isDone() {
			return true;
		}

		@Override
		public boolean isWritten() {
			return true;
		}

		@Override
		public Throwable getException() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWritten() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setException(Throwable cause) {
			throw new UnsupportedOperationException();
		}

		@Override
		public WriteFuture await() throws InterruptedException {
			return this;
		}

		@Override
		public WriteFuture awaitUninterruptibly() {
			return this;
		}

		@Override
		public WriteFuture addListener(IoFutureListener<?> listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public WriteFuture removeListener(IoFutureListener<?> listener) {
			throw new UnsupportedOperationException();
		}

	}
}
