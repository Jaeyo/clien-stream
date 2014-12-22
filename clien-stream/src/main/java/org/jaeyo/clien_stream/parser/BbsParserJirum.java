package org.jaeyo.clien_stream.parser;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.ArticleItem;
import org.jaeyo.clien_stream.entity.ArticleReplyItem;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BbsParserJirum implements BbsParser {
	private static final Logger logger = LoggerFactory.getLogger(BbsParserJirum.class);
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat dateFormat2 = new SimpleDateFormat("(yyyy-MM-dd HH:mm)");

	@Override
	public ArrayList<BbsItem> parseBbs(BbsNames bbsName, int page) {
		String url = String.format("http://www.clien.net/cs2/bbs/board.php?bo_table=%s&page=%s", bbsName.toString().toLowerCase(), page);
		try {
			ArrayList<BbsItem> items = new ArrayList<BbsItem>();

			Document doc = Jsoup.parse(new URL(url), 3000);
			
			for(Element trEl : doc.getElementsByTag("tbody").first().getElementsByTag("tr")){
				if(trEl.attr("onmouseover").equals(""))
					continue;
				
				Elements tdEls=trEl.getElementsByTag("td");
				
				if(tdEls.get(4).text().trim().equals("-"))
					continue;
				
				long num = Long.parseLong(tdEls.get(0).text());
				String title = tdEls.get(2).child(0).text();
				String imgNickPath = null;
				String nick = null;
				long date = dateFormat.parse(tdEls.get(4).child(0).attr("title")).getTime();
				long hit = Long.parseLong(tdEls.get(5).text());

				Elements imgNickEls = tdEls.get(3).getElementsByTag("img");
				if (imgNickEls.size() != 0)
					imgNickPath = imgNickEls.first().absUrl("src");
				else
					nick = tdEls.get(3).text();

				items.add(new BbsItem(num, title, nick, imgNickPath, date, hit, bbsName.toString().toLowerCase()));
			} //for trEl
			
			return items;
		} catch (NumberFormatException e) {
			logger.error(String.format("%s, errmsg : %s, bbsName : %s", e.getClass().getSimpleName(), e.getMessage(), bbsName), e);
			return null;
		} catch (Exception e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
			return null;
		} // catch
	} // parsePage

//	@Override
//	public ArticleItem parseArticle(BbsNames bbsName, long num) {
//		String url=String.format("http://www.clien.net/cs2/bbs/board.php?bo_table=%s&wr_id=%s", bbsName.toString().toLowerCase(), num);
//		
//		try {
//			Document doc=Jsoup.parse(new URL(url), 3000);
//			Element resContents=doc.getElementById("resContents");
//			
//			for(Element signatureEl : resContents.getElementsByClass("signature"))
//				signatureEl.remove();
//			
//			String articleHtml=resContents.html();
//			
//			ArticleItem article=new ArticleItem(articleHtml, new ArrayList<ArticleReplyItem>());
//			
//			Element boardMainEl=doc.getElementsByClass("board_main").first();
//			Iterator<Element> replyHeadElIter=boardMainEl.getElementsByClass("reply_head").iterator();
//			Iterator<Element> replyContentElIter=boardMainEl.getElementsByClass("reply_content").iterator();
//			Iterator<Element> textareaElIter=boardMainEl.getElementsByTag("textarea").iterator();
//			
//			while(replyHeadElIter.hasNext() && replyContentElIter.hasNext() && textareaElIter.hasNext()){
//				Element replyHeadEl=replyHeadElIter.next();
//				Element replyContentEl=replyContentElIter.next();
//				Element textareaEl=textareaElIter.next();
//				
//				String replyText=textareaEl.text();
//				String nick=null, imgNickPath=null;
//				Element userIdEl=replyHeadEl.getElementsByClass("user_id").first();
//				if(userIdEl.child(0).tagName().equals("img")){
//					imgNickPath=userIdEl.child(0).absUrl("src");
//				} else{
//					nick=userIdEl.child(0).text();
//				} //if
//				long date=dateFormat2.parse(replyHeadEl.child(0).child(1).text()).getTime();
//				boolean isReReply=false;
//				
//				article.getReplys().add(new ArticleReplyItem(replyText, nick, imgNickPath, date, isReReply));
//			} //while
//			
//			return article;
//		} catch (NumberFormatException e) {
//			logger.error(String.format("%s, errmsg : %s, bbsName : %s", e.getClass().getSimpleName(), e.getMessage(), bbsName), e);
//			return null;
//		} catch (Exception e) {
//			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
//			return null;
//		} //catch
//	}
} // class