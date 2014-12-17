package org.jaeyo.clien_stream.entity;

import java.io.Serializable;

import com.mongodb.BasicDBObject;

public class ArticleReplyItem implements Serializable{
	private String replyText;
	private String nick;
	private String imgNickPath;
	private long date;
	private boolean isReReply;

	public ArticleReplyItem(){
	}  //INIT
	
	public ArticleReplyItem(String replyText, String nick, String imgNickPath, long date, boolean isReReply) {
		setReplyText(replyText);
		setNick(nick);
		setImgNickPath(imgNickPath);
		setDate(date);
		setReReply(isReReply);
	} //INIT

	public ArticleReplyItem(BasicDBObject dbObj) {
		setReplyText(dbObj.getString("replyText"));
		setNick(dbObj.getString("nick"));
		setImgNickPath(dbObj.getString("imgNickPath"));
		setDate(dbObj.getLong("date"));
		setReReply(dbObj.getBoolean("isReReply"));
	} //INIT

	public String getReplyText() {
		return replyText;
	}

	public void setReplyText(String replyText) {
		this.replyText = replyText;
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

	public boolean isReReply() {
		return isReReply;
	}

	public void setReReply(boolean isReReply) {
		this.isReReply = isReReply;
	}
	
	public BasicDBObject toDBObject(){
		BasicDBObject dbObj=new BasicDBObject();
		dbObj.put("replyText", getReplyText());
		dbObj.put("nick", getNick());
		dbObj.put("imgNickPath", getImgNickPath());
		dbObj.put("date", getDate());
		return dbObj;
	} //toDBObject
} // class