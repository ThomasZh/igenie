package org.restexpress.example.echo;

import org.restexpress.RestExpress;
import org.restexpress.example.echo.serialization.SerializationProvider;
import org.restexpress.util.Environment;

/**
 * The main entry-point into the RestExpress Echo example services.
 * 
 * @author toddf
 * @since Aug 31, 2009
 */
public class Main {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		RestExpress.setSerializationProvider(new SerializationProvider());
		Configuration config = Environment.load(args, Configuration.class);
		RestExpress server = new RestExpress().setName(config.getName()).setPort(config.getPort());

		defineRoutes(server, config);

		if (config.getWorkerCount() > 0) {
			server.setIoThreadCount(config.getWorkerCount());
		}

		if (config.getExecutorThreadCount() > 0) {
			server.setExecutorThreadCount(config.getExecutorThreadCount());
		}

		mapExceptions(server);
		server.bind();
		server.awaitShutdown();
	}

	/**
	 * @param server
	 * @param config
	 */
	private static void defineRoutes(RestExpress server, Configuration config) {
		// This route supports GET, POST, PUT, DELETE echoing the 'echo'
		// query-string parameter in the response.
		// GET and DELETE are also supported but require an 'echo' header or
		// query-string parameter.
		server.uri("/echo/{delay_ms}", config.getEchoController()).noSerialization();

		// Waits the delay_ms number of milliseconds and responds with a 200.
		// Supports GET, PUT, POST, DELETE methods.
		server.uri("/success/{delay_ms}.{format}", config.getSuccessController());

		// Waits the delay_ms number of milliseconds and responds with the
		// specified HTTP response code.
		// Supports GET, PUT, POST, DELETE methods.
		server.uri("/status/{delay_ms}/{http_response_code}.{format}", config.getStatusController());

		// my first test
		server.uri("/api/{command_tag}/{session_ticket}", config.getApiController());
	}

	/**
	 * @param server
	 */
	private static void mapExceptions(RestExpress server) {
		// server
		// .mapException(ItemNotFoundException.class, NotFoundException.class)
		// .mapException(DuplicateItemException.class, ConflictException.class)
		// .mapException(ValidationException.class, BadRequestException.class);
	}
}
