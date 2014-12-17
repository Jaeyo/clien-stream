package org.jaeyo.clien_stream.parser;

import java.util.ArrayList;

import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.ArticleItem;
import org.jaeyo.clien_stream.entity.BbsItem;

public interface BbsParser {
	public ArrayList<BbsItem> parseBbs(BbsNames bbsName, int page);
	public ArticleItem parseArticle(BbsNames bbsName, long num);
} //interface