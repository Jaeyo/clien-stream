package org.jaeyo.clien_stream.parser;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class BbsParserPark implements BbsParser {
	private static final Logger logger = LoggerFactory.getLogger(BbsParserPark.class);
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("(yyyy-MM-dd HH:mm)");

	@Override
	public ArrayList<BbsItem> parseBbs(String bbsName, int page) {
		String url = String.format("http://www.clien.net/cs2/bbs/board.php?bo_table=%s&page=%s", bbsName, page);
		try {
			ArrayList<BbsItem> items = new ArrayList<BbsItem>();

			Document doc = Jsoup.parse(new URL(url), 3000);
			Elements mytrEls = doc.getElementsByClass("mytr");
			for (Element mytrEl : mytrEls) {
				Elements tdEls = mytrEl.getElementsByTag("td");
				long num = Long.parseLong(tdEls.get(0).text());
				String title = tdEls.get(1).text();
				String imgNickPath = null;
				String nick = null;
				long date = dateFormat.parse(tdEls.get(3).child(0).attr("title")).getTime();
				long hit = Long.parseLong(tdEls.get(4).text());

				Elements imgNickEls = tdEls.get(2).getElementsByTag("img");
				if (imgNickEls.size() != 0)
					imgNickPath = imgNickEls.first().absUrl("src");
				else
					nick = tdEls.get(2).text();

				items.add(new BbsItem(num, title, nick, imgNickPath, date, hit, bbsName));
			} // for mytrEl

			return items;
		} catch (IOException e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
			return null;
		} catch (ParseException e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
			return null;
		} // catch
	} // parsePage

	@Override
	public ArticleItem parseArticle(BbsNames bbsName, long num) {
		String url=String.format("http://www.clien.net/cs2/bbs/board.php?bo_table=%s&wr_id=%s", bbsName, num);
		
		try {
			Document doc=Jsoup.parse(new URL(url), 3000);
			Element resContents=doc.getElementById("resContents");
			for(Element signatureEl : resContents.getElementsByClass("signature"))
				signatureEl.remove();
			String articleHtml=resContents.html();
			
			ArticleItem article=new ArticleItem(articleHtml, new ArrayList<ArticleReplyItem>());
			
			Element commentWrapper=doc.getElementById("comment_wrapper");
			for(Element replyBaseEl : commentWrapper.getElementsByClass("reply_base")){
				ArticleReplyItem replyItem=new ArticleReplyItem();
				Element userIdFirstChildEl=replyBaseEl.getElementsByTag("user_id").first().child(0);
				if(userIdFirstChildEl.tagName().equals("img")) {
					replyItem.setImgNickPath(userIdFirstChildEl.absUrl("src"));
				} else{
					replyItem.setNick(userIdFirstChildEl.text());
				} //if
				Element userIdSecondChildEl=replyBaseEl.getElementsByTag("user_id").first().child(1);
				replyItem.setDate(dateFormat2.parse(userIdSecondChildEl.text()).getTime());
				replyItem.setReReply(replyBaseEl.attr("style").contains("30"));
				
				article.getReplys().add(replyItem);
			} //for replyBaseEl
			
			return article;
		} catch (IOException e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
			return null;
		} catch (ParseException e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
			return null;
		} //catch
	}
} // class