package com.oct.ga.comm.client;

import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * only handle server response
 * 
 * @author liwenzhi
 *
 */
public class StpHandler extends IoHandlerAdapter {
	
	private final static Logger logger = LoggerFactory.getLogger(StpHandler.class);
	/**
	 * currently received command mapped by message
	 */
	private RespCommand response;

    /**
     * waiting for a batch of response @2014/12/01
     */
    private List<RespCommand> responses;


    private StpClient client;

    public StpHandler(StpClient client){
        this.client = client;
    }

    /**
     * clear last response before sending request
     * lwz7512@2014/11/05
     */
	public void resetResonse() {
        this.response = null;
        this.responses = new ArrayList<RespCommand>();
    }
	
	/**
	 * for outer use to call execute method
	 */
	public RespCommand getResponse() {
		return response;
	}

    public List<RespCommand> getResponses() {
        return this.responses;
    }

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		logger.info(">>> server response received!");

		if (message == null) return;
		
		TlvObject tlv = (TlvObject) message;
        logger.info("received tag: "+tlv.getTag());

        //FIXME, compare tag just send with
        int expectTag = (Short)session.getAttribute("tag")+1;
        logger.info("expect tag: "+expectTag);

        if (tlv.getTag() == expectTag){//if the package is wanted of request, then decode it!
            logger.info("start decode tlv: "+tlv.getTag());
            response = (RespCommand) CommandParser.decode(tlv);//save the decode message to response

            responses.add(response);//save the response;
            logger.info("***** response added and size: " + getResponses().size());

        }else {
            logger.info("tag do not match for req/resp: "+session.getAttribute("tag")+"/"+tlv.getTag());
        }

        // decode all the message to response command
		if (response == null) {
			logger.warn(">>> current pkg decode has no implementation in StpCommandParser.decode()!");
            //FIXME,暂时不关闭会话否则不能进行后续操作
            //lwz7512@2014/10/27
//			session.close(true);
			return;// break the logic blow
		}

		logger.info(">>> message parsed!");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
        logger.error("stp client exception!");

		SocketAddress rsa = session.getRemoteAddress();
		logger.error("remote address=" + ((rsa == null)? "":rsa.toString()) + " cause=" + cause.getLocalizedMessage());

        /**
         * close session while network broken
         * @2014/12/15
         */
        this.client.close();
	}

	@Override
	public void messageSent(IoSession session, Object obj) throws Exception {
		logger.info(">>> messge sent!");

		super.messageSent(session, obj);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info(">>> session closed!");
		super.sessionClosed(session);

		SocketAddress lsa = session.getLocalAddress();
		logger.info("local address=" + lsa.toString());
		SocketAddress rsa = session.getRemoteAddress();
		logger.info("remote address=" + ((rsa == null)? "":rsa.toString()));
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info(">>> session created!");
		super.sessionCreated(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		super.sessionIdle(session, status);

		if (status == IdleStatus.WRITER_IDLE)
			logger.info(">>> session idle!");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info(">>> session opened!");
		super.sessionOpened(session);

		SocketAddress lsa = session.getLocalAddress();
		logger.info("local address=" + lsa.toString());
		SocketAddress rsa = session.getRemoteAddress();
		logger.info("remote address=" + rsa.toString());
	}

}
