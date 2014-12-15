package org.jaeyo.clien_stream.schedule;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import org.jaeyo.clien_stream.common.Conf;
import org.jaeyo.clien_stream.common.ConfKey;
import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.parser.BbsParserImpl;

public class ScheduleManager {
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
				timer.schedule(new ParseTask(new BbsParserImpl(), bbsName), 5*1000, period);
				break;
			default:
				//TODO
			} //switch
		} //for bbsName
	} //schedule
} //class