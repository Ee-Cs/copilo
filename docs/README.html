<!DOCTYPE html>
<HTML lang="en">
<META charset="UTF-8">
<BODY>
<a href="https://github.com/k1729p/Study10/tree/main/docs"><img alt="" src="images/ColorScheme.png" height="25" width="800"/></a>
<H2 id="contents">Study10 README Contents</H2>
<H3 id="top">Research the <a href="https://spring.io/projects/spring-cloud-netflix">Spring Cloud Netflix</a></H3>

<p><img alt="" src="images/Diagram.png" height="200" width="600"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>Consumer ● Producer ● Registration</I>
</p>
<p>
The microservice registration and discovery are done with the Eureka Service Discovery server.<br>
The Eureka server uses a <a href="https://resilience4j.readme.io/docs">Resilience4J</a> circuit breaker.
</p>

<p>
The sections of this project:
</p>
<OL>
<li><a href="#ONE"><b>Starting SpringBoot Servers</b></a></li>
<li><a href="#TWO"><b>Web Browser Client</b></a></li>
<li><a href="#THREE"><b>Curl Client</b></a></li>
<li><a href="#FOUR"><b>Scenario with Fallback</b></a></li>
</OL>

<p>Java source code. Packages in modules 'common', 'producer', 'consumer', and 'registration':<br>
<img alt="" src="images/aquaHR-500.png"><br>
<img alt="" src="images/aquaSquare.png"> module 'common' application sources:
	<a href="https://github.com/k1729p/Study10/tree/master/microservices-common/src/main/java/kp/common/">kp.common</a><br>
<img alt="" src="images/aquaSquare.png"> module 'producer' application sources:
		<a href="https://github.com/k1729p/Study10/tree/master/microservices-producer/src/main/java/kp/producer/">kp.producer</a><br>
<img alt="" src="images/aquaSquare.png"> module 'consumer' application sources:
		<a href="https://github.com/k1729p/Study10/tree/master/microservices-consumer/src/main/java/kp/consumer/">kp.consumer</a><br>
<img alt="" src="images/aquaSquare.png"> module 'registration' application sources:
		<a href="https://github.com/k1729p/Study10/tree/master/microservices-registration/src/main/java/kp/registration/">kp.registration</a><br>
<img alt="" src="images/aquaHR-500.png">
</p>

<p>
<img alt="" src="images/yellowHR-500.png"><br>
<img alt="" src="images/yellowSquare.png">
    <a href="http://htmlpreview.github.io/?https://github.com/k1729p/Study10/blob/main/docs/apidocs/index.html">
	Java API Documentation</a><br>
<img alt="" src="images/yellowHR-500.png">
</p>

<hr>
<h3 id="ONE">❶ Starting SpringBoot Servers</H3>

<P>Action:<br>
<img alt="" src="images/orangeHR-500.png"><br>
<img alt="" src="images/orangeSquare.png"> 1. With batch file 
'<a href="https://github.com/k1729p/Study10/blob/main/0_batch/01%20MVN%20clean%20install.bat">01 MVN clean install.bat</a>' 
build servers.<br>
<img alt="" src="images/orangeSquare.png"> 2. With batch file 
'<a href="https://github.com/k1729p/Study10/blob/main/0_batch/02%20run%20registration%20server.bat">02 run registration server.bat</a>' 
start the Eureka registration server.<br>
<img alt="" src="images/orangeSquare.png"> 3. With batch file 
'<a href="https://github.com/k1729p/Study10/blob/main/0_batch/03%20run%20producer%20server.bat">03 run producer server.bat</a>' 
start the <B>producer-service</B> server.<br>
<img alt="" src="images/orangeSquare.png"><img alt="" src="images/spacer-32.png">It registers the 1st instance of <B>producer-service</B>.<br>
<img alt="" src="images/orangeSquare.png"> 4. With batch file 
'<a href="https://github.com/k1729p/Study10/blob/main/0_batch/04%20run%20producer%20server%20(9091).bat">04 run producer server (9091).bat</a>' 
start the alternative <B>producer-service</B> server on port 9091.<br>
<img alt="" src="images/orangeSquare.png"><img alt="" src="images/spacer-32.png">It registers the 2nd instance of <B>producer-service</B>.<br>
<img alt="" src="images/orangeSquare.png"> 5. With batch file 
'<a href="https://github.com/k1729p/Study10/blob/main/0_batch/05%20run%20consumer%20server.bat">05 run consumer server.bat</a>' 
start the <B>consumer-service</B> server.<br>
<img alt="" src="images/orangeHR-500.png"></P>

