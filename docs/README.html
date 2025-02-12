<!DOCTYPE html>
<HTML lang="en">
<META charset="UTF-8">
<BODY>
<a href="https://github.com/k1729p/Study04/tree/main/docs"><img alt="" src="images/ColorScheme.png" height="25" width="800"/></a>
<H2 id="contents">Study04 README Contents</H2>
<H3 id="top">Research the Kafka Broker and <a href="https://kafka.apache.org/documentation/streams/">Apache Kafka Streams</a></H3>

<p>
Kafka is an event streaming platform.
</p>
<p>
Kafka Storage &amp; Processing:
</p>
<ul>
 <li>Storage Layer
  <ul>
   <li>Concepts: <i>topic, partition, broker</i></li>
   <li>Kafka Broker</li>
  </ul>
 </li>
 <li>Processing Layer
  <ul>
   <li>Concepts: <i>stream, table, global table</i></li>
   <li>Kafka Streams</li>
  </ul>
 </li>
</ul>

<P>
<img alt="" src="images/MermaidFlowchart1.png" height="210" width="180"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>In the Docker container 'ptc-kafka' runs Kafka Server with single Kafka Broker.</I>
</P>

<P>
The sections of this project:
</P>
<OL>
<LI><a href="#ONE"><b>Docker Build</b></a></LI>
<LI><a href="#TWO"><b>Producer, Transformer, Consumers</b></a></LI>
<LI><a href="#THREE"><b>Comparer and Counter</b></a></LI>
<LI><a href="#FOUR"><b>Kafka Scripts</b></a></LI>
</OL>

<hr>

<P>
Java source code. Packages:<br>
<img alt="" src="images/aquaHR-500.png"><br>
<img alt="" src="images/aquaSquare.png">
    <i>application sources</i>&nbsp;:&nbsp;
	<a href="https://github.com/k1729p/Study04/tree/main/src/main/java/kp">kp</a><br>
<img alt="" src="images/aquaSquare.png">
    <i>test sources</i>&nbsp;:&nbsp;
	<a href="https://github.com/k1729p/Study04/tree/main/src/test/java/kp/processors">kp.processors</a><br>
<img alt="" src="images/aquaHR-500.png">
</P>

<P>
<img alt="" src="images/yellowHR-500.png"><br>
<img alt="" src="images/yellowSquare.png">
    <a href="http://htmlpreview.github.io/?https://github.com/k1729p/Study04/blob/main/docs/apidocs/index.html">
	Java API Documentation</a><br>
<img alt="" src="images/yellowHR-500.png">
</P>
<hr>
<H3 id="ONE">❶ Docker Build</H3>

<P>Action:<br>
<img alt="" src="images/orangeHR-500.png"><br>
<img alt="" src="images/orangeSquare.png"> 1. With batch file
 <a href="https://github.com/k1729p/Study04/blob/main/0_batch/01%20Docker%20build%20and%20run.bat">
 <I>"01 Docker build and run.bat"</I></a> build the image and<br>
<img alt="" src="images/orangeSquare.png"><img alt="" src="images/spacer-32.png">start the container with the application.<br>
<img alt="" src="images/orangeHR-500.png"></P>

<P><img alt="" src="images/greenCircle.png">
1.1. Docker image is built using these files:
<a href="https://raw.githubusercontent.com/k1729p/Study04/main/docker-config/Dockerfile"><b>Dockerfile</b></a> and
<a href="https://raw.githubusercontent.com/k1729p/Study04/main/docker-config/compose.yaml"><b>compose.yaml</b></a>.
</P>
<P>
<img alt="" src="images/ScreenshotDockerContainer.png" height="675" width="950"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>The screenshot of the created Docker containers.</I>
</P>
<a href="#top">Back to the top of the page</a>
<hr>
<H3 id="TWO">❷ Producer, Transformer, Consumers</H3>
<P>
<img alt="" src="images/MermaidFlowchart2.png" height="480" width="500"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>Docker containers for stream processing applications: producer, transformer, and two consumers</I>
</P>
<P><img alt="" src="images/greenCircle.png">
2.1. <b>Producer</b> in the container <b>ptc-producer</b> produces to topics '<b>prod-1</b>' and '<b>prod-2</b>'.<br>
Sends the 'ProducerRecord' objects with the Kafka client 'KafkaProducer'.<br>
The producer method:
<a href="https://github.com/k1729p/Study04/blob/main/src/main/java/kp/producers/KpProducer.java#L82">
kp.producers.KpProducer::produceRecordsSet</a>.
</P>
<P><img alt="" src="images/PtcProducer.png" height="365" width="365"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>The log from the container <b>ptc-producer</b>.</I>
</P>

