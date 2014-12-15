package org.jaeyo.clien_stream.entity;

import java.io.Serializable;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BbsItem implements Serializable{
	private long num;
	private String title;
	private String nick;
	private String imgNickPath;
	private long date;
	private long hit;
	private String bbsName;

	public BbsItem(long num, String title, String nick, String imgNickPath, long date, long hit, String bbsName) {
		this.num = num;
		this.title = title;
		this.nick = nick;
		this.imgNickPath = imgNickPath;
		this.date = date;
		this.hit = hit;
		this.bbsName = bbsName;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getImgNickPath() {
		return imgNickPath;
	}

	public void setImgNickPath(String imgNickPath) {
		this.imgNickPath = imgNickPath;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getHit() {
		return hit;
	}

	public void setHit(long hit) {
		this.hit = hit;
	}

	public String getBbsName() {
		return bbsName;
	}

	public void setBbsName(String bbsName) {
		this.bbsName = bbsName;
	}
	
	public DBObject toDBObject(){
		BasicDBObject retDbObj = new BasicDBObject();
		retDbObj.append("_id", getNum());
		retDbObj.append("title", getTitle());
		retDbObj.append("nick", getNick());
		retDbObj.append("imgNickPath", getImgNickPath());
		retDbObj.append("date", getDate());
		retDbObj.append("hit", getHit());	
		return retDbObj;
	} //toDBObject
} // class