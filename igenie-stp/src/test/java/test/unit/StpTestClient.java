package test.unit;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.cmd.task.UploadTaskNoteReq;
import com.oct.ga.comm.codec.TlvPackageCodecFactory;
import com.oct.ga.comm.domain.task.TaskNote;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.parser.StpCommandParser;

public class StpTestClient
{
	private final static Logger logger = LoggerFactory.getLogger(StpTestClient.class);

	private static IoConnector connector;
	private static IoSession session;

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		connector = new NioSocketConnector();
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
		connector.getSessionConfig().setReadBufferSize(GlobalArgs.BUFFER_SIZE); // 4k
		connector.setHandler(new StpTestHandler());

		ConnectFuture connFuture = connector.connect(new InetSocketAddress("localhost", 13102));
		connFuture.awaitUninterruptibly();
		session = connFuture.getSession();

	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
		session.close(true);
		connector.dispose(true);
		logger.debug(">>> connection recyled!");
	}

	@Test
	public void testTaskNoteAdd()
	{
		logger.debug("sending tasknote...");

		TaskNote tn = new TaskNote();
		tn.setNoteId("9");
		tn.setTaskId("000");
		tn.setAccountId("abc-def-ghi");
		tn.setAccountName("lwz7512");
		tn.setTimestamp((int) (System.currentTimeMillis() / 1000));
		tn.setTxt("this is a test task note!");

		UploadTaskNoteReq cmd = new UploadTaskNoteReq(tn);
		try {
			TlvObject tlv = StpCommandParser.encode(cmd);

			session.write(tlv);// send to server

			Thread.sleep(30000);// waiting server response...
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}// timeout 30s

	}

	@Test
	public void testTaskNoteMod()
	{
		// TODO, ...

	}

	@Test
	public void testTaskNoteDel()
	{
		// TODO, ...

	}

}// end of StpTestClient
