package org.jaeyo.clien_stream.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class ArticleItem implements Serializable{
	private String articleHtml;
	private ArrayList<ArticleReplyItem> replys;

	public ArticleItem(String articleHtml, ArrayList<ArticleReplyItem> replys) {
		setArticleHtml(articleHtml);
		setReplys(replys);
	} //INIT

	public ArticleItem(BasicDBObject dbObj) {
		setArticleHtml(dbObj.getString("articleHtml"));
		setReplys(new ArrayList<ArticleReplyItem>());
		for(Object reply: (BasicDBList) dbObj.get("replys")){
			BasicDBObject replyDbObj=(BasicDBObject) reply;
			getReplys().add(new ArticleReplyItem(replyDbObj));
		} //for reply
	} //INIT

	public String getArticleHtml() {
		return articleHtml;
	}

	public void setArticleHtml(String articleHtml) {
		this.articleHtml = articleHtml;
	}

	public ArrayList<ArticleReplyItem> getReplys() {
		return replys;
	}

	public void setReplys(ArrayList<ArticleReplyItem> replys) {
		this.replys = replys;
	}
	
	public BasicDBObject toDBObject(){
		BasicDBObject dbObj=new BasicDBObject();
		dbObj.put("articleHtml", getArticleHtml());
		
		BasicDBList replys=new BasicDBList();
		for(ArticleReplyItem replyItem : getReplys())
			replys.add(replyItem);
		dbObj.put("replys", replys);
		
		return dbObj;
	} //toDBObject
} // class