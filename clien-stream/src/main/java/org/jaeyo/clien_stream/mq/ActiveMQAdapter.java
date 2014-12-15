package org.jaeyo.clien_stream.mq;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQAdapter {
	private static final Logger logger=LoggerFactory.getLogger(ActiveMQAdapter.class);
	private static Connection conn;
	private static Map<MessageListener, MessageConsumer> consumers=new HashMap<MessageListener, MessageConsumer>();
	
	static{
		BrokerService broker=new BrokerService();
		broker.setBrokerName("localhost");
		broker.setUseJmx(false);
		try {
			broker.start();
		} catch (Exception e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
		} //catch
		
		ActiveMQConnectionFactory connFactory=new ActiveMQConnectionFactory();
		connFactory.setBrokerURL("vm://localhost?create=false");
		try {
			conn=connFactory.createConnection();
			conn.start();
		} catch (JMSException e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
		} //catch
	} //static
	
	
	public static void produceMessage(String topic, Serializable msg) throws JMSException{
		Session session=conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination dest=session.createTopic(topic);
		MessageProducer producer=session.createProducer(dest);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		ObjectMessage objMsg=session.createObjectMessage(msg);
		producer.send(objMsg);
		session.close();
	} //produceMessage
	
	public static void listen(String topic, MessageListener listener) throws JMSException{
		Session session=conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer consumer=session.createConsumer(session.createTopic(topic));
		consumers.put(listener, consumer);
		consumer.setMessageListener(listener);
	} //listen
	
	public static void unlisten(MessageListener listener) throws JMSException{
		MessageConsumer consumer=consumers.get(listener);
		if(consumer!=null)
			consumer.close();
	} //unlisten
} //class