<table style="border:solid">
<caption>Endpoints</caption>
<tbody>
<tr><td style="border:solid"><b>producer-service on port 8081</b></td>
<td style="border:solid"><A HREF="http://localhost:8081/content">http://localhost:8081/content</A></td></tr>
<tr><td style="border:solid"><b>producer-service on port 9091</b></td>
<td style="border:solid"><A HREF="http://localhost:9091/content">http://localhost:9091/content</A></td></tr>
<tr><td style="border:solid"><b>consumer-service</b></td>
<td style="border:solid"><A HREF="http://localhost:8082/content">http://localhost:8082/content</A></td></tr>
<tr><td style="border:solid"><b>Eureka Dashboard</b></td>
<td style="border:solid"><a href="http://localhost:8761/">http://localhost:8761/</a></td></tr>
</tbody>
</table>

<table style="border:solid">
<caption>Eureka Configuration</caption>
<tbody>
<tr><td style="border:solid"><b>producer server</b></td>
<td style="border:solid"><A HREF="https://github.com/k1729p/Study10/blob/main/microservices-producer/src/main/resources/application.yml">
application.yml</A></td></tr>
<tr><td style="border:solid"><b>consumer server</b></td>
<td style="border:solid"><A HREF="https://github.com/k1729p/Study10/blob/main/microservices-consumer/src/main/resources/application.yml">
application.yml</A></td></tr>
<tr><td style="border:solid"><b>registration server</b></td>
<td style="border:solid"><a href="https://github.com/k1729p/Study10/blob/main/microservices-registration/src/main/resources/application.yml">
application.yml</a></td></tr>
</tbody>
</table>

<p><img alt="" src="images/greenCircle.png">
1.1. In the <a href="https://github.com/k1729p/Study10/blob/main/microservices-consumer/src/main/java/kp/consumer/ConsumerApplication.java">
ConsumerApplication</a> the  web client 'RestTemplate' is configured to use a 'LoadBalancerClient'.
</p>
 
<p><img alt="" src="images/greenCircle.png">
1.2. The screenshots from the <a href="http://localhost:8761/">Eureka Dashboard</a>.
</p>
<p><img alt="" src="images/EurekaDashboardFragment.png" height="180" width="1050"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>Screenshot fragment from the Eureka Dashboard home</I>
</p>
<p><img alt="" src="images/EurekaDashboardNewlyRegisteredLeases.png" height="210" width="585"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>Screenshot fragment from the Eureka Dashboard 'registered leases' tab</I>
</P>

<p><img alt="" src="images/greenCircle.png">
1.3. The <b>registration server</b> console log
<a href="images/ConsoleLogRegistrationServer.png">screenshot</a>.
</p>

<p><img alt="" src="images/greenCircle.png">
1.4. The Eureka server endpoint with the list of registered applications:
<a href="http://localhost:8761/eureka/apps/">http://localhost:8761/eureka/apps/</a>.
</p>

<a href="#top">Back to the top of the page</a>
<hr>
<h3 id="TWO">❷ Web Browser Client</H3>
<P>Action:<br>
<img alt="" src="images/orangeHR-500.png"><br>
<img alt="" src="images/orangeSquare.png"> 1. In the web browser call <B>consumer-service</B> two times.
 Link <A HREF="http://localhost:8082/content">http://localhost:8082/content</A>.<br>
<img alt="" src="images/orangeHR-500.png"></P>

