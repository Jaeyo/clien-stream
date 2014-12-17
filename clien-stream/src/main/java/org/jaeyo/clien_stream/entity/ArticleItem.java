package org.jaeyo.clien_stream.entity;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

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
			replys.add(replyItem.toDBObject());
		dbObj.put("replys", replys);
		
		return dbObj;
	} //toDBObject
	
	public String toJSON(){
		JSONObject json=new JSONObject();
		json.put("articleHtml", getArticleHtml());
		
		JSONArray replysJson=new JSONArray();
		for(ArticleReplyItem reply : replys){
			replysJson.put(reply.toJSON());
		} //for reply
		json.put("replys", replysJson.toString());
		return json.toString();
	} //toJSON
} // class