<P><img alt="" src="images/greenCircle.png">
2.2. <b>Transformer</b> in the container <b>ptc-transformer</b> directs the records from the producer topics ('<b>prod-1</b>' and '<b>prod-2</b>') to the consumer topics ('<b>cons-1</b>' and '<b>cons-2</b>').<br>
The topology method:
<a href="https://github.com/k1729p/Study04/blob/main/src/main/java/kp/processors/KpTransformer.java#L76">
kp.processors.KpTransformer::initializeTopology</a>.
</P>
<P><img alt="" src="images/PtcTransformer.png" height="405" width="485"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>The log from the container <b>ptc-transformer</b>.</I>
</P>

<P><img alt="" src="images/greenCircle.png">
2.3. <b>Consumer</b> consumes the 'ConsumerRecord' objects with the Kafka client 'KafkaConsumer'.<br>
The consumer method:
<a href="https://github.com/k1729p/Study04/blob/main/src/main/java/kp/consumers/KpConsumer.java#L57">
kp.consumers.KpConsumer::consumeRecords</a>.
</P>

<P>
<b>Consumer</b> in the container <b>ptc-consumer-1</b> consumes from topic '<b>cons-1</b>'.<br>
<P><img alt="" src="images/PtcConsumer1.png" height="220" width="535"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>The log from the container <b>ptc-consumer-1</b>.</I>
</P>

<P>
<b>Consumer</b> in the container <b>ptc-consumer-2</b> consumes from topic '<b>cons-2</b>'.<br>
</P>
<P><img alt="" src="images/PtcConsumer2.png" height="220" width="535"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>The log from the container <b>ptc-consumer-2</b>.</I>
</P>

<a href="#top">Back to the top of the page</a>
<hr>
<H3 id="THREE">❸ Comparer and Counter</H3>
<P>
<img alt="" src="images/MermaidFlowchart3.png" height="205" width="545"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>Docker containers for stream processing applications: comparer and counter</I>
</P>

<P><img alt="" src="images/greenCircle.png">
3.1. <b>Comparer</b> in the container <b>ptc-comparer</b> compares the records in producer topics with the records in consumer topics:
</P>
<ul>
 <li>'<b>prod-1</b>' versus '<b>cons-1</b>'
 <li>'<b>prod-2</b>' versus '<b>cons-2</b>'
</ul>
<P>
The topology method:
<a href="https://github.com/k1729p/Study04/blob/main/src/main/java/kp/processors/KpComparer.java#L65">
kp.processors.KpComparer::initializeTopology</a>.
</P>
<P>
The <a href="images/PtcComparer.png">
<b>screenshot</b></a> of the log from the container <b>ptc-comparer</b>.
</P>

<P><img alt="" src="images/greenCircle.png">
3.2. <b>Counter</b> in the container <b>ptc-counter</b> counts the records in topics '<b>prod-1</b>', '<b>prod-2</b>', '<b>cons-1</b>', and '<b>cons-2</b>'.<br>
The topology method:
<a href="https://github.com/k1729p/Study04/blob/main/src/main/java/kp/processors/KpCounter.java#L60">
kp.processors.KpCounter::initializeTopology</a>.
</P>
<P>
The <a href="images/PtcCounter.png">
<b>screenshot</b></a> of the log from the container <b>ptc-counter</b>.
</P>

<a href="#top">Back to the top of the page</a>
<hr>
<H3 id="FOUR">❹ Kafka Scripts</H3>

<P>Action:<br>
<img alt="" src="images/orangeHR-500.png"><br>
<img alt="" src="images/orangeSquare.png"> 1. With batch file
 <a href="https://github.com/k1729p/Study04/blob/main/0_batch/02%20Kafka%20list%20topics.bat">
 <I>"02 Kafka list topics.bat"</I></a> run in Docker the Kafka script for topic listing.<br>
<img alt="" src="images/orangeSquare.png"> 2. With batch file
 <a href="https://github.com/k1729p/Study04/blob/main/0_batch/03%20Kafka%20consume.bat">
 <I>"03 Kafka consume.bat"</I></a> run in Docker the Kafka consumer console on topic 'cons-1'.<br>
<img alt="" src="images/orangeHR-500.png"></P>

<P><img alt="" src="images/greenCircle.png">
4.1. The batch file <I>"02 Kafka list topics.bat"</I>.<br>
The <a href="images/KafkaListTopics.png">
<b>screenshot</b></a> of the console log from the run of the batch file <I>"02 Kafka list topics.bat"</I>.
</P>

<P><img alt="" src="images/greenCircle.png">
4.2. The batch file <I>"03 Kafka consume.bat"</I>.
</P>
<P><img alt="" src="images/KafkaConsume.png" height="155" width="50"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>The console log from the run of the batch file "03 Kafka consume.bat".</I>
</P>

<a href="#top">Back to the top of the page</a>
<hr>
</BODY>
</HTML>