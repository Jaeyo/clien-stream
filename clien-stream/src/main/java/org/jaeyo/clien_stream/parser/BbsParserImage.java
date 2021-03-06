package org.jaeyo.clien_stream.parser;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.ArticleItem;
import org.jaeyo.clien_stream.entity.ArticleReplyItem;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BbsParserImage implements BbsParser {
	private static final Logger logger = LoggerFactory.getLogger(BbsParserImage.class);
	private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private SimpleDateFormat dateFormat2=new SimpleDateFormat("(yyyy-MM-dd HH:mm)");

	@Override
	public ArrayList<BbsItem> parseBbs(BbsNames bbsName, int page) {
		String url=String.format("http://www.clien.net/cs2/bbs/board.php?bo_table=%s&page=%s", bbsName.toString().toLowerCase(), page);
	
		ArrayList<BbsItem> items=new ArrayList<BbsItem>();
		
		try {
			Document doc=Jsoup.parse(new URL(url), 3000);
			Iterator<Element> viewHeadElsIter=doc.getElementsByClass("view_head").iterator();
			Iterator<Element> viewTitleElsIter=doc.getElementsByClass("view_title").iterator();
			while(viewHeadElsIter.hasNext() && viewTitleElsIter.hasNext()){
				Element viewHeadEl=viewHeadElsIter.next();
				Element viewTitleEl=viewTitleElsIter.next();
				
				Element userInfoEl=viewHeadEl.getElementsByClass("user_info").first().child(0);
				String nick=null, imgNickPath=null;
				if(userInfoEl.tagName().equals("img"))
					imgNickPath=userInfoEl.absUrl("src");
				else
					nick=userInfoEl.text();
				
				String[] postInfoStr=viewHeadEl.getElementsByClass("post_info").text().split(",");
				long date=dateFormat.parse(postInfoStr[0].trim()).getTime();
				long hit=Long.parseLong(postInfoStr[1].trim().replace("Hit : ", ""));
				String title=viewTitleEl.text();
				String linkWithNum=viewTitleEl.getElementsByTag("a").first().attr("href");
				long num=Long.parseLong(linkWithNum.substring(linkWithNum.indexOf("wr_id=")+"wr_id=".length(), linkWithNum.indexOf("&page=")));
				items.add(new BbsItem(num, title, nick, imgNickPath, date, hit, bbsName.toString().toLowerCase()));
			} //while
			
			return items;
		} catch (NumberFormatException e) {
			logger.error(String.format("%s, errmsg : %s, bbsName : %s", e.getClass().getSimpleName(), e.getMessage(), bbsName), e);
			return null;
		} catch (Exception e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
			return null;
		} //catch
	} // parseBbs

//	@Override
//	public ArticleItem parseArticle(BbsNames bbsName, long num) {
//		String url=String.format("http://www.clien.net/cs2/bbs/board.php?bo_table=%s&wr_id=%s", bbsName.toString().toLowerCase(), num);
//		
//		try {
//			Document doc=Jsoup.parse(new URL(url), 3000);
//			Element resContents=doc.getElementById("resContents");
//			for(Element signatureEl : resContents.getElementsByClass("signature"))
//				signatureEl.remove();
//			String articleHtml=resContents.html();
//			
//			ArticleItem article=new ArticleItem(articleHtml, new ArrayList<ArticleReplyItem>());
//			
//			Element boardMainEl=doc.getElementsByClass("board_main").first();
//			for(Element replyBaseEl : boardMainEl.getElementsByClass("reply_base")){
//				Element userIdEl=replyBaseEl.getElementsByClass("user_id").first();
//				String nick=null, imgNickPath=null;
//				if(userIdEl.child(0).tagName().equals("img")){
//					imgNickPath=userIdEl.child(0).absUrl("src");
//				} else{
//					nick=userIdEl.child(0).text();
//				} //if
//				long date=dateFormat2.parse(replyBaseEl.getElementsByClass("reply_info").first().child(1).text()).getTime();
//				String replyText=replyBaseEl.getElementsByTag("textarea").first().text();
//				boolean isReReply=replyBaseEl.attr("style").contains("30"); 
//				
//				ArticleReplyItem reply=new ArticleReplyItem(replyText, nick, imgNickPath, date, isReReply);
//				article.getReplys().add(reply);
//			} //for replyBaseEl
//			
//			return article;
//		} catch (NumberFormatException e) {
//			logger.error(String.format("%s, errmsg : %s, bbsName : %s", e.getClass().getSimpleName(), e.getMessage(), bbsName), e);
//			return null;
//		} catch (Exception e) {
//			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
//			return null;
//		} //catch
//	} // parseArticle
} // class