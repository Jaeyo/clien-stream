package org.jaeyo.clien_stream.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import org.json.JSONObject;

import com.mongodb.BasicDBObject;

public class BbsItem implements Serializable{
	private static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private long num;
	private String title;
	private String nick;
	private String imgNickPath;
	private long date;
	private long hit;
	private String bbsName;
	private ArticleItem article;

	public BbsItem(BasicDBObject dbObj) {
		setNum(dbObj.getLong("num"));
		setTitle(dbObj.getString("title"));
		setImgNickPath(dbObj.getString("imgNickPath"));
		setDate(dbObj.getLong("date"));
		setHit(dbObj.getLong("hit"));
		setBbsName(dbObj.getString("bbsName"));
		if(dbObj.get("article")!=null)
			setArticle(new ArticleItem((BasicDBObject) dbObj.get("article")));
	} // INIT

	public BbsItem(long num, String title, String nick, String imgNickPath, long date, long hit, String bbsName) {
		setNum(num);
		setTitle(title);
		setImgNickPath(imgNickPath);
		setDate(date);
		setHit(hit);
		setBbsName(bbsName);
	} // INIT

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

	public ArticleItem getArticle() {
		return article;
	}

	public void setArticle(ArticleItem article) {
		this.article = article;
	}

	public BasicDBObject toDBObject() {
		BasicDBObject dbObj = new BasicDBObject();
		dbObj.put("num", getNum());
		dbObj.put("title", getTitle());
		dbObj.put("imgNickPath", getImgNickPath());
		dbObj.put("date", getDate());
		dbObj.put("hit", getHit());
		dbObj.put("bbsName", getBbsName());
		
		if(getArticle()!=null)
			dbObj.put("article", getArticle().toDBObject());
		
		return dbObj;
	} // toDBObject
	
	public String toJSON(){
		JSONObject json=new JSONObject();
		json.put("num", getNum());
		json.put("title", getTitle());
		json.put("nick", getNick());
		json.put("imgNickPath", getImgNickPath());
		json.put("date", dateFormat.format(getDate()));
		json.put("hit", getHit());
		json.put("bbsName", getBbsName());
		json.put("article", getArticle().toJSON());
		return json.toString();
	} //toJSON
} // class /