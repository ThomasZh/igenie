/**
 * 
 */
package com.oct.ga.comm.client;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.List;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.StpCommand;
import com.oct.ga.comm.codec.TlvPackageCodecFactory;
import com.oct.ga.comm.tlv.TlvObject;

/**
 * service transfer protocol client:
 * 
 * design to talk to stp server/service, currently for web app use
 * 
 * @author liwenzhi
 * 
 */
public class StpClient
{
	private final static Logger logger = LoggerFactory.getLogger(StpClient.class);

	private IoConnector connector;
	private IoSession session;

	private StpHandler messageHandler;

    /**
     * only one request globally allowed
     * @2014/12/04
     */
    private boolean running;

    /**
     * session obtained
     */
    private boolean connected;

	/**
	 * init stp client base values
	 */
	public StpClient()
	{
		connector = new NioSocketConnector();
		// 过滤器（自定义协议）
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
		// 设置数据将被读取的缓冲区大小
		connector.getSessionConfig().setReadBufferSize(4096); // 4k

		messageHandler = new StpHandler(this);
		connector.setHandler(messageHandler);

	}

    /**
     * check the client status, then decide whether to query
     *
     * @return
     */
    public boolean isRunning(){
        return running;
    }

    /**
     * check session availability
     *
     * @return
     */
    public boolean isConnected(){
        return connected;
    }

    /**
	 * after registerCommand, call this
	 */
	public void start(String ip, int port){

        running = true;
        connected = false;

		ConnectFuture connFuture = connector.connect(new InetSocketAddress(ip, port));
		connFuture.awaitUninterruptibly();// setup connection...

		session = connFuture.getSession();

        running = false;

		logger.info(">>> stp client connected...");

        if(session != null) connected = true;

	}

	/**
	 * send request to server and wait for response
	 * 
	 * @param request
	 * @return response
	 * @throws InterruptedException
	 * @throws java.io.UnsupportedEncodingException
	 */
	public RespCommand send(StpCommand request) throws InterruptedException, UnsupportedEncodingException
	{
        if(running){
            logger.warn(">>>> only one request allowed!");
            return null;
        }

        running = true;//open running state

        //FIXME, remember this command just send and check it in messageHandler.messageReceived
        //lwz7512@2014/10/28
        session.setAttribute("tag", request.getTag());
        //FIXME, clear last response, to avoid return last response in current request
        //lwz7512@2014/11/05
        messageHandler.resetResonse();

        TlvObject msg = request.encode();
		WriteFuture writeFuture = session.write(msg);
		writeFuture.awaitUninterruptibly();

		if (writeFuture.getException() != null) {//network connection broken!
			session.getConfig().setUseReadOperation(false);
            running = false;
            connected = false;
            logger.warn(">>>> write message exception!");
			return null;
		}

        logger.info(">>>>>>>>> open read mode ...");
		session.getConfig().setUseReadOperation(true);

		final ReadFuture readFuture = session.read();

		readFuture.awaitUninterruptibly();// read response message

        logger.info(">>>>>>>>> read message complete ...");
		if (readFuture.getException() != null) {
			session.getConfig().setUseReadOperation(false);
            running = false;
            connected = false;
            logger.warn(">>>> read message exception!");
			return null;
		}

        // stop blocking inbound messages
		session.getConfig().setUseReadOperation(false);

        //reset the status
        running = false;

		return messageHandler.getResponse();

	}

    /**
     * get responses in same type
     * @2014/12/01
     * @return
     */
    public List<RespCommand> getResponses(){
        return messageHandler.getResponses();
    }

    /**
     * close connection
     */
    public void close() {
        if (session != null) session.close(true);

        connected = false;
        running = false;
    }

    /**
     * check session available
     * @return
     */
    public boolean available() {
        if(session==null) return false;
        return true;
    }


}
