package org.jaeyo.clien_stream.schedule;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.parser.BbsParserChehum;
import org.jaeyo.clien_stream.parser.BbsParserHongbo;
import org.jaeyo.clien_stream.parser.BbsParserImage;
import org.jaeyo.clien_stream.parser.BbsParserJirum;
import org.jaeyo.clien_stream.parser.BbsParserKin;
import org.jaeyo.clien_stream.parser.BbsParserPark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleManager {
	private static final Logger logger=LoggerFactory.getLogger(ScheduleManager.class);
	private static boolean isScheduled=false;
	private static Map<BbsNames, Timer> timers=new HashMap<BbsNames, Timer>();
	
	public synchronized static void schedule(){
		if(isScheduled)
			return;
		isScheduled=true;
	
		int period=Integer.parseInt(Conf.get(ConfKey.PARSE_SCHEDULE_PERIOD_BBS, "1"));
		for(BbsNames bbsName : BbsNames.values()){
			Timer timer=new Timer();
			timers.put(bbsName, timer);
			switch(bbsName){
			case PARK:
				timer.schedule(new ParseTask(new BbsParserPark(), bbsName), 5*1000, period);
				break;
			case IMAGE:
				timer.schedule(new ParseTask(new BbsParserImage(), bbsName), 5*1000, period); 
				break;
			case KIN:
				timer.schedule(new ParseTask(new BbsParserKin(), bbsName), 5*1000, period); 
				break;
			case NEWS:
				timer.schedule(new ParseTask(new BbsParserPark(), bbsName), 5*1000, period); 
				break;
			case LECTURE:
				timer.schedule(new ParseTask(new BbsParserKin(), bbsName), 5*1000, period); 
				break;
			case USE:
				timer.schedule(new ParseTask(new BbsParserKin(), bbsName), 5*1000, period); 
				break;
			case CHEHUM:
				timer.schedule(new ParseTask(new BbsParserChehum(), bbsName), 5*1000, period); 
				break;
			case USEFUL:
				timer.schedule(new ParseTask(new BbsParserPark(), bbsName), 5*1000, period); 
				break;
			case JIRUM:
				timer.schedule(new ParseTask(new BbsParserJirum(), bbsName), 5*1000, period); 
				break;
			case COUPON:
				timer.schedule(new ParseTask(new BbsParserJirum(), bbsName), 5*1000, period);
				break;
			case HONGBO:
				timer.schedule(new ParseTask(new BbsParserHongbo(), bbsName), 5*1000, period); 
				break;
			case PDS:
			case SOLD:
				continue; //not supported
			default:
				logger.error("unknown bbsName : {}", bbsName.toString());
			} //switch
		} //for bbsName
	} //schedule
} //class