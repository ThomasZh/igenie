package com.oct.ga.stp.http.server;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.restexpress.RestExpress;
import org.restexpress.route.parameterized.ParameterizedRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.stp.ApplicationContextUtil;
import com.oct.ga.stp.GlobalConfigurationVariables;
import com.oct.ga.stp.http.framework.Controller;
import com.redoct.ga.sup.client.socket.SupSocketConnectionManager;

@Component
public class RestfulStpServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestfulStpServer.class);
	@Autowired
	@Controller
	private Map<String, Object> controllers;
	@Autowired
	private RestExpress server;

	@PostConstruct
	public void init() {
		LOGGER.info("Initializing server...");
		defineRoutes();
		mapExceptions();
		server.bind();
		LOGGER.info("Server is started");
	}

	@PreDestroy
	public void shutdown() {
		LOGGER.info("Shutting down server...");
		server.shutdown(true);
		LOGGER.info("Server is shutdown");
	}

	private void defineRoutes() {
		// account
		mapUri("/account/login", "loginController");
		mapUri("/account/ssologin", "ssoLoginController");
		mapUri("/account/email-register", "emailRegisterLoginController");
		mapUri("/account/phone-register", "phoneRegisterLoginController");
		mapUri("/account/apply-for-binding-phone", "applyForBindingPhoneController");
		mapUri("/account/binding-phone", "bindingPhoneController");
		mapUri("/account/apply-for-email-verificaction", "applyForEmailVerificationController");
		mapUri("/account/verify-email", "verifyEmailController");
		mapUri("/account/reset-password", "resetPwdController");
		mapUri("/account/change-password", "changePwdController");
		mapUri("/accounts/mine", "myAccountController");
		mapUri("/accounts/{accountId}/base", "accountBaseController");
		mapUri("/account/offline", "offlineController");
		mapUri("/account/logout", "logoutController");
		mapUri("/account/rebind-phone", "rebindPhoneController");
		// activity
		mapUri("/activities/{activityId}/itineraries", "itineraryController");
		mapUri("/test/email", "emailController").noSerialization();
	}

	private ParameterizedRouteBuilder mapUri(String uri, String controllerName) {
		Object controller = controllers.get(controllerName);
		if (controller == null) {
			LOGGER.error("No controller for uri \"{}\", controller name is \"{}\"", uri, controllerName);
			throw new RuntimeException("No controller for uri: " + uri);
		}
		return server.uri(uri, controller);
	}

	/**
	 * @param server
	 */
	private void mapExceptions() {
		// server
		// .mapException(ItemNotFoundException.class, NotFoundException.class)
		// .mapException(DuplicateItemException.class, ConflictException.class)
		// .mapException(ValidationException.class, BadRequestException.class);
	}

	public static void main(String args[]) throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("stp-http-context.xml");
		ApplicationContextUtil.setApplicationContext(applicationContext);
		loadSupServerState();
	}

	private static void loadSupServerState() throws IOException {
		GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext()
				.getBean("globalConfigurationVariables");
		SupSocketConnectionManager socketConnectionManager = GenericSingleton
				.getInstance(SupSocketConnectionManager.class);
		String pathname = gcv.getSupServerListPath();
		socketConnectionManager.loadFromFile(pathname);
		LOGGER.info("Load sup server list from " + pathname);
	}
}
