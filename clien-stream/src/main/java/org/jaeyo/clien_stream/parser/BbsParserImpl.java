package org.jaeyo.clien_stream.parser;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jaeyo.clien_stream.entity.BbsItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BbsParserImpl implements BbsParser {
	private static final Logger logger = LoggerFactory.getLogger(BbsParserImpl.class);
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public ArrayList<BbsItem> parsePage(String bbsName, int page) {
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
} // class