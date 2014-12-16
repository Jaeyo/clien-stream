package org.jaeyo.clien_stream.entity;

import com.mongodb.BasicDBObject;

public class BbsItem extends BasicDBObject{
	private long num;
	private String title;
	private String nick;
	private String imgNickPath;
	private long date;
	private long hit;
	private String bbsName;
	private ArticleItem article;

	public BbsItem(BasicDBObject dbObj){
		setNum(dbObj.getLong("num"));
		setTitle(dbObj.getString("title"));
		setImgNickPath(dbObj.getString("imgNickPath"));
		setDate(dbObj.getLong("date"));
		setHit(dbObj.getLong("hit"));
		setBbsName(dbObj.getString("bbsName"));
		setArticle(new ArticleItem((BasicDBObject)dbObj.get("article")));
	} //INIT
	
	public BbsItem(long num, String title, String nick, String imgNickPath, long date, long hit, String bbsName) {
		setNum(num);
		setTitle(title);
		setImgNickPath(imgNickPath);
		setDate(date);
		setHit(hit);
		setBbsName(bbsName);
	} //INIT

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
		put("num", num);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		put("title", title);
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
		put("nick", nick);
	}

	public String getImgNickPath() {
		return imgNickPath;
	}

	public void setImgNickPath(String imgNickPath) {
		this.imgNickPath = imgNickPath;
		put("imgNickPath", imgNickPath);
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
		put("date", date);
	}

	public long getHit() {
		return hit;
	}

	public void setHit(long hit) {
		this.hit = hit;
		put("hit", hit);
	}

	public String getBbsName() {
		return bbsName;
	}

	public void setBbsName(String bbsName) {
		this.bbsName = bbsName;
		put("bbsName", bbsName);
	}

	public ArticleItem getArticle() {
		return article;
	}

	public void setArticle(ArticleItem article) {
		this.article = article;
		put("article", article);
	}
} // class