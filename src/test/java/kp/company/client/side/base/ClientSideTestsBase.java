package kp.company.client.side.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import kp.company.TestsBase;

/**
 * The base class for client side tests.
 * <p>
 * The server is <b>started</b>.
 * </p>
 */
public abstract class ClientSideTestsBase extends TestsBase {

	/**
	 * The port.
	 */
	@LocalServerPort
	protected int port;

	/**
	 * The {@link TestRestTemplate}.
	 */
	@Autowired
	protected TestRestTemplate restTemplate;

}