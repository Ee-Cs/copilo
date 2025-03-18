/**
 * Module <b>kp.client</b>
 *
 * @uses kp.service.KpService
 */
module kp.client {
	requires kp.service;
	requires java.logging;

	uses kp.service.KpService;
}