<p><img alt="" src="images/greenCircle.png">
2.1. Controllers.
</p>
<p>
The <b>consumer-service</b> controller GET method: 
<a href="https://github.com/k1729p/Study10/blob/main/microservices-consumer/src/main/java/kp/consumer/controller/ConsumerController.java#L41">
kp.consumer.controller.ConsumerController::getContent</a>.
</p>
<p>
The <a href="https://github.com/k1729p/Study10/blob/main/microservices-consumer/src/main/java/kp/consumer/service/ConsumerService.java">
kp.consumer.service.ConsumerService</a> with web client <b>RestTemplate</b> and <b>CircuitBreaker</b> for fallbacks.
</p>
<p>
The <b>consumer-service</b> service method: 
<a href="https://github.com/k1729p/Study10/blob/main/microservices-consumer/src/main/java/kp/consumer/service/ConsumerService.java#L51">
kp.consumer.service.ConsumerService::getContent</a>.
</p>
<p>
The <b>producer-service</b> controller GET method: 
<a href="https://github.com/k1729p/Study10/blob/main/microservices-producer/src/main/java/kp/producer/controller/ProducerController.java#L39">
kp.producer.controller.ProducerController::getContent</a>.
</p>

<p><img alt="" src="images/greenCircle.png">
2.2. Calling the <b>consumer-service</b> two times returns the same result in the web browser.<br>
<img alt="" src="images/ConsumerServiceContent.png" height="55" width="135"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>The result from the <b>consumer-service</b></I>
</P>

<P>The <b>consumer-service</b> was called two times.<br>
<img alt="" src="images/ConsoleLogConsumerServer.png" height="35" width="645"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>console log from the <b>consumer-service</b> server.</I></P>

<P>On the 1st call, the <b>consumer-service</b> responded with the content from the <b>producer-service</b> on port 9091.<br>
<img alt="" src="images/ConsoleLogProducerServer9091.png" height="25" width="645"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>console log from the <b>producer-service</b> server on port 9091.</I></P>

<P>On the 2nd call, the <b>consumer-service</b> responded with the content from the <b>producer-service</b> on port 8081.<br>
<img alt="" src="images/ConsoleLogProducerServer8081.png" height="25" width="645"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>console log from the <b>producer-service</b> server on port 8081.</I></P>

<p>
It is the <i>round-robin</i> load-balancing algorithm.
</p>

<a href="#top">Back to the top of the page</a>
<hr>
<h3 id="THREE">❸ Curl Client</H3>

<P>Action:<br>
<img alt="" src="images/orangeHR-500.png"><br>
<img alt="" src="images/orangeSquare.png"> 1. Execute the batch file 
<a href="https://github.com/k1729p/Study10/blob/main/0_batch/06%20CURL%20read.bat">06 CURL read.bat</a>.<br>
<img alt="" src="images/orangeHR-500.png"></P>

<P><img alt="" src="images/greenCircle.png">
3.1. The <a href="images/ConsoleLogCurl.png">
<b>screenshot</b></a> of the console log from the run of the batch file "06 CURL read.bat".
</P>

<a href="#top">Back to the top of the page</a>
<hr>
<h3 id="FOUR">❹ Scenario with Fallback</h3>

<p>Action:<br>
<img alt="" src="images/orangeHR-500.png"><br>
<img alt="" src="images/orangeSquare.png"> 1. Start the Eureka registration server.<br>
<img alt="" src="images/orangeSquare.png"> 2. Start <B>producer-service</B> server on default port 8081.<br>
<img alt="" src="images/orangeSquare.png"> 3. Start <B>consumer-service</B> server.<br>
<img alt="" src="images/orangeSquare.png"> 4. Execute <I>"06 CURL read.bat"</I>.<br>
<img alt="" src="images/orangeSquare.png"> 5. Kill the <B>producer-service</B> on default port 8081.<br>
<img alt="" src="images/orangeSquare.png"> 6. Start <B>producer-service</B>' server on alternative port 9091.<br>
<img alt="" src="images/orangeSquare.png"> 7. Execute <I>"06 CURL read.bat"</I>.<br>
<img alt="" src="images/orangeHR-500.png">
</p>

<p><img alt="" src="images/greenCircle.png">
4.1. The <B>consumer-service</B> server switches to the next available <B>producer-service</B> server.<br>
</p>
<p><img alt="" src="images/greenCircle.png">
4.2. For the absent <B>producer-service</B> there is a response "fallback" from the Resilience4J fallback method.<br>
</p>

<P><img alt="" src="images/ConsoleLogConsumerServerFallback.png" height="35" width="695"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>Console log from the <b>consumer-service</b> with the responses: normal and fallback.</I></P>

<a href="#top">Back to the top of the page</a>
<hr>
</BODY>
</HTML>