package org.jaeyo.clien_stream.schedule;

import java.util.ArrayList;
import java.util.TimerTask;

import javax.jms.JMSException;

import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.jaeyo.clien_stream.mq.ActiveMQAdapter;
import org.jaeyo.clien_stream.parser.BbsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseTask extends TimerTask {
	private static final Logger logger = LoggerFactory.getLogger(ParseTask.class);
	private BbsParser parser = null;
	private BbsNames bbsName;

	public ParseTask(BbsParser parser, BbsNames bbsName) {
		this.parser = parser;
		this.bbsName = bbsName;
	} // INIT

	@Override
	public void run() {
		ArrayList<BbsItem> result = parser.parsePage(bbsName.toString().toLowerCase(), 1);
		try {
			ActiveMQAdapter.produceMessage("parse-bbs", result);
		} catch (JMSException e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
		} // catch
	} // run
} // class